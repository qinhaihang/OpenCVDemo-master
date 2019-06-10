package com.qhh.opencvdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qhh.opencvdemo.Constants;
import com.qhh.opencvdemo.R;
import com.qhh.opencvdemo.utils.ImageCvUtils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.CvType.CV_16S;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.Sobel;

public class BarConrrectActivity extends AppCompatActivity {

    private ImageView iv_gray;
    private ImageView iv_src;
    private ImageView iv_sobel;
    private ImageView iv_dft;
    private ImageView iv_threshold;
    private ImageView iv_houghlines;
    private ImageView iv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_conrrect);
        iv_src = findViewById(R.id.iv_src);
        iv_gray = findViewById(R.id.iv_gray);
        iv_sobel = findViewById(R.id.iv_sobel);
        iv_dft = findViewById(R.id.iv_dft);
        iv_threshold = findViewById(R.id.iv_threshold);
        iv_houghlines = findViewById(R.id.iv_houghlines);
        iv_result = findViewById(R.id.iv_result);
        saveBar2SD();
    }

    private void saveBar2SD(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bar);
        File file = new File(Constants.TEST_IMAGE);
        if(!file.exists()){
            file.mkdirs();
        }
        File saveFile = new File(file, "bar.jpg");
        Constants.barImage = saveFile.getAbsolutePath();
        try {
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
    }

    public void conrrect(View view) {

        Mat src = Imgcodecs.imread(Constants.barImage);
        iv_src.setImageBitmap(ImageCvUtils.mat2Bitmap(src));

        Mat imageGray = new Mat(), imageGuussian = new Mat();

        Mat imageSobelX = new Mat(), imageSobelY = new Mat(), imageSobelOut = new Mat();

        Imgproc.cvtColor(src, imageGray, Imgproc.COLOR_BGR2GRAY);

        iv_gray.setImageBitmap(ImageCvUtils.mat2Bitmap(imageGray));
//        Imgcodecs.imwrite(Constants.TEST_IMAGE + "gray_rlt.jpg", imageGray);

        Imgproc.GaussianBlur(imageGray, imageGuussian, new Size(3, 3), 0);
        //水平和垂直方向灰度图像的梯度和,使用Sobel算子
        Mat imageX16S = new Mat(), imageY16S = new Mat();
        Sobel(imageGuussian, imageX16S, CV_16S, 1, 0, 3, 1, 0, 4);
        Sobel(imageGuussian, imageY16S, CV_16S, 0, 1, 3, 1, 0, 4);
        convertScaleAbs(imageX16S, imageSobelX, 1, 0);
        convertScaleAbs(imageY16S, imageSobelY, 1, 0);
        Core.add(imageSobelX, imageSobelY, imageSobelOut);

        iv_sobel.setImageBitmap(ImageCvUtils.mat2Bitmap(imageSobelOut));
//        Imgcodecs.imwrite(Constants.TEST_IMAGE + "xy_rlt.jpg", imageSobelOut);

        Mat srcImg = imageSobelOut;
        //宽高扩充，非必须，特定的宽高可以提高傅里叶运算效率
        Mat padded = new Mat();
        int opWidth = Core.getOptimalDFTSize(srcImg.rows());
        int opHeight = Core.getOptimalDFTSize(srcImg.cols());
        Core.copyMakeBorder(srcImg, padded, 0, opWidth - srcImg.rows(), 0, opHeight - srcImg.cols(), BORDER_CONSTANT, Scalar.all(0));
        padded.convertTo(padded, CvType.CV_32FC1);
        // Mat[] planes = {padded, Mat.zeros(padded.size(), CvType.CV_32FC1)};
        Mat comImg = new Mat();
        //通道融合，融合成一个2通道的图像
        comImg.convertTo(comImg, CvType.CV_32FC1);
        List<Mat> planesList = new ArrayList<>();
        planesList.add(padded);
        planesList.add(Mat.zeros(padded.size(), CvType.CV_32FC1));
        Core.merge(planesList, comImg);
        Core.dft(comImg, comImg);
        Core.split(comImg, planesList);
        Core.magnitude(planesList.get(0), planesList.get(1), planesList.get(0));
        Mat magMat = planesList.get(0);
        Core.add(magMat, Scalar.all(1), magMat);
        Core.log(magMat, magMat);
        //对数变换，方便显示
        //        magMat = magMat(Rect(0, 0, magMat.cols & -2, magMat.rows & -2));
        //        magMat = magMat.submat(0, magMat.rows() & -2, 0, magMat.cols() & -2);
        magMat = magMat.submat(new Rect(0, 0, magMat.cols() & -2, magMat.rows() & -2));
        //以下把傅里叶频谱图的四个角落移动到图像中心
        int cx = magMat.cols() / 2;
        int cy = magMat.rows() / 2;
        Mat q0 = new Mat(magMat, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(magMat, new Rect(0, cy, cx, cy));
        Mat q2 = new Mat(magMat, new Rect(cx, cy, cx, cy));
        Mat q3 = new Mat(magMat, new Rect(cx, 0, cx, cy));
        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q2.copyTo(q0);
        tmp.copyTo(q2);
        q1.copyTo(tmp);
        q3.copyTo(q1);
        tmp.copyTo(q3);
        Core.normalize(magMat, magMat, 0, 1, Core.NORM_MINMAX);
        Mat magImg = new Mat(magMat.size(), CV_8UC1);
        magMat.convertTo(magImg, CV_8UC1, 255, 0);

        iv_dft.setImageBitmap(ImageCvUtils.mat2Bitmap(magImg));
//        Imgcodecs.imwrite(Constants.TEST_IMAGE + "dft_rlt.jpg", magImg);

        Imgproc.threshold(magImg, magImg, 180, 255, Imgproc.THRESH_BINARY);

        iv_threshold.setImageBitmap(ImageCvUtils.mat2Bitmap(magImg));
//        Imgcodecs.imwrite(Constants.TEST_IMAGE + "threshold_rlt.jpg", magImg);


        MatOfFloat lines = new MatOfFloat();
        float pi180 = (float) PI / 180;
        Mat linImg = new Mat(magImg.size(), CV_8UC3);
        Imgproc.HoughLines(magImg, lines, 1, pi180, 100, 0, 0);
        int numLines = lines.rows();
        double theta = 0;
        double t = 0;
        for (int l = 0; l < numLines; l++) {
            double[] vec = lines.get(l, 0);
            double rho = vec[0];
            theta = vec[1];

            Point pt1 = new Point();
            Point pt2 = new Point();

            double a = Math.cos(theta);
            double b = Math.sin(theta);

            double x0 = a * rho;
            double y0 = b * rho;

            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * (a));
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * (a));

            if (theta >= 0) {
                Imgproc.line(linImg, pt1, pt2, new Scalar(255, 0, 0), 3, 8, 0);
            }
            if (theta != 0) {
                t = theta;
            }

        }

        iv_houghlines.setImageBitmap(ImageCvUtils.mat2Bitmap(linImg));
//        Imgcodecs.imwrite(IMG_DIR + "houghLines_rlt.jpg", linImg);
        //        t = theta / index;


        //校正角度计算
        float angelD = (float) (180 * t / PI - 90);
        Point center = new Point(src.cols() / 2, src.rows() / 2);
        Mat rotMat = Imgproc.getRotationMatrix2D(center, angelD, 1.0);
        Mat imageSource = Mat.ones(src.size(), CV_8UC3);
        Imgproc.warpAffine(src, imageSource, rotMat, src.size(), 1, 0, new Scalar(255, 255, 255));//仿射变换校正图像

        iv_result.setImageBitmap(ImageCvUtils.mat2Bitmap(imageSource));
//        Imgcodecs.imwrite(IMG_DIR + "jiaodu_rlt.jpg", imageSource);
        ////Zbar一维码识别
        //ImageScanner scanner;
        //scanner.set_config(ZBAR_NONE, ZBAR_CFG_ENABLE, 1);
        //int width1 = imageSource.cols;
        //int height1 = imageSource.rows;
        //uchar * raw = (uchar *) imageSource.data;
        //Image imageZbar (width1, height1, "Y800", raw, width1 * height1);
        //scanner.scan(imageZbar); //扫描条码
        //Image::SymbolIterator symbol = imageZbar.symbol_begin();
        //if (imageZbar.symbol_begin() == imageZbar.symbol_end()) {
        //    cout << "查询条码失败，请检查图片！" << endl;
        //}
        //for (; symbol != imageZbar.symbol_end(); ++symbol) {
        //    cout << "类型：" << endl << symbol -> get_type_name() << endl << endl;
        //    cout << "条码：" << endl << symbol -> get_data() << endl << endl;
        //}
        //namedWindow("Source Window", 0);
        //imshow("Source Window", imageSource);
        //waitKey();
        //imageZbar.set_data(NULL, 0);

    }

}
