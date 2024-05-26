package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class NewSimulationPage extends JFrame {

    JFrame frame = new JFrame();
    JPanel background = new JPanel(null);

    NewSimulationPage() {
        frame.setTitle("Human.Drive - Start a new simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // logo
        ImageIcon logo = new ImageIcon("human.drive--A_traffic_simulator/src/photo/newsimulation-logo.png");
        frame.setIconImage(logo.getImage());

        background.setSize(900, 600);
        background.setBackground(new Color(20, 20, 20));
        frame.setSize(background.getWidth(), background.getHeight());

        frame.add(background);

        JPanel carPanel = new JPanel();
        JPanel screenPanel = new JPanel();
        JPanel roadPanel = new JPanel();
        JPanel attributePanel = new JPanel();

        screenPanel.setBounds(300, 30, 550, 300);
        screenPanel.setBackground(Color.GRAY);
        attributePanel.setBounds(600, 345, 250, 200);
        attributePanel.setBackground(Color.LIGHT_GRAY);

        background.add(screenPanel);

        background.add(attributePanel);

        Border customized = new RoundBorder(30); // 圓角邊框，半徑為30

        screenPanel.setBorder(customized);
        attributePanel.setBorder(customized);

        // carpanel
        carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
        carPanel.setBackground(Color.DARK_GRAY);

        for (int i = 1; i <= 10; i++) {
            carPanel.add(Box.createVerticalStrut(15)); // Add vertical spacing

            JButton button = createcarButton("No. " + i);

            // For horizontally centering buttons
            JPanel wrapperPanel1 = new JPanel();
            wrapperPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
            wrapperPanel1.add(button);
            wrapperPanel1.setBackground(Color.DARK_GRAY);

            carPanel.add(wrapperPanel1);
        }

        // Place JPanel in JScrollPane
        JScrollPane carScrollPane = new JScrollPane(carPanel);
        carScrollPane.setBounds(35, 30, 250, 300);
        carScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        carScrollPane.setBorder(new LineBorder(Color.BLACK, 2));

        background.add(carScrollPane);

        // roadpanel
        roadPanel.setLayout(new GridBagLayout()); // Set layout to GridBagLayout
        roadPanel.setBackground(Color.DARK_GRAY);

        for (int i = 1; i <= 10; i++) {
            JButton button = createroadButton("No. " + i);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i - 1; // Set column index
            gbc.gridy = 0; // Set row index
            gbc.insets = new Insets(0, 15, 0, 15); // Add horizontal spacing
            gbc.anchor = GridBagConstraints.CENTER; // Center alignment
            roadPanel.add(button, gbc);
        }

        // Place JPanel in JScrollPane
        JScrollPane roadScrollPane = new JScrollPane(roadPanel);
        roadScrollPane.setBounds(35, 345, 550, 200);
        roadScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        roadScrollPane.setBorder(new LineBorder(Color.BLACK, 2));

        background.add(roadScrollPane);

        frame.setLocationRelativeTo(null); // center the window
        frame.setResizable(false); // disable window resizing
        frame.setVisible(true); // display window
    }

    // Create button template method
    public static JButton createcarButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 60));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false); // Remove the border when the button gets focus
        button.setBorder(new LineBorder(Color.BLACK, 2));

        return button;
    }

    public static JButton createroadButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 150));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false); // Remove the border when the button gets focus
        button.setBorder(new LineBorder(Color.BLACK, 2));

        return button;
    }

    public static void main(String[] args) {
        new NewSimulationPage();
    }
}


