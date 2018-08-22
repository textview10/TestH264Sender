package com.laifeng.sopcastsdk.stream.sender.tcp;

import java.nio.ByteBuffer;

/**
 * Created by xu.wang
 * Date on  2017/11/23 18:50:09.
 *
 * @Desc
 */

public class EncodeV1 {
    private byte encodeVersion;
    private int mainCmd;
    private int subCmd;
    private String body;
    private byte[] buff;    //要发送的内容

    public EncodeV1(byte encodeVersion, int mainCmd, int subCmd, byte[] buff) {
        this.encodeVersion = encodeVersion;
        this.mainCmd = mainCmd;
        this.subCmd = subCmd;
        this.body = null;
        this.buff = buff;
    }

    public EncodeV1(byte encodeVersion, int mainCmd, int subCmd, String body, byte[] buff) {
        this.encodeVersion = encodeVersion;
        this.mainCmd = mainCmd;
        this.subCmd = subCmd;
        this.body = body;
        this.buff = buff;
    }

    public byte[] buildSendContent() {
        int bodyLength;
        int buffLength;
        ByteBuffer bb;
        if (body == null || body.length() == 0) {
            bodyLength = 0;
        } else {
            bodyLength = body.length();
        }
        if (buff == null || buff.length == 0) {
            buffLength = 0;
        } else {
            buffLength = buff.length;
        }
        bb = ByteBuffer.allocate(19 + bodyLength);
        bb.put(Api.encodeVersion1); //编码版本1     0
        bb.put(ByteUtil.int2Bytes(mainCmd));  //1-4   主指令
        bb.put(ByteUtil.int2Bytes(subCmd));   //5-8   子指令
        bb.put(ByteUtil.int2Bytes(bodyLength));  //9 -12位,数据长度
        bb.put(ByteUtil.int2Bytes(buffLength));  //12 -15位,数据长度
        if (bodyLength != 0) {
            bb.put(body.getBytes());
        }
        if (buffLength != 0) {
            bb.put(buff);
        }
        return bb.array();
    }
}
