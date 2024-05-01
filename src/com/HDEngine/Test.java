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
        frame.setSize(600, 400);
        frame.setLocation(0, 0);
        input = new InputManager();
        frame.addKeyListener(input);
        frame.setVisible(true);
        label2 = new JLabel("good");
        label2.setLocation(0, 0);
        frame.add(label2);
    }

    public void run() throws InterruptedException {
        Vehicle a = new Vehicle(100, 30, 10, 10, 10);
        while (true) {
            Thread.sleep(5);
            System.out.println(a.getLocation());

            if (input.getPressedKey()['W']) a.setCurrentForwardState(1);
            else if (input.getPressedKey()['S']) a.setCurrentForwardState(2);
            else a.setCurrentForwardState(0);

            if (input.getPressedKey()['A']) a.setCurrentTurningState(1);
            else if (input.getPressedKey()['D']) a.setCurrentTurningState(2);
            else a.setCurrentTurningState(0);

            System.out.println(a.getCurrentForwardState());
            System.out.println(a.getCurrentTurningState());
            a.tick(0.005);
            label2.setLocation((int) a.getLocation().x, (int) a.getLocation().y);
        }
    }
}
