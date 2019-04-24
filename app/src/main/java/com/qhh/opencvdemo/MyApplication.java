package com.qhh.opencvdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/4/24 20:58
 * @des
 * @packgename com.qhh.opencvdemo
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
