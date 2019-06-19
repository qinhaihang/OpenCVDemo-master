package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.utils.ImageCvUtils;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 自定义滤波，主要通过方法 filter2D
 */
public class CustomFilterActivity extends AppCompatActivity {

    private ImageView iv_blurry;
    private ImageView iv_sharpen;
    private ImageView iv_gradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_filter);
        iv_blurry = findViewById(R.id.iv_blurry);
        iv_sharpen = findViewById(R.id.iv_sharpen);
        iv_gradient = findViewById(R.id.iv_sobel);

    }

    public void blurry(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        //自定义均值卷积核
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        float[] kernelArr = {
                1/9f,1/9f,1/9f,
                1/9f,1/9f,1/9f,
                1/9f,1/9f,1/9f
        };
        kernel.put(0,0,kernelArr);

        Mat blurry = new Mat();
        Imgproc.filter2D(src,blurry,-1,kernel);
        kernel.release();

        Mat dst = new Mat();
        Imgproc.cvtColor(blurry,dst,Imgproc.COLOR_BGR2RGBA);
        blurry.release();

        Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bitmap);
        dst.release();
        iv_blurry.setImageBitmap(bitmap);


        src.release();
    }

    public void sharpen(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        //自定义均值卷积核
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        float[] kernelArr = {
                -1,-1,-1,
                -1,9,-1,
                -1,-1,-1
        };
//        float[] kernelArr = {
//                0,-1,0,
//                -1,5,-1,
//                0,-1,0
//        };
        kernel.put(0,0,kernelArr);

        Mat blurry = new Mat();
        Imgproc.filter2D(src,blurry,-1,kernel);
        kernel.release();

        Mat dst = new Mat();
        Imgproc.cvtColor(blurry,dst,Imgproc.COLOR_BGR2RGBA);
        blurry.release();

        Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bitmap);
        dst.release();
        iv_sharpen.setImageBitmap(bitmap);


        src.release();
    }

    //没有出现效果
    public void gradient(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }
        Mat kx = new Mat(3, 3, CvType.CV_32FC1);
        Mat ky = new Mat(3, 3, CvType.CV_32FC1);

        float[] x = new float[]{
                -1,0,
                0,1
        };
        kx.put(0,0,x);

        float[] y = new float[]{
                0,1,
                -1,0
        };
        ky.put(0,0,y);
        Mat gradient = new Mat();
//        Imgproc.filter2D(src,gradient,-1,kx);
        Imgproc.filter2D(src,gradient,-1,ky);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(gradient);
        iv_gradient.setImageBitmap(bitmap);
    }
}
