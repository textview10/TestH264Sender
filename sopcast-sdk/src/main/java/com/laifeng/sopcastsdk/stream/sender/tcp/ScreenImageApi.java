package com.laifeng.sopcastsdk.stream.sender.tcp;

/**
 * Created by xu.wang
 * Date on  2017/11/22 15:08:42.
 *
 * @Desc
 */

public class ScreenImageApi {
    public static final byte encodeVersion1 = 0x00;       //版本号1

    public class RECORD {   //录屏指令
        public static final int MAIN_CMD = 1; //录屏主指令
        public static final int SEND_BUFF = 11;//发送声音的BUFF
    }

}
