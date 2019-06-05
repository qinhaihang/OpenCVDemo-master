package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class BGR2ARGBActivity extends AppCompatActivity {

    private ImageView mIvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgr2_argb);
        mIvImage = findViewById(R.id.iv_image);
    }

    public void read(View view) {

        Mat src = Imgcodecs.imread(Constants.testImage, Imgcodecs.IMREAD_COLOR);

        int width = src.cols();
        int heigth = src.rows();

        Bitmap bitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);

        Mat result = new Mat();
        Imgproc.cvtColor(src,result,Imgproc.COLOR_BGR2RGBA); //转成Bitmap必须使用

        Utils.matToBitmap(result,bitmap);

        mIvImage.setImageBitmap(bitmap);

        src.release();

    }
}
