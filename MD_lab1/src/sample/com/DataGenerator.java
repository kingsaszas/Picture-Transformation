package sample.com;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class DataGenerator {

    BufferedImage lakeimg;

    String binarizationOn = "No";


    public DataGenerator() {
        //
    }


    public void binarization(int binValue) {
        for (int i = 0; i < lakeimg.getHeight(); i++) {
            for (int j = 0; j < lakeimg.getWidth(); j++) {

                Color c = new Color(lakeimg.getRGB(j, i));

                if (c.getRed() <= binValue && c.getGreen() <= binValue && c.getBlue() <= binValue) {
                    lakeimg.setRGB(j, i, Color.BLACK.getRGB());
                } else {
                    lakeimg.setRGB(j, i, Color.WHITE.getRGB());
                }
            }
        }
    }

    public void changeImage(int value, char sign) {
        for (int i = 0; i < lakeimg.getHeight(); i++) {
            for (int j = 0; j < lakeimg.getWidth(); j++) {

                Color color = new Color(lakeimg.getRGB(j, i));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                if (sign == '+') {
                    r = r + value;
                    g = g + value;
                    b = b + value;
                }

                if (sign == '-') {
                    r -= value;
                    g -= value;
                    b -= value;
                }

                if (r >= 255) {
                    r = 255;
                } else if (r <= 0) {
                    r = 0;
                }
                if (g >= 255) {
                    g = 255;
                } else if (g <= 0) {
                    g = 0;
                }
                if (b >= 255) {
                    b = 255;
                } else if (b <= 0) {
                    b = 0;
                }

                lakeimg.setRGB(j, i, new Color(r, g, b).getRGB());

            }
        }
    }


}
