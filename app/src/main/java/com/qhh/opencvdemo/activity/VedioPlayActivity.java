package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.utils.FileUtils;
import com.qhh.opencvdemo.utils.ImageFormatUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.opencv.videoio.Videoio.CAP_PROP_FPS;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_COUNT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

public class VedioPlayActivity extends AppCompatActivity {

    private ExecutorService mTask;
    private boolean isRelease;
    private ImageView iv_vedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_play);
        FileUtils.copyAssetsFileToPath(this,"test.mp4");

        iv_vedio = findViewById(R.id.iv_vedio);
    }

    public void play(View view) {

        mTask = Executors.newSingleThreadExecutor();
        mTask.submit(new Runnable() {
            @Override
            public void run() {
                playVedio();
            }
        });

    }

    private void playVedio() {
        VideoCapture videoCapture = new VideoCapture();

        videoCapture.open(Constants.videoPath);

        if(!videoCapture.isOpened()){
            Log.e("qhh_vedio","vedio open failure!");
            return;
        }

        double totalFrame = videoCapture.get(CAP_PROP_FRAME_COUNT);
        double fpsRate = videoCapture.get(CAP_PROP_FPS);
        double pauseTime=1000/fpsRate; // 由帧率计算两幅图像间隔时间

        double width = videoCapture.get(CAP_PROP_FRAME_WIDTH);
        double height = videoCapture.get(CAP_PROP_FRAME_HEIGHT);

        int count = 0;
        Mat image = new Mat();
        while (true){

            if(isRelease){
                break;
            }

            boolean read = videoCapture.read(image);
            if(!read){
                continue;
            }

            if(!image.empty()){
                Log.i("qhh_vedio","width = " + image.cols() +",heigth = " + image.rows());

                Mat dst = new Mat();
                Imgproc.cvtColor(image,dst, Imgproc.COLOR_RGB2YUV_I420);

                byte[] I420 = new byte[dst.cols() * dst.rows() * 3 >> 1];

                dst.get(0,0,I420);

                Log.i("qhh_vedio","width = " + dst.cols() +",heigth = " + dst.rows());
                byte[] nv21 = ImageFormatUtils.colorI420toNV21(I420, dst.cols(), dst.rows());

                //count++;
                //FileUtils.save2SDbin(Constants.basePath, I420, count + "");

                //Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);

                Bitmap bitmap = ImageFormatUtils.nv21toBitmap(nv21, image.cols(), image.rows());

                Utils.matToBitmap(image,bitmap);

                image.release();

                showVedio(bitmap);
            }

            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        image.release();
    }

    private void showVedio(final Bitmap bitmap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_vedio.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTask.shutdown();
        mTask = null;
        isRelease = true;
    }
}
