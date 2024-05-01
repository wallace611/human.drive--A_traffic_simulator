package com.HDEngine;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Utilities.InputManager;

import javax.swing.*;

public class Test {
    JFrame frame;
    InputManager input;
    JLabel label1;
    JLabel label2;

    public static void main(String[] args) throws InterruptedException {
        Test a = new Test();
        a.run();
    }

    public Test() {
        frame = new JFrame("AWTDemo");
        frame.setSize(1000, 1000);
        frame.setLocation(0, 0);
        input = new InputManager();
        frame.addKeyListener(input);
        frame.setVisible(true);
        label2 = new JLabel("good");
        label2.setLocation(0, 0);
        frame.add(label2);
    }

    public void run() throws InterruptedException {
        Vehicle a = new Vehicle(100, 10, 10, 10);
        while (true) {
            Thread.sleep(5);
            System.out.println(a.getLocation());

            int count = 0;
            for (boolean b : input.getPressedKey()) {
                if (b) count += 1;
            }
            a.setCurrentState(count);
            switch (count) {
                case 0, 1, 2:
                    a.setCurrentState(count);
                    break;
                case 3:
                    a.setRotation(a.getRotation() + 1.0f);
                    break;
                case 4:
                    a.setRotation(a.getRotation() - 1.0f);
                    break;
            }
            System.out.println(a.getCurrentState());
            a.tick(0.005);
            label2.setLocation((int) a.getLocation().x, (int) a.getLocation().y);
        }
    }
}
