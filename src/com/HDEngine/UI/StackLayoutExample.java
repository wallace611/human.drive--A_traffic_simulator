package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;

public class StackLayoutExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Scrollable Button Panel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 400);

            // 创建一个 JPanel 并设置 BoxLayout 以实现垂直排列
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

            // 添加按钮，并设置按钮之间的垂直间隔为 10 像素
            for (int i = 1; i <= 20; i++) {
                JButton button = createButtonTemplate("Button " + i);
                
                // 创建一个子 JPanel，用于水平居中按钮
                JPanel wrapperPanel = new JPanel();
                wrapperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                wrapperPanel.add(button);
                
                buttonPanel.add(wrapperPanel);
                buttonPanel.add(Box.createVerticalStrut(10));  // 添加垂直间隔
            }

            // 将最后一个间隔移除以使布局更美观
            buttonPanel.remove(buttonPanel.getComponentCount() - 1);

            // 将 JPanel 放置在 JScrollPane 中
            JScrollPane scrollPane = new JScrollPane(buttonPanel);
            //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            // 将 JScrollPane 添加到 JFrame 中
            frame.getContentPane().add(scrollPane);

            frame.setVisible(true);
        });
    }

    // 创建按钮模板方法
    public static JButton createButtonTemplate(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // 去除按钮获取焦点时的边框

        return button;
    }
}

