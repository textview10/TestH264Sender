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


}
