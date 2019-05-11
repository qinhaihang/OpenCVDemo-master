package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadImageInfoActivity extends AppCompatActivity {

    private String testImagePath;
    private TextView mImgInfo;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_image_info);
        saveTestImage2SD();
        initView();
    }

    private void initView() {
        mImgInfo = findViewById(R.id.tv_imgInfo);
        mImage = findViewById(R.id.image);
        mImage.setImageDrawable(getDrawable(R.drawable.face));
    }

    public void readImageInfo(View view) {
        Mat imread = Imgcodecs.imread(testImagePath);
        int channels = imread.channels();
        int width = imread.cols();
        int height = imread.rows();
        int dims = imread.dims();
        int depth = imread.depth();
        int type = imread.type();
        StringBuffer infos = new StringBuffer();
        infos.append("channels = " + channels + "\n");
        infos.append("width = " + width + "\n");
        infos.append("height = " + height + "\n");
        infos.append("dims = " + dims + "\n");
        infos.append("depth = " + depth + "\n");
        infos.append("type = " + type + "\n");
        mImgInfo.setText(infos.toString());
    }

    public void saveTestImage2SD(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        File file = new File(Constants.TEST_IMAGE);
        if(!file.exists()){
            file.mkdirs();
        }
        File saveFile = new File(file, "test.jpg");
        testImagePath = saveFile.getAbsolutePath();
        try {
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
    }
}
