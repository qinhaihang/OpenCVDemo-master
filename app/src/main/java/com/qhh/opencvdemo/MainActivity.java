package com.qhh.opencvdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.qhh.opencvdemo.activity.AddActivity;
import com.qhh.opencvdemo.activity.AmbiguityActivity;
import com.qhh.opencvdemo.activity.BrightContrastActivity;
import com.qhh.opencvdemo.activity.GrayActivity;
import com.qhh.opencvdemo.activity.MeanStdDevActivity;
import com.qhh.opencvdemo.activity.ReadImageInfoActivity;
import com.qhh.opencvdemo.activity.SaveMat2SDActivity;
import com.qhh.opencvdemo.adapter.MainAdatper;
import com.qhh.permission.PermissionHelper;
import com.qhh.permission.callback.ICallbackManager;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdatper.OnItemClickListener {


    private ArrayList<String> mItems;
    private MainAdatper mMainAdatper;
    private Class[] activitys = new Class[]{GrayActivity.class, ReadImageInfoActivity.class,
            SaveMat2SDActivity.class, MeanStdDevActivity.class, AddActivity.class, BrightContrastActivity.class,
            AmbiguityActivity.class};
    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initLoadOpenCV();
        initItem();
        PermissionHelper.getInstance()
                .init(this)
                .setmRequestCallback(new ICallbackManager.IRequestCallback() {
                    @Override
                    public void onAllPermissonGranted(boolean flag) {
                        Log.d("qinhaihang","onAllPermissonGranted = " + flag);
                        if(flag){
                            saveTestImage2SD();
                        }
                    }
                })
                .checkPermission(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initView() {
        mRv = findViewById(R.id.rv);
        mItems = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRv.setLayoutManager(layoutManager);
        mMainAdatper = new MainAdatper(mItems,this);
        mRv.setAdapter(mMainAdatper);
    }

    private void initItem() {

        mItems.add("灰度");
        mItems.add("读取图片信息");
        mItems.add("Mat对象的转为图片保存本地");
        mItems.add("计算图像均值方差做二分值处理");
        mItems.add("两个图像相加");
        mItems.add("调整图像亮度和对比度");
        mItems.add("模糊");

        mMainAdatper.setDatas(mItems);
    }

    private void initLoadOpenCV(){
        boolean isDebug = OpenCVLoader.initDebug();
        if(isDebug){
            Log.i("qhh","init openCV success!!");
        }else{
            Log.e("qhh","init openCV failure!!");
        }
    }

    public void saveTestImage2SD(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        File file = new File(Constants.TEST_IMAGE);
        if(!file.exists()){
            file.mkdirs();
        }
        File saveFile = new File(file, "test.jpg");
        Constants.testImage = saveFile.getAbsolutePath();
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

    @Override
    public void onItemClick(int position) {

        startActivity(new Intent(this,activitys[position]));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PermissionHelper.getInstance().release();
    }
}
