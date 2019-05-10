package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.baselib.image.CaptureHelper;
import com.qhh.baselib.image.callback.IRequestCaptureCallback;
import com.qhh.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.nio.ByteBuffer;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class GrayActivity extends AppCompatActivity {

    private ImageView iv_original;
    private ImageView iv_gray;
    private File saveFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gray);
        initView();
    }

    private void initView() {
        iv_original = findViewById(R.id.iv_original);
        iv_gray = findViewById(R.id.iv_gray);
    }

    public void process(View view) {

//        if(saveFile.exists()){
//            Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
            Mat dir= new Mat();
            Mat source = new Mat();
            Utils.bitmapToMat(bitmap, source);
            cvtColor(source,dir,Imgproc.COLOR_BGRA2GRAY);
            Utils.matToBitmap(dir,bitmap);
            iv_gray.setImageBitmap(bitmap);
            source.release();
            dir.release();
//        }
    }

    public void takePhotos(View view) {
        CaptureHelper.getInstance()
                .init(this)
                .setIRequestCaptureCallback(new IRequestCaptureCallback() {
                    @Override
                    public void error(int resultCode) {

                    }

                    @Override
                    public void success(int resultCode, final String path) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        iv_original.setImageBitmap(bitmap);
                    }
                })
                .requestCapture(saveFile.getAbsolutePath());

    }

    private Boolean YV12ToBGR24_OpenCV(byte[] pYUV,byte[] pBGR24,int width,int height){

        if (width < 1 || height < 1 || pYUV == null || pBGR24 == null)
            return false;

        ByteBuffer bgr = ByteBuffer.allocate(width * height);
        Mat dst = new Mat(height, width, CV_8UC3, bgr);

        ByteBuffer yuv = ByteBuffer.allocate(width * height);
        Mat src = new Mat(height + height / 2, width, CV_8UC1, yuv);
//        cvtColor(src,dst,CV_YUV2BGR_YV12);
        cvtColor(src,dst,Imgproc.COLOR_YUV2BGR_YV12);
        return true;
    }

    public Bitmap YV12ToBGR24_OpenCV(byte[] pYUV,int width,int height){

        if (width < 1 || height < 1 || pYUV == null)
            return null;

        ByteBuffer bgr = ByteBuffer.allocate(width * height);
        Mat dst = new Mat(height, width, CV_8UC3, bgr);

        ByteBuffer yuv = ByteBuffer.allocate(width * height);
        Mat src = new Mat(height + height / 2, width, CV_8UC1, yuv);
        //cvtColor(src,dst,CV_YUV2BGR_YV12);
        cvtColor(src,dst, Imgproc.COLOR_YUV2BGR_YV12);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bmp);
        return bmp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CaptureHelper.getInstance().release();
    }
}
