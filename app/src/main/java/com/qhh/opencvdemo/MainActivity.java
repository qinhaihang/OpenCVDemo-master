package com.qhh.opencvdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.qhh.opencvdemo.activity.GrayActivity;
import com.qhh.opencvdemo.activity.ReadImageInfoActivity;
import com.qhh.opencvdemo.activity.SaveMat2SDActivity;
import com.qhh.opencvdemo.adapter.MainAdatper;
import com.qhh.permission.PermissionHelper;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdatper.OnItemClickListener {


    private ArrayList<String> mItems;
    private MainAdatper mMainAdatper;
    private Class[] activitys = new Class[]{GrayActivity.class, ReadImageInfoActivity.class,
            SaveMat2SDActivity.class};
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
