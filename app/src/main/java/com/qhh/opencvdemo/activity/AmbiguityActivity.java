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
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class AmbiguityActivity extends AppCompatActivity {

    private ImageView iv_mean;
    private Bitmap mMeanBitmap;
    private ImageView iv_gauss;
    private Bitmap mGaussBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiguity);
        iv_mean = findViewById(R.id.iv_mean);
        iv_gauss = findViewById(R.id.iv_gauss);
    }

    public void meanValue(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage,Imgcodecs.IMREAD_COLOR);

        if(src.empty()){
            return;
        }

        Mat dst = new Mat();

        Imgproc.blur(src,dst,new Size(10,10),new Point(-1,-1), Core.BORDER_DEFAULT);

        mMeanBitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst,result,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, mMeanBitmap);

        iv_mean.setImageBitmap(mMeanBitmap);

        src.release();
        dst.release();
        result.release();
    }

    public void gauss(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage,Imgcodecs.IMREAD_COLOR);
        if(src.empty()){
            return;
        }
        Mat dst = new Mat();
        Imgproc.GaussianBlur(src,dst,new Size(0,0),10,0);

        mGaussBitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst,result,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result,mGaussBitmap);

        iv_gauss.setImageBitmap(mGaussBitmap);

        src.release();
        dst.release();
        result.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMeanBitmap != null){
            mMeanBitmap.recycle();
        }
    }

}
