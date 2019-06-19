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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class BaseFeatureActivity extends AppCompatActivity {

    private ImageView iv_sobel;
    private ImageView iv_scharr;
    private ImageView iv_laplacian;
    private Mat src;
    private Mat sobel;
    private Mat scharr;
    private Mat laplacian;
    private ImageView iv_canny;
    private ImageView iv_houghlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_feature);
        initView();
        src = Imgcodecs.imread(Constants.testImage);
    }

    private void initView() {
        iv_sobel = findViewById(R.id.iv_sobel);
        iv_scharr = findViewById(R.id.iv_scharr);
        iv_laplacian = findViewById(R.id.iv_laplacian);
        iv_canny = findViewById(R.id.iv_canny);
        iv_houghlines = findViewById(R.id.iv_houghlines);
    }

    public void click(View view) {
        switch(view.getId()){
            case R.id.btn_sobel:
                sobelGradient();
                break;
            case R.id.btn_scharr:
                scharrGradient();
                break;
            case R.id.btn_laplacian:
                laplacian();
                break;
            case R.id.btn_canny:
                canny();
                break;
            case R.id.btn_houghlines:
                houghLines();
                break;
            default:
                break;
        }
    }

    private void houghLines() {
        Mat src = Imgcodecs.imread(Constants.barImage);

        Mat edges = new Mat();
        Imgproc.Canny(src,edges,50,150,3,true);

        Mat lines = new Mat();

        Imgproc.HoughLines(edges,lines,1,Math.PI / 180,100);

        Mat out = Mat.zeros(src.size(),src.type());

        float[] data = new float[2];
        for (int i = 0; i < lines.rows(); i++) {
            lines.get(i,0,data);
            float rho = data[0] , theta = data[1];
            double a = Math.cos(theta), b= Math.sin(theta);
            double x0 = a*rho , y0 = b*rho;

            Point pt1 = new Point();
            Point pt2 = new Point();
            pt1.x = Math.round(x0 + 1000*(-b));
            pt1.y = Math.round(y0 + 1000*(a));
            pt2.x = Math.round(x0 - 1000*(-b));
            pt2.y = Math.round(y0 + 1000*(a));

            Imgproc.line(out,pt1,pt2,new Scalar(0,0,255),3,Imgproc.LINE_AA,0);
        }

        Mat dst = new Mat();
        out.copyTo(dst);
        out.release();
        edges.release();

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(dst);
        iv_houghlines.setImageBitmap(bitmap);
    }

    private void canny() {

        Mat edges = new Mat();
        Mat dst = new Mat();

        Imgproc.Canny(src,edges,50,255,3,false);
//        Core.bitwise_and(src,src,dst,edges);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(edges);
        iv_canny.setImageBitmap(bitmap);
    }

    private void laplacian() {

        laplacian = new Mat();
        Imgproc.Laplacian(src, laplacian,CvType.CV_32F,3,1,0);
        Core.convertScaleAbs(laplacian, laplacian);
        Bitmap bitmap = ImageCvUtils.mat2Bitmap(laplacian);
        iv_laplacian.setImageBitmap(bitmap);
    }

    private void scharrGradient() {

        Mat gradx = new Mat();
        Imgproc.Scharr(src,gradx, CvType.CV_32F,1,0);
        Core.convertScaleAbs(gradx,gradx);

        Mat grady = new Mat();
        Imgproc.Scharr(src,grady, CvType.CV_32F,0,1);
        Core.convertScaleAbs(grady,grady);

        scharr = new Mat();
        Core.addWeighted(gradx, 0.5, grady, 0.5, 0, scharr);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(scharr);
        iv_scharr.setImageBitmap(bitmap);
        gradx.release();
        grady.release();
    }

    private void sobelGradient(){

        Mat gradx = new Mat();
        Imgproc.Sobel(src,gradx, CvType.CV_32F,1,0);
        Core.convertScaleAbs(gradx,gradx);

        Mat grady = new Mat();
        Imgproc.Sobel(src,grady, CvType.CV_32F,0,1);
        Core.convertScaleAbs(grady,grady);

        sobel = new Mat();
        Core.addWeighted(gradx, 0.5, grady, 0.5, 0, sobel);

        Bitmap bitmap = ImageCvUtils.mat2Bitmap(sobel);
        iv_sobel.setImageBitmap(bitmap);
        gradx.release();
        grady.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        src.release();
        sobel.release();
        scharr.release();
        laplacian.release();
    }
}
