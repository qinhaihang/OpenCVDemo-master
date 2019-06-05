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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiguity);
        iv_mean = findViewById(R.id.iv_mean);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMeanBitmap != null){
            mMeanBitmap.recycle();
        }
    }
}
