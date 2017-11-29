package com.laifeng.sopcastsdk.stream.sender.tcp;

/**
 * Created by xu.wang
 * Date on  2017/11/22 15:08:42.
 *
 * @Desc
 */

public class Api {
    /*#########################与window客户端通信使用#######################################*/
    /*--------------------------与设备相关的常量------------------------------------------------*/
    public static final byte encodeVersion1 = (byte) 0x00;
    public static final byte encodeVersion2 = (byte) 0x00;
    public static final byte encodeVersion3 = (byte) 0x01;     //第二版数据格式
    public static final byte PLATFORM = (byte) 0x21;    //发送平台指令,表明来自移动教学平台
    public static final byte MACHINE_TYPE = (byte) 0x42; //发送设备类型指令,表明来自Android_phone
    public static final byte MAIN_COMMAND_2 = (byte) 0x00;
    public static final byte SUB_COMMAND_2 = (byte) 0x00;

    //############心跳包指令###########

    //###########扩音指令 MEDIA_STREAM
    public static final byte Main_MediaStream = (byte) 0xA1;  //扩音主指令
    public static final byte Push_AudioBuffer = (byte) 0x01;  //音频buffer数据
    public static final byte Request_StartAudio = (byte) 0x21;  //开始发送音频
    public static final byte Request_StopAudio = (byte) 0x22;  //停止发送音频

    //视频直播指令

    public static byte ASk_VIDEO_STATUS = (byte) 0x20;
    public static byte ASk_VIDEO_ATTRIBUTE = (byte) 0x21;
    public static byte PUSH_VIDEO_ATTRIBUTE = (byte) 0x22;
    public static byte RESPONSE_READYFORVIDEO = (byte) 0x23;
    public static byte RESPONSE_FAIL_TO_START_VIDEO = (byte) 0x24;
    public static final byte PUSH_VIDEO_BUFFER = (byte) 0x25;
    public static final byte REQUEST_STOP_VIDEO = (byte) 0x28;
    public static final byte RESPONSE_STOP_VIDEO = (byte) 0x29;
    public static final byte Push_VideoBufferHeader = (byte) 0x26;
}
