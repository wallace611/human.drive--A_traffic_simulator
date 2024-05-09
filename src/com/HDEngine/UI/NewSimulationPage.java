package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

public class NewSimulationPage extends JFrame {
    
    JFrame frame = new JFrame();
    JPanel background = new JPanel();

    NewSimulationPage(){
        frame.setTitle("Human.Drive - Start a new simulation");
        frame.setSize(900, 600);
        
        // logo
        ImageIcon logo = new ImageIcon("photo/newsimulation-logo.png");
        frame.setIconImage(logo.getImage());
        
        background.setSize(500, 400);
        background.setBackground(new Color(20, 20, 20));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(background);

        //background.setLayout(new GridBagLayout());

        //JPanel kindofcar = new JPanel();
        //JPanel kindofcar = new JPanel();
        //JPanel kindofcar = new JPanel();
        //JPanel kindofcar = new JPanel();

        
        frame.setLocationRelativeTo(null);// center the window
        
        frame.setResizable(false);// disable window resizing
        
        frame.setVisible(true);// display window

    }

    public static void main(String[] args) {
        new NewSimulationPage();
        
    }
}
