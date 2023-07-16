package com.cloud.photo.user.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRotateUtils {
    //把图片向右旋转90度后保存到新图片
    public static void rotateClockwise90(File picFile, String outFilePath) throws IOException {
        //把原图读入内存
        BufferedImage bufferedImage = ImageIO.read(picFile);

        //获取原图宽、高
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        //内存中创建新图片（老图片旋转90度之后），其宽、高与原始图片相反
        BufferedImage rotateImg = new BufferedImage(height, width, bufferedImage.getType());

        //新建变换（变换的实际效果和代码顺序是反的，下面实际效果是先旋转后平移）
        AffineTransform trans = new AffineTransform();
        //2.由于原点在左上角，所以顺时针旋转90度后图片在可视范围左侧，需向右移动过来
        trans.translate(height, 0);
        //1.原始图片顺时针旋转90度，若想任意角度，请自行修改
        trans.rotate(Math.PI*0.5);

        //获取新建内存图片的“图形对象”，通过它在内存图片中绘图
        Graphics2D graphics2D = (Graphics2D)rotateImg.getGraphics();
        //绘图前，先设置变换对象，应用上面的旋转、平移
        graphics2D.setTransform(trans);

        //将老图片，从新图片的原点（0,0）点开始绘制到新图片中
        graphics2D.drawImage(bufferedImage, 0, 0, null);

        //到此为止，图片已顺时针旋转完毕，以“.jpg”格式保存到本地文件中
        ImageIO.write(rotateImg, "jpg", new File(outFilePath));
    }

    public static void main(String[] args) {
        File origin = new File("F:/SpringCloud/vips-dev-8.14/img/preview_600.jpg");

        try {
            rotateClockwise90(origin, "F:/SpringCloud/vips-dev-8.14/img/preview_600_rotate.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
