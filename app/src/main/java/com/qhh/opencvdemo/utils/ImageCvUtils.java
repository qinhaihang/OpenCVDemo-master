package com.qhh.opencvdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

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
        Bitmap result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int sampleSize = caculateSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        result = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return result;
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
            while (halfPicWidth / sampleSize > reqWidth && halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 原图
     * @return bitmap 旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        // 图片旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 得到旋转后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
