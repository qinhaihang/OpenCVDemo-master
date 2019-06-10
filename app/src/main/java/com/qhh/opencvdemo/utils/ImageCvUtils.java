package com.qhh.opencvdemo.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/6/10 15:08
 * @des
 * @packgename com.qhh.opencvdemo.utils
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class ImageCvUtils {

    public static Bitmap mat2Bitmap(Mat src){
        Bitmap bitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat cvResult = new Mat();

        Imgproc.cvtColor(src,cvResult,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(cvResult,bitmap);
        cvResult.release();
        return bitmap;
    }

}
