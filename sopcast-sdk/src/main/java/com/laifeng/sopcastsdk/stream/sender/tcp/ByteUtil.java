package com.laifeng.sopcastsdk.stream.sender.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xu.wang
 * Date on 2016/11/11 15:36
 */
public class ByteUtil {
    /**
     * 将int转为长度为4的byte数组
     *
     * @param length
     * @return
     */
    public static byte[] int2Bytes(int length) {
        byte[] result = new byte[4];
        result[0] = (byte) length;
        result[1] = (byte) (length >> 8);
        result[2] = (byte) (length >> 16);
        result[3] = (byte) (length >> 24);
        return result;
    }
}
