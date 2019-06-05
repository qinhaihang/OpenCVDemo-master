package com.qhh.opencvdemo.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;

import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.manager.CameraManager;

import java.io.IOException;

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
                try {
                    CameraManager.getInstance().open(0,mTexture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                CameraManager.getInstance().releaseCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        CameraManager.getInstance().setPreviewCallback(new CameraManager.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data) {
                Log.d("qhh_opencv","data size " + data.length);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraManager.getInstance().releaseCamera();
    }
}
