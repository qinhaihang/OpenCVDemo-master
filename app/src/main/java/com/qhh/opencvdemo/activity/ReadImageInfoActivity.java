package com.qhh.opencvdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ReadImageInfoActivity extends AppCompatActivity {

    private TextView mImgInfo;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_image_info);
        initView();
    }

    private void initView() {
        mImgInfo = findViewById(R.id.tv_imgInfo);
        mImage = findViewById(R.id.image);
        mImage.setImageDrawable(getDrawable(R.drawable.face));
    }

    public void readImageInfo(View view) {
        Mat imread = Imgcodecs.imread(Constants.testImage);
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

}
