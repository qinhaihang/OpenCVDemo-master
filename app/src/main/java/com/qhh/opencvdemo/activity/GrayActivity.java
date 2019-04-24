package com.qhh.opencvdemo.activity;

import android.Manifest;
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
import com.qhh.permission.PermissionHelper;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class GrayActivity extends AppCompatActivity {

    private ImageView iv_original;
    private ImageView iv_gray;
    private File saveFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gray);
        initView();
        PermissionHelper.getInstance()
                .init(this)
                .checkPermission(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initView() {
        iv_original = findViewById(R.id.iv_original);
        iv_gray = findViewById(R.id.iv_gray);
    }

    public void process(View view) {

        if(saveFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());

            Mat dir= new Mat();
            Mat source = new Mat();
            Utils.bitmapToMat(bitmap, source);
            Imgproc.cvtColor(source,dir,Imgproc.COLOR_BGRA2GRAY);
            Utils.matToBitmap(dir,bitmap);
            iv_gray.setImageBitmap(bitmap);
            source.release();
            dir.release();
        }

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeFile(path);
                                iv_original.setImageBitmap(bitmap);
                            }
                        });
                    }
                })
                .requestCapture(saveFile.getAbsolutePath());

    }

}
