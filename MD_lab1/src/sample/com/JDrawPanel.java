package sample.com;

import javax.swing.*;
import java.awt.*;

public class JDrawPanel extends JPanel {

    DataGenerator dg;

    JDrawPanel(DataGenerator dg) {
        this.dg = dg;
    }

    //wyswietla obrazek
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(dg.lakeimg, 0, 0, this);

        repaint();
    }

    @Override
    public void repaint(){
        super.repaint();
    }

}
