package com.laifeng.sopcastsdk.stream.sender.tcp;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.laifeng.sopcastsdk.stream.sender.tcp.interf.OnTcpReadListener;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by xu.wang
 * Date on  2017/11/21 16:43:11.
 *
 * @Desc
 */

public class TcpReadThread extends Thread {
    private final static String TAG = "TcpReadThread";
    private BufferedInputStream bis;
    private OnTcpReadListener mListener;
    private volatile boolean startFlag;

    public TcpReadThread(BufferedInputStream bis, OnTcpReadListener listener) {
        this.bis = bis;
        this.mListener = listener;
        startFlag = true;
    }

    @Override
    public void run() {
        super.run();
        while (startFlag) {
            SystemClock.sleep(50);
            try {
                acceptMsg();
            } catch (IOException e) {
                startFlag = false;
                mListener.socketDisconnect();
//                Log.e(TAG, "read data Exception = " + e.toString());
            }
        }
    }

    public void shutDown() {
        startFlag = false;
        this.interrupt();
    }

    public void acceptMsg() throws IOException {
        if (mListener == null) return;
        if (bis.available() <= 0) {
            return;
        }
        byte[] bytes = new byte[2];
        bis.read(bytes);
        String s = new String(bytes);
        if(TextUtils.isEmpty(s)){
            return;
        }
        if (TextUtils.equals(s, "OK")) {
            mListener.connectSuccess();
        }
    }
}
