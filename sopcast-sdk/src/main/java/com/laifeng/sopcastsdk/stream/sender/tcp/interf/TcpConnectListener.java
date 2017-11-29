package com.laifeng.sopcastsdk.stream.sender.tcp.interf;

/**
 * Created by xu.wang
 * Date on  2017/11/27 11:52:34.
 *
 * @Desc
 */

public interface TcpConnectListener {
    void onSocketConnectSuccess();
    void onSocketConnectFail();
    void onTcpConnectSuccess();
    void onTcpConnectFail();
    void onPublishSuccess();
    void onPublishFail();
    void onSocketDisconnect();
}
