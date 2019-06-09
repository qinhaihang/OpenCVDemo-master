package com.qhh.opencvdemo.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.camera.CameraConfig;
import com.qhh.opencvdemo.camera.CameraUtil;
import com.qhh.opencvdemo.utils.ImageFormatUtils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class RotationActivity extends AppCompatActivity {

    private TextureView mTexture;

    private LinkedBlockingQueue<byte[]> mQueue = new LinkedBlockingQueue<>();
    private ExecutorService mExecutorService;

    private int mWidth;
    private int mHeight;
    private ImageView mIvRatation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rotation);
        mTexture = findViewById(R.id.texture);
        mIvRatation = findViewById(R.id.iv_ratation);
        mTexture.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera(surface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                CameraUtil.getInstance().releaseCameraIfNeed();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        final int[] count = {0};
        mExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {

                while (true){

                    if(mWidth == 0 || mHeight == 0){
                        continue;
                    }

                    try {
                        byte[] data = mQueue.take();

                        Mat src = new Mat(mHeight * 3 >> 1, mWidth, CvType.CV_8UC1);
                        src.put(0,0,data);

                        Mat bgr = new Mat();
                        Imgproc.cvtColor(src,bgr,Imgproc.COLOR_YUV2BGR_NV21);

                        Mat bgrResult = new Mat();
                        Mat matRotation = Imgproc.getRotationMatrix2D(new Point(mHeight / 2, mWidth / 2), 20.0, 1);
                        Imgproc.warpAffine(bgr,bgrResult,matRotation,bgrResult.size());

                        Mat yuvResult = new Mat();
                        Imgproc.cvtColor(bgrResult,yuvResult,Imgproc.COLOR_BGR2YUV_I420);

                        byte[] resultByte = new byte[yuvResult.cols() * yuvResult.rows()  * 3 >> 1];

                        yuvResult.get(0,0,resultByte);

                        byte[] nv21 = ImageFormatUtils.colorI420toNV21(resultByte, mWidth, mHeight);
//
//                        count[0]++;
//                        FileUtils.save2SDbin(Constants.BIN_PATH, nv21, count[0] + "");

                        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, mWidth, mHeight, null);
                        if (yuvImage != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            yuvImage.compressToJpeg(new Rect(0, 0, mWidth, mHeight), 100, stream);

                            final Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIvRatation.setImageBitmap(bitmap);
                                }
                            });

                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void openCamera(SurfaceTexture surface) {
        CameraConfig build = new CameraConfig.Builder()
                .facingBackCamera()
                .facingFrontCamera()
                .orientation(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 90 : 0)
                .support720p()
                .support480p()
                .build();

        CameraUtil.getInstance().setPreviewCallback(new CameraUtil.PreviewCallback() {

            @Override
            public void onSize(int width, int height) {
                Log.i("qhh", "width = " + width + ", height = " + height);
                mWidth = width;
                mHeight = height;
            }

            @Override
            public void onPreviewFrame(byte[] data) {

                try {
                    mQueue.put(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        CameraUtil.getInstance().openCamera(build, surface);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();
        mExecutorService = null;
    }
}
