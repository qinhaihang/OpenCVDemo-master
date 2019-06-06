package com.qhh.opencvdemo.activity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/6/6 19:12
 * @des
 * @packgename com.qhh.opencvdemo.activity
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class RotationActivityTest {

    @Test
    public void I420toNV21() {

        byte[] I420 = {
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                2, 3, 4, 5,
                6, 7, 8, 9
        };

        int w = 4;
        int h = 4;

        int uStart = w * h;
        int vStart = w * h * 5 >> 2;

        byte u0 = I420[uStart];
        byte v0 = I420[vStart];

        System.out.println("u 0 = " + u0);
        System.out.println("v 0 = " + v0);

        byte[] uvTemp = new byte[w * h >> 1];

        byte[] u = new byte[w * h >> 2];
        System.arraycopy(I420,uStart,u,0,u.length);

        byte[] v = new byte[w * h >> 2];
        System.arraycopy(I420,vStart,v,0,v.length);

        int uCount = 0;
        int vCount = 0;

        for (int i = 0; i < uvTemp.length; i++) {
            if(i % 2 == 0){ //偶数 v分量
                uvTemp[i] = v[vCount];
                vCount++;
            }else{ //奇数 u分量
                uvTemp[i] = u[uCount];
                uCount++;
            }
        }

        for (int i = 0; i < uvTemp.length; i++) {
            System.out.print(uvTemp[i] + ",");
        }

    }

}
