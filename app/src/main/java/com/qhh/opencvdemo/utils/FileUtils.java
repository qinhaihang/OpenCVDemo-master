package com.qhh.opencvdemo.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

}
