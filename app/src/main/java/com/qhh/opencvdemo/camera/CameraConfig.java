package com.qhh.opencvdemo.camera;

import android.hardware.Camera;

import java.util.ArrayList;

public class CameraConfig {

    private final int orientation;
    /**
     * 预览宽高
     */
    private int mPreviewWidth;
    private int mPreviewHeight;

    /**
     * 要选择的分辨率
     */
    private ArrayList<Integer> mSupportPreviewSize = new ArrayList<>(4);

    /**
     * 要开哪个相机
     */
    private ArrayList<Integer> mSupportCameraType = new ArrayList<>(2);

    /**
     * 支持的分辨率
     */
    public static final int CAMERA_SUPPORT_640_480 = 101;
    public static final int CAMERA_SUPPORT_800_600 = 102;
    public static final int CAMERA_SUPPORT_1280_720 = 103;
    public static final int CAMERA_SUPPORT_1920_1080 = 104;

    /**
     * 支持前置后置
     */
    public static final int CAMERA_FACING_BACK = 0;

    public static final int CAMERA_FACING_FRONT = 1;

    public static final int CAMERA_FACING_USB = 2;


    public CameraConfig(int previewWidth, int previewHeight
            , ArrayList<Integer> supportCameraType
            , ArrayList<Integer> supportPreviewSize, int orientation) {

        mSupportCameraType.addAll(supportCameraType);
        mSupportPreviewSize.addAll(supportPreviewSize);

        if (previewWidth != 0) {
            mPreviewWidth = previewWidth;
        }
        if (previewHeight != 0) {
            mPreviewHeight = previewHeight;
        }

        this.orientation = orientation;
    }

    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    public ArrayList<Integer> getSupportPreviewSize() {
        return mSupportPreviewSize;
    }

    public ArrayList<Integer> getSupportCameraType() {
        return mSupportCameraType;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public static class Builder {

        private int mPreviewWidth;
        private int mPreviewHeight;

        private int orientation;

        private ArrayList<Integer> mSupportPreviewSize = new ArrayList<>(4);

        private ArrayList<Integer> mSupportCameraType = new ArrayList<>(2);


        public Builder previewWidth(int previewWidth) {
            mPreviewWidth = previewWidth;
            return this;
        }

        public Builder previewHeight(int previewHeight) {
            mPreviewHeight = previewHeight;
            return this;
        }

        public Builder facingUSBCamera() {
            mSupportCameraType.add(CAMERA_FACING_USB);
            return this;
        }

        public Builder facingFrontCamera() {
            mSupportCameraType.add(CAMERA_FACING_FRONT);
            return this;
        }

        public Builder facingBackCamera() {
            mSupportCameraType.add(CAMERA_FACING_BACK);
            return this;
        }

        public Builder support480p() {
            mSupportPreviewSize.add(CAMERA_SUPPORT_640_480);
            return this;
        }

        public Builder support720p() {
            mSupportPreviewSize.add(CAMERA_SUPPORT_1280_720);
            return this;
        }

        public Builder support1080p() {
            mSupportPreviewSize.add(CAMERA_SUPPORT_1920_1080);
            return this;
        }

        public Builder support600p() {
            mSupportPreviewSize.add(CAMERA_SUPPORT_800_600);
            return this;
        }

        public Builder orientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public CameraConfig build() {
            return new CameraConfig(mPreviewWidth, mPreviewHeight, mSupportCameraType, mSupportPreviewSize, orientation);
        }

    }


    public static boolean equalsSize(Camera.Size size, int previewSize) {
        switch (previewSize) {
            case CAMERA_SUPPORT_640_480:
                return size.width == 640 && size.height == 480;
            case CAMERA_SUPPORT_800_600:
                return size.width == 800 && size.height == 600;
            case CAMERA_SUPPORT_1280_720:
                return size.width == 1280 && size.height == 720;
            case CAMERA_SUPPORT_1920_1080:
                return size.width == 1920 && size.height == 1080;
            default:
                return false;
        }
    }


    public static Size getSize(int size) {
        switch (size) {
            case CAMERA_SUPPORT_640_480:
                return new Size(640, 480);
            case CAMERA_SUPPORT_800_600:
                return new Size(800, 600);
            case CAMERA_SUPPORT_1280_720:
                return new Size(1280, 720);
            case CAMERA_SUPPORT_1920_1080:
                return new Size(1920, 1080);
            default:
                return new Size(640, 480);
        }
    }

    public static class Size {
        private int width;
        private int height;

        public Size() {
        }

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}
