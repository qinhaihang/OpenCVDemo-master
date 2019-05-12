package com.qhh.opencvdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.utils.FileUtils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class SaveMat2SDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_mat2_sd);
        FileUtils.checkFileExists(Constants.TEST_IMAGE);
    }

    public void save(View view) {
        Mat image = new Mat(500, 500, CvType.CV_8UC3);
        image.setTo(new Scalar(127,127,127));
        String imageName = System.currentTimeMillis() + "_test.jpg";
        File sdFile = new File(Constants.TEST_IMAGE, imageName);
        Imgcodecs.imwrite(sdFile.getAbsolutePath(),image);

        image.release();
    }
}
