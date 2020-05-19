package com.proj.main;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

    public Window(int width, int height, String title, Environment environment) {
        //create a new window
        JFrame frame = new JFrame(title);

        //set window size
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //really important
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //window will start in the middle of the screen
        frame.add(environment); //adding the environment into the contentPane of JFrame
        frame.setVisible(true); //see the frame
        environment.start();
    }
}
