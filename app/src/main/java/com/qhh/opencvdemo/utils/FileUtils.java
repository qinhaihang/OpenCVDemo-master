package com.qhh.opencvdemo.utils;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.qhh.opencvdemo.Constants.basePath;
import static com.qhh.opencvdemo.Constants.videoPath;

/**
 * @author qinhaihang
 * @version $Rev$
 * @time 19-5-12 下午2:32
 * @des
 * @packgename com.qhh.opencvdemo.utils
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class FileUtils {

    public static boolean checkFileExists(String path){
        boolean flag = false;
        File file = new File(path);
        if(!file.exists() && file.isDirectory()){
            flag = file.mkdirs();
        }
        return flag;
    }

    public static void save2SDbin(String basePath,byte[] data,String name){

        File baseFile = new File(basePath);
        if(!baseFile.exists() || !baseFile.isDirectory()){
            baseFile.mkdirs();
        }

        File binFile = new File(baseFile, name + ".YUV");

        FileOutputStream fos = null;
        DataOutputStream dos = null;

        try {

            fos = new FileOutputStream(binFile, true);
            dos = new DataOutputStream(fos);

            long startTime = System.currentTimeMillis();

            dos.write(data);
            dos.flush();

            long time = System.currentTimeMillis() - startTime;
            Log.i("qhh_time","save time = " + time);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(dos != null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean copyAssetsFileToPath(Context context, String fileName) {

        File dirFile = new File(basePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dirFile.mkdirs();
        }
        File outFile = new File(videoPath);
        if (outFile.exists() && outFile.isFile()) {
            outFile.delete();
        }
        try {
            InputStream in = context.getAssets().open(fileName);
            if (in == null) {
                Log.e("", fileName + " the src file is not existed");
                return false;
            }
            OutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int length = in.read(buffer);
            while (length > 0) {
                out.write(buffer, 0, length);
                length = in.read(buffer);
            }

            out.flush();
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            outFile.delete();
            e.printStackTrace();
        }
        return false;
    }

}
