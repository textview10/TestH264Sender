package com.zonekey.testh264sender;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.screen.ScreenRecordActivity;
import com.laifeng.sopcastsdk.stream.packer.tcp.TcpPacker;
import com.laifeng.sopcastsdk.stream.sender.OnSenderListener;
import com.laifeng.sopcastsdk.stream.sender.local.LocalSender;
import com.laifeng.sopcastsdk.stream.sender.tcp.TcpSender;

import java.io.File;
import java.io.IOException;

public class MainActivity extends ScreenRecordActivity implements OnSenderListener {

    private AppCompatButton btn_start;
    private String ip = "192.168.13.193";
    private int port = 11111; //pc接受直播命令的端口号
    private VideoConfiguration mVideoConfiguration;
    private TcpSender tcpSender;
    private final static String TAG = "MainActivity";
    private boolean isRecord = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalView();
    }

    private void initalView() {
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecord) {
                    requestRecording();
                    Log.e("Test", "start record");
                } else {
                    stopRecording();
                    Log.e("Test", "stop record");
                }
            }
        });

        initialData();
    }

    private void initialData() {
        String path = Environment.getExternalStorageDirectory() + "/test";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, "test.h264");
        if (file1.exists()) {
            file1.delete();
        }
        try {
            file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Test", "" + e.toString());
        }
    }

    @Override
    protected void requestRecordSuccess() {
        super.requestRecordSuccess();
        isRecord = true;
        startRecord();
    }

    @Override
    protected void requestRecordFail() {
        super.requestRecordFail();
    }

    private void startRecord() {
        TcpPacker packer = new TcpPacker();
//        FlvPacker flvPacker = new FlvPacker();
//        packer.initAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mVideoConfiguration = new VideoConfiguration.Builder().build();
        setVideoConfiguration(mVideoConfiguration);
        setRecordPacker(packer);

        tcpSender = new TcpSender(ip, port);
        tcpSender.setSenderListener(this);
        tcpSender.setVideoParams(mVideoConfiguration);
        tcpSender.connect();
        LocalSender localSender = new LocalSender();
        setRecordSender(tcpSender);
        startRecording();
    }

    @Override
    public void onConnecting() {
        Log.e(TAG, "onConnecting ...");
    }

    @Override
    public void onConnected() {
        Log.e(TAG, "onConnected");
    }

    @Override
    public void onDisConnected() {
        Log.e(TAG, "onDisConnected");
    }

    @Override
    public void onPublishFail() {
        Log.e(TAG, "onPublishFail");
    }

    @Override
    public void onNetGood() {
        Log.e(TAG, "onNetGood");
    }

    @Override
    public void onNetBad() {
        Log.e(TAG, "onNetBad");
    }


    @Override
    public void finish() {
        super.finish();
        tcpSender.stop();
    }
}
