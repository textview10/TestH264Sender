package com.laifeng.sopcastsdk.stream.sender.tcp.interf;

/**
 * Created by xu.wang
 * Date on  2017/11/23 19:23:36.
 *
 * @Desc 从tcp read thread中的回调
 */

public interface OnTcpReadListener {

    void socketDisconnect();    //断开连接

    void connectSuccess();  //收到server消息,连接成功.
}
