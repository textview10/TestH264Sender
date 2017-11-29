package com.laifeng.sopcastsdk.stream.sender;

/**
 * Created by xu.wang
 * Date on  2017/11/28 16:31:55.
 *
 * @Desc
 */

public interface OnSenderListener {
    void onConnecting();
    void onConnected();
    void onDisConnected();
    void onPublishFail();
    void onNetGood();
    void onNetBad();
}
