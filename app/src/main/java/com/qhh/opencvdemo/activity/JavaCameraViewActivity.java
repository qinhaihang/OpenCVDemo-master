package com.qhh.opencvdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.qhh.opencvdemo.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

public class JavaCameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private JavaCameraView cv_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_java_camera_view);
        cv_camera = findViewById(R.id.cv_camera);
        cv_camera.setVisibility(View.VISIBLE);
        cv_camera.setCvCameraViewListener(this);
        cv_camera.setCameraIndex(0);
        if(cv_camera != null){
            cv_camera.disableView();
        }
        cv_camera.enableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        return null;
    }
}
