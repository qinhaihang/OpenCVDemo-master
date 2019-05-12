package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MatConvertBitmapActivity extends AppCompatActivity {

    private String testImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_convert_bitmap);
        saveTestImage2SD();
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

    public void bitmapCovertMat(View view) {
        Bitmap bitmap = BitmapFactory.decodeFile(testImagePath);

//        Utils.bitmapToMat();
    }

}
