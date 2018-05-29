package com.laifeng.sopcastsdk.stream.packer.tcp;

import android.media.MediaCodec;

import com.laifeng.sopcastsdk.stream.packer.AnnexbHelper;
import com.laifeng.sopcastsdk.stream.packer.Packer;

import java.nio.ByteBuffer;

/**
 * Created by xu.wang
 * Date on  2017/11/23 10:02:31.
 *
 * @Desc add
 */

public class TcpPacker implements Packer, AnnexbHelper.AnnexbNaluListener {
    private static final String TAG = "TcpPacker";
    public static final int HEADER = 0;
    public static final int METADATA = 1;
    public static final int FIRST_VIDEO = 2;
    public static final int AUDIO = 4;
    public static final int KEY_FRAME = 5;
    public static final int INTER_FRAME = 6;

    private OnPacketListener packetListener;
    private boolean isHeaderWrite;
    private boolean isKeyFrameWrite;

    private int mAudioSampleRate, mAudioSampleSize;
    private boolean mIsStereo;
    private boolean mSendAudio = false;

    private AnnexbHelper mAnnexbHelper;


    public TcpPacker() {
        mAnnexbHelper = new AnnexbHelper();
    }

    public void setPacketListener(OnPacketListener listener) {
        packetListener = listener;
    }


    @Override
    public void start() {
        mAnnexbHelper.setAnnexbNaluListener(this);
    }

    @Override
    public void onVideoData(ByteBuffer bb, MediaCodec.BufferInfo bi) {
        mAnnexbHelper.analyseVideoDataonlyH264(bb, bi);
    }

    @Override
    public void onAudioData(ByteBuffer bb, MediaCodec.BufferInfo bi) {
        if (packetListener == null ) {
            return;
        }
        if (!mSendAudio) {
            return;
        }
        bb.position(bi.offset);
        bb.limit(bi.offset + bi.size);
        byte[] audio = new byte[bi.size];
        bb.get(audio);
        //一般第一帧都是2个字节
        int length = 7 + audio.length;
        ByteBuffer tempBb = ByteBuffer.allocate(length + 4);
        tempBb.put(header);
        tempBb.put(getADTSHeader(length));
        tempBb.put(audio);
        packetListener.onPacket(tempBb.array(), AUDIO);
    }

    @Override
    public void stop() {
        isHeaderWrite = false;
        isKeyFrameWrite = false;
        mAnnexbHelper.stop();
    }

    private byte[] mSpsPps;
    private byte[] header = {0x00, 0x00, 0x00, 0x01};   //H264的头文件

    @Override
    public void onSpsPps(byte[] sps, byte[] pps) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(sps.length + 4);
        byteBuffer.put(header);
        byteBuffer.put(sps);
        mSpsPps = byteBuffer.array();

        packetListener.onPacket(mSpsPps, FIRST_VIDEO);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(pps.length + 4);
        byteBuffer1.put(header);
        byteBuffer1.put(pps);
        packetListener.onPacket(byteBuffer1.array(), FIRST_VIDEO);
        isHeaderWrite = true;
    }


    @Override
    public void onVideo(byte[] video, boolean isKeyFrame) {
        if (packetListener == null || !isHeaderWrite) {
            return;
        }
        int packetType = INTER_FRAME;
        if (isKeyFrame) {
            isKeyFrameWrite = true;
            packetType = KEY_FRAME;
        }
        //确保第一帧是关键帧，避免一开始出现灰色模糊界面
        if (!isKeyFrameWrite) {
            return;
        }
        ByteBuffer bb;
        if (isKeyFrame) {
            bb = ByteBuffer.allocate(video.length);
            bb.put(video);
        } else {
            bb = ByteBuffer.allocate(video.length);
            bb.put(video);
        }
        packetListener.onPacket(bb.array(), packetType);
    }

    public void initAudioParams(int sampleRate, int sampleSize, boolean isStereo) {
        mAudioSampleRate = sampleRate;
        mAudioSampleSize = sampleSize;
        mIsStereo = isStereo;
    }

    public void setSendAudio(boolean sendAudio) {
        this.mSendAudio = sendAudio;
    }

    /**
     * 给编码出的aac裸流添加adts头字段
     *
     * @param packetLen
     */
    private byte[] getADTSHeader(int packetLen) {
        byte[] packet = new byte[7];
        int profile = 2;  //AAC LC
        int freqIdx = 4;  //16.0KHz
        int chanCfg = 2;  //CPE 声道数
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
        return packet;
    }
}
