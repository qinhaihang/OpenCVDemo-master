package com.qhh.opencvdemo.utils;

import java.io.File;

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

}
