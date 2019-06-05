package com.qhh.opencvdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import com.qhh.opencvdemo.R;

public class RotationActivity extends AppCompatActivity {

    private TextureView mTexture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        mTexture = findViewById(R.id.texture);
    }
}
