package com.qhh.opencvdemo;

import android.os.Environment;

import java.io.File;

/**
 * @author qinhaihang
 * @version $Rev$
 * @time 19-5-11 下午12:40
 * @des
 * @packgename com.qhh.opencvdemo
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class Constants {

    public static final String TEST_IMAGE = Environment.getExternalStorageDirectory() + File.separator + "OpenCVDemo"+File.separator;


    public static String basePath = Environment.getExternalStorageDirectory() + File.separator + "OpenCVDemo" + File.separator;
    public static String videoPath = basePath + "test.mp4";

    public static String testImage;
    public static String barImage;

    public static final String BIN_PATH = TEST_IMAGE + File.separator + "bin";

}
