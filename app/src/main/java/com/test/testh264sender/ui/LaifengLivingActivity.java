package com.test.testh264sender.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.laifeng.sopcastsdk.camera.CameraListener;
import com.laifeng.sopcastsdk.configuration.CameraConfiguration;
import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.stream.packer.tcp.TcpPacker;
import com.laifeng.sopcastsdk.stream.sender.OnSenderListener;
import com.laifeng.sopcastsdk.stream.sender.tcp.TcpSender;
import com.laifeng.sopcastsdk.ui.CameraLivingView;
import com.test.testh264sender.Constant;
import com.test.testh264sender.R;

/**
 * Created by xu.wang
 * Date on  2018/5/28 10:36:29.
 *
 * @Desc
 */

public class LaifengLivingActivity extends AppCompatActivity {
    private static final String TAG = "LaifengLivingActivity";
    private VideoConfiguration mVideoConfiguration;
    private int mCurrentBps;
    private int mOrientation = 0;
    private long startCameraMil;
    private long successCameraMil;
    private boolean isFirst = true;
    private TcpSender mTcpSender;

    private CameraLivingView cameraLivingView;
    private AppCompatButton btn_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        initialView();
    }

    private void initialView() {
        cameraLivingView = findViewById(R.id.clv_laifeng_living);
        btn_start = findViewById(R.id.btn_living_start);
        initialLiving();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLivingView.start();
                mTcpSender.start();
                mTcpSender.connect();
            }
        });

    }

    //0竖屏, 1横屏
    private void initialLiving() {
        if (mOrientation == 0) {
            CameraConfiguration.Builder cameraBuilder = new CameraConfiguration.Builder();
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            cameraBuilder.setOrientation(CameraConfiguration.Orientation.PORTRAIT).setFacing(CameraConfiguration.Facing.BACK);
            CameraConfiguration cameraConfiguration = cameraBuilder.build();
            cameraLivingView.setCameraConfiguration(cameraConfiguration);
            mVideoConfiguration = new VideoConfiguration.Builder().setSize(1080, 1920).build();
        } else {
            CameraConfiguration.Builder cameraBuilder = new CameraConfiguration.Builder();
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            cameraBuilder.setOrientation(CameraConfiguration.Orientation.LANDSCAPE).setFacing(CameraConfiguration.Facing.BACK);
            CameraConfiguration cameraConfiguration = cameraBuilder.build();
            cameraLivingView.setCameraConfiguration(cameraConfiguration);
            mVideoConfiguration = new VideoConfiguration.Builder().setSize(1920, 1080).build();
        }
        cameraLivingView.setVideoConfiguration(mVideoConfiguration);
        startCameraMil = System.currentTimeMillis();
        TcpPacker packer = new TcpPacker();
        packer.setSendAudio(false);
        cameraLivingView.setPacker(packer);    //设置发送器
        mTcpSender = new TcpSender(Constant.ip, Constant.port);
        mTcpSender.setSenderListener(mSenderListener);
        cameraLivingView.setSender(mTcpSender);
        cameraLivingView.setCameraOpenListener(new CameraListener() {
            @Override
            public void onOpenSuccess() {
                Log.e(TAG, "openCamera success");
                if (isFirst) {
                    isFirst = false;
                }
            }

            @Override
            public void onOpenFail(int error) {
                Log.e(TAG, "openCamera error" + error);
            }

            @Override
            public void onCameraChange() {
                Log.e(TAG, "Camera switch");
            }
        });

        cameraLivingView.setLivingStartListener(new CameraLivingView.LivingStartListener() {
            @Override
            public void startError(int error) {
                Log.e(TAG, "living start error ... error_code" + error);
            }

            @Override
            public void startSuccess() {
                Log.e(TAG, "living start success");
            }
        });
    }


    private OnSenderListener mSenderListener = new OnSenderListener() {
        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnected() {
            Log.e(TAG, "onConnect success...");
            cameraLivingView.start();
            mCurrentBps = mVideoConfiguration.maxBps;
        }

        @Override
        public void onDisConnected() {
            cameraLivingView.stop();
            Log.e(TAG, "onDisConnect");
        }

        @Override
        public void onPublishFail() {
            cameraLivingView.stop();
            Log.e(TAG, "onPublishFail...");
        }

        @Override
        public void onNetGood() {
            if (mCurrentBps + 50 <= mVideoConfiguration.maxBps) {
                int bps = mCurrentBps + 50;
                if (cameraLivingView != null) {
                    boolean result = cameraLivingView.setVideoBps(bps);
                    if (result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                Log.d(TAG, "BPS_CHANGE good good good");
            }
        }

        @Override
        public void onNetBad() {
            if (mCurrentBps - 100 >= mVideoConfiguration.minBps) {
                int bps = mCurrentBps - 100;
                if (cameraLivingView != null) {
                    boolean result = cameraLivingView.setVideoBps(bps);
                    if (result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                Log.d(TAG, "BPS_CHANGE bad down 100");
            }
        }
    };
}
