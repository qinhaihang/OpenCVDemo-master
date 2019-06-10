package com.qhh.opencvdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    public static Bitmap compressSampling(Context context, int resId, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    /**
     * 根据尺寸压缩
     * @return
     */
    public static Bitmap compressSize(Context context,int resId,int reqWidth, int reqHeight ){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inJustDecodeBounds = false;
        int sampleSize = caculateSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    /**
     * 计算出所需要压缩的大小
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private static int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

}
