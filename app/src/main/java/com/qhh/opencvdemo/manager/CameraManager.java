package com.qhh.opencvdemo.manager;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.view.TextureView;

import java.io.IOException;
import java.util.List;

/**
 * @author qinhaihang
 * @version $Rev$
 * @time 19-6-5 下午11:09
 * @des
 * @packgename com.qhh.opencvdemo.manager
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class CameraManager {

    @Nullable
    private Camera.CameraInfo mFrontCameraInfo = null;
    private int mFrontCameraId = -1;

    @Nullable
    private Camera.CameraInfo mBackCameraInfo = null;
    private int mBackCameraId = -1;
    private Camera mCamera;

    private PreviewCallback mPreviewCallback;

    private static class SingletonHolder{
        private static final CameraManager INSTANCE = new CameraManager();
    }

    public static CameraManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void initCameraInfo(){
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        for (int cameraId = 0; cameraId < numberOfCameras; cameraId++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 后置摄像头信息
                mBackCameraId = cameraId;
                mBackCameraInfo = cameraInfo;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                // 前置摄像头信息
                mFrontCameraId = cameraId;
                mFrontCameraInfo = cameraInfo;
            }
        }
    }

    public Camera.Parameters initParameters(Camera camera,int width,int height){

        try {

            Camera.Parameters parameters = camera.getParameters();

            parameters.setPreviewFormat(ImageFormat.NV21);

            Camera.Size bestSize = getBestSize(width, height, parameters.getSupportedPreviewSizes());

            parameters.setPreviewSize(bestSize.width,bestSize.height);

            return parameters;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private Camera.Size getBestSize(int targetWidth, int targetHeight, List<Camera.Size> sizeList){
        Camera.Size bestSize = null;

        double targetRatio = targetHeight / targetWidth; //宽高比
        double minDiff = targetRatio;

        for(Camera.Size size : sizeList){
            int width = size.width;
            int height = size.height;

            if(width == targetWidth && height == targetHeight){
                bestSize = size;
                break;
            }

            double surpportRatio = width / height;

            if(Math.abs(surpportRatio - targetRatio) < minDiff){
                minDiff = Math.abs(surpportRatio - targetRatio);
                bestSize = size;
            }

        }

        return bestSize;
    }

    public void open(int id, TextureView surface) throws IOException {

        if(mCamera != null){
            releaseCamera();
        }

        mCamera = Camera.open(id);

        if(mCamera != null){

            mCamera.setPreviewTexture(surface.getSurfaceTexture());

            Camera.Parameters parameters = initParameters(mCamera, surface.getWidth(), surface.getHeight());

            mCamera.setParameters(parameters);

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if(mPreviewCallback != null){
                        mPreviewCallback.onPreviewFrame(data);
                    }
                }
            });

        }

    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    public void releaseCamera() {
        if(mCamera != null){
            mCamera.release();
            mCamera  = null;
        }
    }

    public interface PreviewCallback{
        void onPreviewFrame(byte[] data);
    }

}
