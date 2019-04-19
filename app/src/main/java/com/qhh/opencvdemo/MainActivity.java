package com.qhh.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage = findViewById(R.id.iv_image);
        initLoadOpenCV();
    }

    public void process(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        Mat dir= new Mat();
        Mat source = new Mat();
        Utils.bitmapToMat(bitmap, source);
        Imgproc.cvtColor(source,dir,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dir,bitmap);
        mImage.setImageBitmap(bitmap);
        source.release();
        dir.release();
    }

    private void initLoadOpenCV(){
        boolean isDebug = OpenCVLoader.initDebug();
        if(isDebug){
            Log.i("qhh","init openCV success!!");
        }else{
            Log.e("qhh","init openCV failure!!");
        }
    }
}
