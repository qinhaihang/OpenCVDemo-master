package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic);
        initView();
    }

    private void initView() {
        ImageView src1 = findViewById(R.id.iv_src1);
        ImageView src2 = findViewById(R.id.iv_src2);
        ImageView src3 = findViewById(R.id.iv_src3);

        Mat srcMat1 = Imgcodecs.imread(Constants.testImage);
        if(srcMat1.empty()){
            return;
        }

        int width = srcMat1.width();
        int height = srcMat1.height();
        int type = srcMat1.type();

        Mat src1Result = new Mat();
        Imgproc.cvtColor(srcMat1,src1Result, Imgproc.COLOR_BGR2RGBA);
        Bitmap srcBitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src1Result,srcBitmap1);
        src1.setImageBitmap(srcBitmap1);

        Mat srcMat2 = new Mat(height, width, type);
        int cx = srcMat1.cols() - 100;
        int cy = 60;
        Imgproc.circle(srcMat2,new Point(cx,cy),60,new Scalar(90,95,234),-1,8,0);

        Bitmap srcBitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Mat src2Result = new Mat();
        Imgproc.cvtColor(srcMat2,src2Result,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(src2Result,srcBitmap2);
        src2.setImageBitmap(srcBitmap2);

        Mat dst = new Mat();
        Core.add(srcMat1,srcMat2,dst);
        Bitmap dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Mat dstResult = new Mat();
        Imgproc.cvtColor(dst,dstResult,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dstResult,dstBitmap);
        src3.setImageBitmap(dstBitmap);


        srcMat1.release();
        src1Result.release();
        srcMat2.release();
        src2Result.release();
        dst.release();
        dstResult.release();
    }
}
