package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.utils.ImageCvUtils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class BaseFeatureActivity extends AppCompatActivity {

    private ImageView iv_gradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_feature);
        initView();
    }

    private void initView() {
        iv_gradient = findViewById(R.id.iv_gradient);
    }

    public void click(View view) {
        switch(view.getId()){
            case R.id.btn_gradient:
                sobleGradient();
                break;
            default:
                break;
        }
    }

    private void sobleGradient(){
        Mat src = Imgcodecs.imread(Constants.testImage);

        Mat gradx = new Mat();
        Imgproc.Sobel(src,gradx, CvType.CV_32F,1,0);
        Core.convertScaleAbs(gradx,gradx);

        Mat grady = new Mat();
        Imgproc.Sobel(src,grady, CvType.CV_32F,0,1);
        Core.convertScaleAbs(grady,grady);

        Mat dst = new Mat();
        Core.addWeighted(gradx, 0.5, grady, 0.5, 0, dst);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_gradient.setImageBitmap(bitmap);

    }

}
