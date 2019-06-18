package com.qhh.opencvdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/6/9 15:33
 * @des
 * @packgename com.qhh.opencvdemo.utils
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class ImageFormatUtils {

    /**
     *
     * @param I420
     * @param w 原图大小分辨率
     * @param h 原图大小分辨率
     * @return
     */
    public static byte[] colorI420toNV21(byte[] I420, int w, int h) {

        int uStart = w * h;
        int vStart = w * h * 5 >> 2;

//        byte u0 = I420[uStart];
//        byte v0 = I420[vStart];

//        System.out.println("u 0 = " + u0);
//        System.out.println("v 0 = " + v0);

        byte[] uvTemp = new byte[w * h >> 1];

        byte[] u = new byte[w * h >> 2];
        System.arraycopy(I420, uStart, u, 0, u.length);

        byte[] v = new byte[w * h >> 2];
        System.arraycopy(I420, vStart, v, 0, v.length);

        int uCount = 0;
        int vCount = 0;

        for (int i = 0; i < uvTemp.length; i++) {
            if (i % 2 == 0) { //偶数 v分量
                uvTemp[i] = v[vCount];
                vCount++;
            } else { //奇数 u分量
                uvTemp[i] = u[uCount];
                uCount++;
            }
        }

//        for (int i = 0; i < uvTemp.length; i++) {
//            System.out.print(uvTemp[i] + ",");
//        }

        System.arraycopy(uvTemp, 0, I420, uStart, uvTemp.length);

//        for (int i = 0; i < I420.length; i++) {
//
//            if(i%4 == 0){
//                System.out.print("\n"+I420[i] + ",");
//            }else{
//                System.out.print(I420[i] + ",");
//            }
//        }

        return I420;
    }

    public static Bitmap nv21toBitmap(byte[] nv21,int width,int height){
        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        if (yuvImage != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, stream);

            final Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return bitmap;
            }
        }
        return null;
    }

}
