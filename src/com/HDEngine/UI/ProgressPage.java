package com.HDEngine.UI;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressPage extends JFrame{ //跑條程式
    
    private JFrame frame;
    private JProgressBar bar;
    
    public ProgressPage(){
        
        frame = new JFrame();
        bar = new JProgressBar();
        
        bar.setValue(0);
        bar.setBounds(0,0,420,50);
        bar.setStringPainted(true);
        //bar.setForeground(Color.GRAY);

        frame.add(bar);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(bar.getWidth(), bar.getHeight());
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        fill();
    }

    public static void main(String[] args) {
        new ProgressPage();

    }

    public void fill(){
        int counter = 0;
        while(counter<=100){
            bar.setValue(counter);
            try{
                Thread.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            counter+=1;
        }
        bar.setString("Done...");
        try{
            Thread.sleep(500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        frame.dispose();
    }
}
