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
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FilterActivity extends AppCompatActivity {

    private ImageView iv_midfilter;
    private ImageView iv_minfilter;
    private ImageView iv_maxfilter;
    private ImageView iv_bilateral;
    private ImageView iv_shiftfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_filter);
        iv_midfilter = findViewById(R.id.iv_midfilter);
        iv_minfilter = findViewById(R.id.iv_minfilter);
        iv_maxfilter = findViewById(R.id.iv_maxfilter);
        iv_bilateral = findViewById(R.id.iv_bilateral);
        iv_shiftfilter = findViewById(R.id.iv_shiftfilter);
    }

    public void midFilter(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        Mat dst = new Mat();
        //中值滤波,ksize 一般为 3 和 5 的卷积核
        Imgproc.medianBlur(src,dst,5);

        Bitmap bitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat cvResult = new Mat();

        Imgproc.cvtColor(dst,cvResult,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(cvResult,bitmap);

        iv_midfilter.setImageBitmap(bitmap);

        src.release();
        dst.release();
        cvResult.release();
    }

    public void minFilter(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        Mat dst = new Mat();
        //获取滤波用的 kernel
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        Imgproc.erode(src,dst,kernel);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_minfilter.setImageBitmap(bitmap);

        src.release();
        dst.release();
    }

    public void maxFilter(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        Mat dst = new Mat();
        //获取滤波用的 kernel
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        Imgproc.dilate(src,dst,kernel);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_maxfilter.setImageBitmap(bitmap);

        src.release();
        dst.release();
    }

    public void bilateralFilter(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        Mat dst = new Mat();

        Imgproc.bilateralFilter(src,dst,0,150,25);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_bilateral.setImageBitmap(bitmap);

        src.release();
        dst.release();
    }

    public void shiftFilter(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage);
        if(src.empty()){
            return;
        }

        Mat dst = new Mat();

        Imgproc.pyrMeanShiftFiltering(src,dst,10,50);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_shiftfilter.setImageBitmap(bitmap);

        src.release();
        dst.release();
    }
}
