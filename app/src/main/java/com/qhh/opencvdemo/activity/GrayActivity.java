package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gray);
    }

    public void process(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        Mat dir= new Mat();
        Mat source = new Mat();
        Utils.bitmapToMat(bitmap, source);
        Imgproc.cvtColor(source,dir,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dir,bitmap);
        source.release();
        dir.release();
    }
}
