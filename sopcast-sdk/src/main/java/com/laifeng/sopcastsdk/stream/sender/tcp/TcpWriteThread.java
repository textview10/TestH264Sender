package com.laifeng.sopcastsdk.stream.sender.tcp;

import android.os.SystemClock;
import android.util.Log;

import com.laifeng.sopcastsdk.entity.Frame;
import com.laifeng.sopcastsdk.stream.sender.rtmp.io.OnWriteListener;
import com.laifeng.sopcastsdk.stream.sender.rtmp.packets.Video;
import com.laifeng.sopcastsdk.stream.sender.sendqueue.ISendQueue;
import com.laifeng.sopcastsdk.stream.sender.tcp.interf.OnTcpWriteListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by xu.wang
 * Date on  2017/11/21 16:46:03.
 *
 * @Desc
 */

public class TcpWriteThread extends Thread {
    private BufferedOutputStream bos;
    private ISendQueue iSendQueue;
    private volatile boolean startFlag;
    private OnTcpWriteListener mListener;
    private final String TAG = "TcpWriteThread";

    public TcpWriteThread(BufferedOutputStream bos, ISendQueue sendQueue, OnTcpWriteListener listener) {
        this.bos = bos;
        startFlag = true;
        this.iSendQueue = sendQueue;
        this.mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        while (startFlag) {
            Frame frame = iSendQueue.takeFrame();
            if (frame == null) {
                continue;
            }
            if (frame.data instanceof Video) {
                sendData(((Video) frame.data).getData());
                Log.e(TAG,"send a msg" );
            }
        }
    }


    public void shutDown() {
        startFlag = false;
        this.interrupt();
    }

    public void sendData(byte[] buff) {
        try {
            EncodeV1 encodeV1 = new EncodeV1(buff);
            bos.write(encodeV1.buildSendContent());
            bos.flush();
            Log.e(TAG,"send data ");
        } catch (IOException e) {
            startFlag = false;
//            Log.e("TcpWriteThread", "sendData Exception =" + e.toString());
            mListener.socketDisconnect();
        }
    }


}
