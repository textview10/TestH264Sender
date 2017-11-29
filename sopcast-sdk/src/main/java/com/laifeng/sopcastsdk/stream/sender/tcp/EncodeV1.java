package com.laifeng.sopcastsdk.stream.sender.tcp;

import java.nio.ByteBuffer;

/**
 * Created by xu.wang
 * Date on  2017/11/23 18:50:09.
 *
 * @Desc
 */

public class EncodeV1 {
    private byte[] buff;    //要发送的内容

    public EncodeV1(byte[] buff) {
        this.buff = buff;
    }

    public byte[] buildSendContent() {
        if (buff == null || buff.length == 0) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.allocate(4 + buff.length);
        bb.put(ByteUtil.int2Bytes(buff.length));
        bb.put(buff);
        return bb.array();
    }
}
