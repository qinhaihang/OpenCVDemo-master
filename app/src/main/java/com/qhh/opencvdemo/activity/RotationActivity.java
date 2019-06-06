package com.qhh.opencvdemo.activity;

import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;

import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.camera.CameraConfig;
import com.qhh.opencvdemo.camera.CameraUtil;

public class RotationActivity extends AppCompatActivity {

    private TextureView mTexture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        mTexture = findViewById(R.id.texture);
        mTexture.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera(surface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                CameraUtil.getInstance().releaseCameraIfNeed();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    private void openCamera(SurfaceTexture surface) {
        CameraConfig build = new CameraConfig.Builder()
                .facingBackCamera()
                .facingFrontCamera()
                .orientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 90 : 0)
                .support720p()
                .support480p()
                .build();

        CameraUtil.getInstance().setPreviewCallback(new CameraUtil.PreviewCallback() {

            @Override
            public void onSize(int width, int height) {
                Log.i("qhh", "width = " + width + ", height = " + height);
            }

            @Override
            public void onPreviewFrame(byte[] data) {
                Log.i("qhh", "data length = " + data.length);
            }
        });
        CameraUtil.getInstance().openCamera(build, surface);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
