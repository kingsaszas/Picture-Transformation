package sample.com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import java.util.Scanner;

public class Main extends JFrame {

    private JButton binButton, brighteningButton, dimmingButton, originalButton, dilatation, erosion;
    private JPanel picturePanel, buttonsPanel;
    private JDrawPanel jDrawPanel;
    private JTextField writebrightnes, writebin;
    private JTextArea warningsArea;
    private JLabel infobrightness, info, warnings;
    private JComboBox jComboBox;

    DataGenerator dg;

    public void showPicture() {
        try {
            BufferedImage img = ImageIO.read(new File("Mapa_MD_no_terrain_low_res_dark_Gray.bmp"));
            dg.lakeimg = img;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Main(String title) {
        super(title);

        this.dg = new DataGenerator();

        Functions2 f2 = new Functions2(dg);

        //buttons
        binButton = new JButton("binarization");
        brighteningButton = new JButton("brightness");
        dimmingButton = new JButton("dimming picture");
        originalButton = new JButton("original picture");
        //dilatation = new JButton("dilatation");
        //erosion = new JButton("erosion");

        writebin = new JTextField("0");
        writebrightnes = new JTextField("0");
        infobrightness = new JLabel("brigthness level:");
        info = new JLabel("binarization level:");
        warnings = new JLabel("warnings:");

        warningsArea = new JTextArea();
        warningsArea.setLineWrap(true);
        warningsArea.setWrapStyleWord(true);
        warningsArea.setSize(50, 35);

        //ComboBox - lab2
        JComboBox<String> jComboBox= new JComboBox<String>();
        jComboBox.addItem("dilatation");
        jComboBox.addItem("erosion");
        jComboBox.addItem("down - filtr");
        jComboBox.addItem("up - filtr");
        jComboBox.addItem("Gauss - filtr");
        jComboBox.addItem("opening morphologic");
        jComboBox.addItem("closing morphologic");

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(13, 1));
        buttonsPanel.add(originalButton);
        buttonsPanel.add(infobrightness);
        buttonsPanel.add(writebrightnes);
        buttonsPanel.add(brighteningButton);
        buttonsPanel.add(dimmingButton);
        buttonsPanel.add(info);
        buttonsPanel.add(writebin);
        buttonsPanel.add(binButton);
        buttonsPanel.add(warnings);
        buttonsPanel.add(warningsArea);
        buttonsPanel.add(jComboBox);

        //image buffor
        showPicture();

        //buttons action:
        originalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dg.binarizationOn = "no";
                showPicture();
                jDrawPanel.repaint();
            }
        });

        brighteningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int val = Integer.parseInt(writebrightnes.getText());
                    if (val >= 0 && val <= 255) {
                        warningsArea.setText(null);
                        dg.changeImage(val, '+');
                    } else {
                        warningsArea.setText("Value must be between 0 and 255");
                    }
                } catch (NumberFormatException ex) {
                    warningsArea.setText("the value must be an integer");
                }

                jDrawPanel.repaint();
            }
        });

        dimmingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int val = Integer.parseInt(writebrightnes.getText());
                    if (val >= 0 && val <= 255) {
                        warningsArea.setText(null);
                        dg.changeImage(val, '-');
                    } else {
                        warningsArea.setText("Value must be between 0 and 255");
                    }
                } catch (NumberFormatException ex) {
                    warningsArea.setText("the value must be an integer");
                }

                jDrawPanel.repaint();
            }
        });

        binButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dg.binarization(180);
               try {
                    int level = Integer.parseInt(writebin.getText());
                   // System.out.println("level: " + level);
                    dg.binarization(level);
                    dg.binarizationOn = "Yes";
                    warningsArea.setText(null);
                } catch(NumberFormatException ex) {
                    warningsArea.setText("the value must be an integer");
                }
                jDrawPanel.repaint();
            }
        });

        //ComboBox - lab2
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> function = (JComboBox<String>) e.getSource();
                String selectedFunction = (String) function.getSelectedItem();

                if(dg.binarizationOn.equals("Yes")){
                    if (selectedFunction.equals("dilatation")) {
                        f2.dilatation();
                        jDrawPanel.repaint();

                    } else if (selectedFunction.equals("erosion")) {
                        f2.erosion();
                        jDrawPanel.repaint();

                    } else if (selectedFunction.equals("opening morphologic")) {
                        f2.erosion();
                        f2.dilatation();
                        jDrawPanel.repaint();

                    } else if (selectedFunction.equals("closing morphologic")) {
                        f2.dilatation();
                        f2.erosion();
                        jDrawPanel.repaint();
                    }
                }


                if (selectedFunction.equals("down - filtr")) {
                    f2.filtrDown();
                    jDrawPanel.repaint();

                } else if (selectedFunction.equals("up - filtr")) {
                    f2.filtrUp();
                    jDrawPanel.repaint();

                } else if (selectedFunction.equals("Gauss - filtr")) {
                    f2.filtrGauss();
                    jDrawPanel.repaint();

                }

            }
        });


        //window
        jDrawPanel = new JDrawPanel(dg);

        picturePanel = new JPanel();
        picturePanel.setLayout(new BorderLayout());
        picturePanel.add(BorderLayout.EAST, buttonsPanel);
        picturePanel.add(BorderLayout.CENTER, jDrawPanel);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(300, 100);
        this.setContentPane(picturePanel);
        this.setSize(new Dimension(780, dg.lakeimg.getHeight() + 200));
        this.setResizable(false);
        this.setVisible(true);

    }

    public static void main(String[] args) {
        Main main = new Main("Lab1");
        main.jDrawPanel.repaint();
    }
}
