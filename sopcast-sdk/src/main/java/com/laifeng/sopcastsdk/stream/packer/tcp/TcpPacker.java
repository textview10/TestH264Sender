package com.laifeng.sopcastsdk.stream.packer.tcp;

import android.media.MediaCodec;
import android.util.Log;

import com.laifeng.sopcastsdk.stream.packer.AnnexbHelper;
import com.laifeng.sopcastsdk.stream.packer.Packer;
import com.laifeng.sopcastsdk.stream.packer.flv.FlvPackerHelper;

import java.nio.ByteBuffer;

import static com.laifeng.sopcastsdk.stream.packer.flv.FlvPackerHelper.FLV_TAG_HEADER_SIZE;
import static com.laifeng.sopcastsdk.stream.packer.flv.FlvPackerHelper.PRE_SIZE;
import static com.laifeng.sopcastsdk.stream.packer.flv.FlvPackerHelper.VIDEO_HEADER_SIZE;
import static com.laifeng.sopcastsdk.stream.packer.flv.FlvPackerHelper.VIDEO_SPECIFIC_CONFIG_EXTEND_SIZE;

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
        if (packetListener == null || !isHeaderWrite || !isKeyFrameWrite) {
            return;
        }
        if (!mSendAudio) {
            return;
        }
        bb.position(bi.offset);
        bb.limit(bi.offset + bi.size);
        byte[] audio = new byte[bi.size];
        bb.get(audio);
        ByteBuffer tempBb = ByteBuffer.allocate(audio.length + 4);
        tempBb.put(header);
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(sps.length + pps.length + 8);
        byteBuffer.put(header);
        byteBuffer.put(sps);
        byteBuffer.put(header);
        byteBuffer.put(pps);
        mSpsPps = byteBuffer.array();
        packetListener.onSpsPps(mSpsPps);   //add by xu.wang onSpsPps()回调没有参与发送逻辑,想要发送这些信息需要回调onPacket();
        packetListener.onPacket(mSpsPps, FIRST_VIDEO);
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

}
