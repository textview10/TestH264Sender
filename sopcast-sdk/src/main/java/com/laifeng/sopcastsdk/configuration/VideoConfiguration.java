package com.laifeng.sopcastsdk.configuration;

/**
 * @Title: VideoConfiguration
 * @Package com.laifeng.sopcastsdk.configuration
 * @Description:
 * @Author Jim
 * @Date 16/9/14
 * @Time 下午3:20
 * @Version
 */
public final class VideoConfiguration {
    // TODO modify by xu.wang
    //--------------------默认高清时的设置参数--------------------------------
    public static final int DEFAULT_HEIGHT = 640;
    public static final int DEFAULT_WIDTH = 360;
    public static final int DEFAULT_FPS = 15;
    public static final int DEFAULT_MAX_BPS = 1500;
    public static final int DEFAULT_MIN_BPS = 400;
    public static final int DEFAULT_IFI = 1;    //关键帧间隔时间 单位s
    public static final String DEFAULT_MIME = "video/avc";
    //--------------------默认标清时的设置参数--------------------------------
    public static final int SECOND_HEIGHT = 640;
    public static final int SECOND_WIDTH = 360;
    public static final int SECOND_FPS = 10;
    public static final int SECOND_MAX_BPS = 600;
    public static final int SECOND_MIN_BPS = 300;
    public static final int SECOND_IFI = 3;
    public static final String SECOND_MIME = "video/avc";

    public int height;
    public int width;
    public final int minBps;
    public int maxBps;
    public int fps;
    public int ifi;
    public final String mime;

    private VideoConfiguration(final Builder builder) {
        height = builder.height;
        width = builder.width;
        minBps = builder.minBps;
        maxBps = builder.maxBps;
        fps = builder.fps;
        ifi = builder.ifi;
        mime = builder.mime;
    }

    private VideoConfiguration(final SecondBuilder builder) {
        height = builder.height;
        width = builder.width;
        minBps = builder.minBps;
        maxBps = builder.maxBps;
        fps = builder.fps;
        ifi = builder.ifi;
        mime = builder.mime;
    }

    public static VideoConfiguration createDefault() {
        return new Builder().build();
    }


    public static class Builder {
        private int height = DEFAULT_HEIGHT;
        private int width = DEFAULT_WIDTH;
        private int minBps = DEFAULT_MIN_BPS;
        private int maxBps = DEFAULT_MAX_BPS;
        private int fps = DEFAULT_FPS;
        private int ifi = DEFAULT_IFI;
        private String mime = DEFAULT_MIME;

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setBps(int minBps, int maxBps) {
            this.minBps = minBps;
            this.maxBps = maxBps;
            return this;
        }

        public Builder setFps(int fps) {
            this.fps = fps;
            return this;
        }

        public Builder setIfi(int ifi) {
            this.ifi = ifi;
            return this;
        }

        public Builder setMime(String mime) {
            this.mime = mime;
            return this;
        }

        public VideoConfiguration build() {
            return new VideoConfiguration(this);
        }
    }

    public static class SecondBuilder {
        private int height = SECOND_HEIGHT;
        private int width = SECOND_WIDTH;
        private int minBps = SECOND_MIN_BPS;
        private int maxBps = SECOND_MAX_BPS;
        private int fps = SECOND_FPS;
        private int ifi = SECOND_IFI;
        private String mime = SECOND_MIME;

        public VideoConfiguration build() {
            return new VideoConfiguration(this);
        }
    }
}
