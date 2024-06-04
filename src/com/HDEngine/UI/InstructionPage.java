package com.HDEngine.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class InstructionPage extends JFrame {

    private JButton btn;

    public InstructionPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setBackground(Color.DARK_GRAY);

        btn = new JButton("退出");
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        // Add action listener to btn
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Create a JTextPane for displaying text content
        JTextPane textPane = new JTextPane();
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setForeground(Color.WHITE);
        textPane.setEditable(false);

        // Get the styled document of the text pane
        StyledDocument doc = textPane.getStyledDocument();

        // Create and add some styled text
        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.WHITE);

        try {
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

            doc.insertString(doc.getLength(), "請詳閱以下使用須知，以助於使用者使用所有功能 :\n\n", normal);
            doc.setParagraphAttributes(doc.getLength() - 38, 38, center, false);

            doc.insertString(doc.getLength(), "*待新增，程式使用須知*\n", normal);
            doc.insertString(doc.getLength(), "\n*1*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*2*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*3*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*4*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*5*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*6*\n\n", normal);
            doc.insertString(doc.getLength(), "\n(點擊右下角按鈕以退出此頁面)\n", normal);

            textPane.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the button to the text pane
        textPane.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.add(btn, BorderLayout.EAST);
        textPane.add(panel, BorderLayout.SOUTH);

        // Create a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JViewport viewport = scrollPane.getViewport();
                viewport.setViewPosition(new Point(0, 0));
            }
        });
    }

    public static void main(String[] args) {
        new InstructionPage();
    }
}
