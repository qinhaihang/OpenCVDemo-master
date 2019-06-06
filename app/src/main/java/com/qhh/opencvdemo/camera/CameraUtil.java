package com.qhh.opencvdemo.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

public class CameraUtil {

    private static CameraUtil sInstance = null;

    private Camera mCamera = null;

    private CameraConfig mCameraConfig = null;

    private SurfaceTexture mSurfaceTexture;

    private int mPreviewSize = -1;

    private PreviewCallback mPreviewCallback;

    public static CameraUtil getInstance() {
        if (sInstance == null) {
            synchronized (CameraUtil.class) {
                if (sInstance == null) {
                    sInstance = new CameraUtil();
                }
            }
        }
        return sInstance;
    }


    public synchronized void openCamera(CameraConfig cameraConfig, SurfaceTexture surfaceTexture) {
        if (cameraConfig == null) {
            return;
        }
        try {
            releaseCameraIfNeed();
            mCamera = getCamera(cameraConfig.getSupportCameraType());
            mCamera.setPreviewTexture(surfaceTexture);
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();

            ArrayList<Integer> supportPreviewSize = cameraConfig.getSupportPreviewSize();
            if (previewSizes != null && previewSizes.size() > 0) {
                label_id:
                for (Integer integer : supportPreviewSize) {
                    for (Camera.Size previewSize : previewSizes) {
                        boolean res = CameraConfig.equalsSize(previewSize, integer);
                        if (res) {
                            mPreviewSize = integer;
                            break label_id;
                        }
                    }
                }
            }


            mCamera.setDisplayOrientation(cameraConfig.getOrientation());

            CameraConfig.Size size = CameraConfig.getSize(mPreviewSize);

            if (mPreviewCallback != null) {
                mPreviewCallback.onSize(size.getWidth(),size.getHeight());
            }

            byte[] tmpBuffer1 = new byte[size.getWidth()
                    * size.getHeight() * 3 / 2];
            byte[] tmpBuffer2 = new byte[size.getWidth()
                    * size.getHeight()  * 3 / 2];
            byte[] tmpBuffer3 = new byte[size.getWidth()
                    * size.getHeight()  * 3 / 2];

            mCamera.addCallbackBuffer(tmpBuffer1);
            mCamera.addCallbackBuffer(tmpBuffer2);
            mCamera.addCallbackBuffer(tmpBuffer3);

            params.setPreviewSize(size.getWidth(),
                    size.getHeight());

            mCamera.setParameters(params);
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (mPreviewCallback != null) {
                        mPreviewCallback.onPreviewFrame(data);
                    }
                    mCamera.addCallbackBuffer(data);
                }
            });
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Camera getCamera(ArrayList<Integer> supportCameraType) {

        if (supportCameraType == null || supportCameraType.size() == 0) {
            return newCamera(1);
        }

        Camera camera = newCamera(supportCameraType.get(0));
        if (camera == null) {
            if (supportCameraType.size() <= 1) {
                return null;
            }
            camera = newCamera(supportCameraType.get(1));
            if (camera == null) {
                camera = Camera.open();
            }
        }
        return camera;
    }

    private Camera newCamera(int cameraId) {
        try {
            Camera camera = Camera.open(cameraId);
            return camera;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void releaseCameraIfNeed() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public synchronized void setPreviewCallback(PreviewCallback callback) {
        this.mPreviewCallback = callback;
    }

    public interface PreviewCallback {
        void onSize(int width,int height);
        void onPreviewFrame(byte[] data);
    }

}
