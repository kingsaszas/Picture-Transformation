package sample.com;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Functions2 {
    
    DataGenerator dg;
    
    public Functions2(DataGenerator dg) {
        this.dg = dg;
    }

    //**********************************************************************

    public int arraySum(int tab[]) {
        int suma = 0;
        for(int i = 0; i<tab.length;i++) {
            suma += tab[i];
        }
        return suma;
    }


    public int convolution(int[] tab, int x, int y, int valArr, int valPixel){
        int conv = 0;

        //wi hi bym musiala przeslac i potem cos tam cos tam pobrac kolor a nie valPIxel jednego

        for(int i = 0; i < 3; i++){
            //System.out.println("valArr: "+ valArr + " |tab wartosc: "+ tab[valArr]+ " |x i y: "+ x +", "+y);
            conv += (tab[valArr]* valPixel);
            valArr ++;
            x++;
        }
        return conv;
    }


    public void filtr(int[] tab) {

        BufferedImage newimage = new BufferedImage(dg.lakeimg.getWidth(), dg.lakeimg.getHeight(),BufferedImage.TYPE_INT_RGB);
        int[][] pixelsFromImage = new int [dg.lakeimg.getHeight()][dg.lakeimg.getWidth()];
        int sumArr = arraySum(tab);

        //wypelniam tablice wartosciami pixeli z obrazku
        for (int hi= 1; hi< dg.lakeimg.getHeight() -1; hi++) {
            for (int wi = 1; wi< dg.lakeimg.getWidth() -1; wi++) {
                Color color = new Color(dg.lakeimg.getRGB(wi,hi));
                int val = color.getRed();
                pixelsFromImage[hi][wi] = val;
            }
        }

        for (int hi= 1; hi< dg.lakeimg.getHeight() -1; hi++) {
            for (int wi = 1; wi< dg.lakeimg.getWidth() -1; wi++) {

                int r = 0;
                int sumPixelNew = 0;

                int val = 0;

                for( int y = -1; y<=1; y++){
                    for(int x = -1; x <= 1; x++) {
                        sumPixelNew += pixelsFromImage[hi + y][wi + x] * tab[val];
                        val ++;
                    }
                }

                if(sumArr != 0)
                    r = sumPixelNew/sumArr;

                /*
                //sprawdzam sobie
                Color color = new Color(dg.lakeimg.getRGB(wi, hi));
                int getColorR = color.getRed();
                System.out.println("\nGetRGB r: " + getColorR);
                System.out.println("sumPixel R: "+ sumPixelNew);
                System.out.println("suma przez ktora dzielimy SumArr: " + sumArr);
                System.out.println("powstaly pkt r: " + r);

                 */

                //sprawdzamy czy nie wychodzi po za tablice

                if(r>255){
                    r = 255;
                } else if (r<0) {
                    r = 0;
                }

                newimage.setRGB(wi,hi,new Color(r,r,r).getRGB());

            }
        }

        dg.lakeimg = newimage;

    }


   public void filtrGauss() {

        int[] arrGauss = {1,4,1,4,32,4,1,4,1};
        filtr(arrGauss);

    }

    public void filtrDown() {

        int[] arrDown = {1,1,1,1,1,1,1,1,1};
        filtr(arrDown);

    }

    public void filtrUp() {

        int[] arrUp = {-1,-1,-1,-1,9,-1,-1,-1,-1};
        filtr(arrUp);

    }


    // *****************************************************************************************

    public Boolean searchNeighbourColor(int x, int y, int val) {
        int counter = 0;
        Boolean isOther = false;

        do {
            Color c = new Color(dg.lakeimg.getRGB(x, y));
            if (c.getRed() == val) { //erozja - 255 (jesli sasiad jest bialy) , dylatacja 0
                //newimage.setRGB(wi, hi, Color.WHITE.getRGB());  //to środkowy tez bialy i break
                isOther = true;
                break;
            } else {
                x++;
                counter++;
            }
        } while (counter < 3);

        return isOther;
    }


    public void frameOfPicture(BufferedImage newimage){
        for (int i= 0; i< dg.lakeimg.getWidth(); i++) {
            newimage.setRGB(i,0,dg.lakeimg.getRGB(i,0));
            newimage.setRGB(i,dg.lakeimg.getHeight() - 1,dg.lakeimg.getRGB(i,dg.lakeimg.getHeight() - 1));
        }

        for (int i= 0; i< dg.lakeimg.getHeight(); i++) {
            newimage.setRGB(0,i,dg.lakeimg.getRGB(0,i));
            newimage.setRGB(dg.lakeimg.getWidth() - 1,i,dg.lakeimg.getRGB(dg.lakeimg.getWidth() - 1,i));
        }
    }


    public void dilatation() {
        // jesli jestem biały 255 i mam czarnego sasiada 0 to staje sie czarny

        BufferedImage newimage = new BufferedImage(dg.lakeimg.getWidth(), dg.lakeimg.getHeight(),BufferedImage.TYPE_INT_RGB);

        frameOfPicture(newimage);

        //a teraz porwonujemy reszte
        for (int hi= 1; hi< dg.lakeimg.getHeight()-1; hi++) {
            for (int wi= 1; wi< dg.lakeimg.getWidth()-1; wi++) {

                Color c = new Color(dg.lakeimg.getRGB(wi,hi));

                //jesli jest biały i ma chociaz jednego czarnego sasiada to tez bedzie bialy. 0 to czarny, 255 to bialy
                if(c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    if (searchNeighbourColor(wi-1,hi-1,0)) {
                        newimage.setRGB(wi, hi, Color.BLACK.getRGB());
                    } else if (searchNeighbourColor(wi-1,hi,0)) {
                        newimage.setRGB(wi, hi, Color.BLACK.getRGB());
                    } else if (searchNeighbourColor(wi-1,hi+1,0)) {
                        newimage.setRGB(wi, hi, Color.BLACK.getRGB());
                    } else {
                        //jesli jestem bialy, a sasiedzi tez sa biali
                        newimage.setRGB(wi, hi, Color.WHITE.getRGB());
                    }
                } else {
                    //jesli jestem czarny, to pozostaje czarny
                    newimage.setRGB(wi, hi, Color.BLACK.getRGB());
                }
            }
        }
        dg.lakeimg = newimage;
    }


    public void erosion() {

        BufferedImage newimage = new BufferedImage(dg.lakeimg.getWidth(), dg.lakeimg.getHeight(),BufferedImage.TYPE_INT_RGB);
        //jesli hoc jeden z sasiedztwa jest bialy (255), to pkt centralny rowniez jest bialy

        //skrajne pkt uzupelnimy tymi samymi co są na obrazku wgranym, bo ich nie bedziemy badac
        frameOfPicture(newimage);

        //a teraz porwonujemy reszte
        for (int hi= 1; hi< dg.lakeimg.getHeight()-1; hi++) {
            for (int wi= 1; wi< dg.lakeimg.getWidth()-1; wi++) {

                Color c = new Color(dg.lakeimg.getRGB(wi,hi));

                //jesli jest czarny i ma chociaz jednego bialego sasiada to tez bedzie bialy. 0 to czarny, 255 to bialy
                if(c.getRed() == 0 && c.getGreen() == 0 && c.getBlue() == 0) {
                    //System.out.println("porownujemy reszte ");
                    if (searchNeighbourColor(wi-1,hi-1,255)) {
                        newimage.setRGB(wi, hi, Color.WHITE.getRGB());
                    } else if (searchNeighbourColor(wi-1,hi,255)) {
                        newimage.setRGB(wi, hi, Color.WHITE.getRGB());
                    } else if (searchNeighbourColor(wi-1,hi+1,255)) {
                        newimage.setRGB(wi, hi, Color.WHITE.getRGB());
                    }
                } else {
                    newimage.setRGB(wi, hi, Color.WHITE.getRGB());
                }

            }
        }
        dg.lakeimg = newimage;
    }
}
