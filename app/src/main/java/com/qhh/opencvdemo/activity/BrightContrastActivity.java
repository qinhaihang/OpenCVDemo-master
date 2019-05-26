package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class BrightContrastActivity extends AppCompatActivity {

    private Mat mSrc;
    private Bitmap mDstBitmap;
    private ImageView mResult;

    private int bright;
    private int contrast;
    private ImageView mResult2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bright_contrast);

        initView();

        mSrc = Imgcodecs.imread(Constants.testImage);

    }

    private void initView() {
        mResult = findViewById(R.id.iv_result);
        mResult2 = findViewById(R.id.iv_result2);
    }

    public void bright(View view) {

        bright += 10;

        Mat dst1 = new Mat();
        Core.add(mSrc, new Scalar(bright, bright, bright), dst1);

        Mat result = new Mat();
        Imgproc.cvtColor(dst1,result,Imgproc.COLOR_BGR2BGRA);

        Bitmap mDstBitmap1 = Bitmap.createBitmap(mSrc.cols(), mSrc.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result,mDstBitmap1);

        mResult.setImageBitmap(mDstBitmap1);
    }

    public void contrast(View view) {

        contrast += 10;

        Mat dst1 = new Mat();
        Core.multiply(mSrc, new Scalar(contrast, contrast, contrast), dst1);

        Mat result = new Mat();
        Imgproc.cvtColor(dst1,result,Imgproc.COLOR_BGR2BGRA);

        Bitmap mDstBitmap2 = Bitmap.createBitmap(mSrc.cols(), mSrc.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result,mDstBitmap2);

        mResult2.setImageBitmap(mDstBitmap2);
    }
}
