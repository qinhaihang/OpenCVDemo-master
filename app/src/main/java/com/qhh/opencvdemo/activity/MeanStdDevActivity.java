package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MeanStdDevActivity extends AppCompatActivity {

    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mean_std_dev);

        initView();
    }

    private void initView() {
        mImage = findViewById(R.id.image);
    }

    public void computer(View view) {
        Mat src = Imgcodecs.imread(Constants.testImage, Imgcodecs.IMREAD_COLOR);
        if(src.empty()){
            return;
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);

        MatOfDouble means = new MatOfDouble();
        MatOfDouble stddevs = new MatOfDouble();
        Core.meanStdDev(gray,means,stddevs);

        double[] meanArr = means.toArray();
        double[] stddevArr = stddevs.toArray();
        Log.i("qhh_opencv","mean = "+ meanArr[0]);
        Log.i("qhh_opencv","stddev = "+ stddevArr[0]);

        int width = gray.cols();
        int height = gray.rows();
        byte[] data = new byte[width * height];
        gray.get(0,0,data);
        int mean = (int)meanArr[0];
        int pv = 0;
        for (int i = 0; i < data.length; i++) {
            pv = data[i]&0xff;
            if(pv > mean){
                data[i] = (byte) 255;
            }else {
                data[i] = (byte) 0;
            }
        }
        gray.put(0,0,data);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(gray,bitmap);

        mImage.setImageBitmap(bitmap);
    }
}
