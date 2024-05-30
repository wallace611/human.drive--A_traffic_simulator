package com.HDEngine.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class InstructionPage extends JFrame {

    private JPanel bottomPanel;
    private JRadioButton okRadioButton;
    private JButton btn;
    private boolean okPressed = false;
    
    JFrame frame = new JFrame();
    JPanel background = new JPanel(new BorderLayout());
    InstructionPage() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setBackground(Color.DARK_GRAY); // 设置整个内容面板的背景色为深灰色

        okRadioButton = new JRadioButton("OK");
        okRadioButton.setBackground(Color.DARK_GRAY); // 设置按钮的背景色为深灰色
        okRadioButton.setForeground(Color.WHITE);
        okRadioButton.setFocusPainted(false);

        btn = new JButton("退出");
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        // Add action listener to okRadioButton
        okRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == okRadioButton){
                    okPressed = true;
                }
            }
        });

        // Add action listener to btn
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (okPressed) {
                    dispose(); // Only dispose the frame if "OK" was pressed before
                }
            }
        });

        // Create a JTextPane for displaying text content
        JTextPane textPane = new JTextPane();
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setForeground(Color.WHITE);
        textPane.setEditable(false); // Set the text area to be non-editable

        // Get the styled document of the text pane
        StyledDocument doc = textPane.getStyledDocument();

        // Create and add some styled text
        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.WHITE);
        
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));  // Initialize bottomPanel
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.add(okRadioButton);
        bottomPanel.add(btn);


        try {
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

            doc.insertString(doc.getLength(), "請詳閱以下使用須知，以助於使用者使用所有功能 :\n\n", normal); 
            
            doc.setParagraphAttributes(doc.getLength()-38, 38, center, false);

            doc.insertString(doc.getLength(), "*待新增，程式使用須知*\n", normal);
            doc.insertString(doc.getLength(), "\n*1*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*2*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*3*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*4*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*5*\n\n", normal);
            doc.insertString(doc.getLength(), "\n*6*\n\n", normal);
            
            doc.insertString(doc.getLength(), "\n(點擊右下角OK以退出此頁面)\n", normal);  
            
            textPane.setCaretPosition(doc.getLength());
            
            textPane.insertComponent(bottomPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add the scroll pane to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setLocationRelativeTo(null);
        setResizable(false);
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
