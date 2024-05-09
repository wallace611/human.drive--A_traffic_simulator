package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StackLayoutExample extends JFrame {
    private JPanel panel;
    private JButton button1, button2, button3;

    public StackLayoutExample() {
        setTitle("Stack Layout Example");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new StackLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        button1 = new JButton("Button 1");
        button2 = new JButton("Button 2");
        button3 = new JButton("Button 3");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.next();
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.next();
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.first();
            }
        });

        panel.add(button1, "button1");
        panel.add(button2, "button2");
        panel.add(button3, "button3");

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StackLayoutExample();
    }
}

class StackLayout implements LayoutManager {
    private Component currentComponent;

    public void addLayoutComponent(String name, Component comp) {}

    public void removeLayoutComponent(Component comp) {}

    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }

    public Dimension minimumLayoutSize(Container parent) {
        return parent.getSize();
    }

    public void layoutContainer(Container parent) {
        if (currentComponent != null) {
            currentComponent.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        }
    }

    public void first() {
        currentComponent = ((JPanel)currentComponent.getParent()).getComponent(0);
        ((JPanel)currentComponent.getParent()).validate();
        ((JPanel)currentComponent.getParent()).repaint();
    }

    public void next() {
        int index = ((JPanel)currentComponent.getParent()).getComponentZOrder(currentComponent) + 1;
        if (index < ((JPanel)currentComponent.getParent()).getComponentCount()) {
            currentComponent = ((JPanel)currentComponent.getParent()).getComponent(index);
            ((JPanel)currentComponent.getParent()).validate();
            ((JPanel)currentComponent.getParent()).repaint();
        }
    }
}