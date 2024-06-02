package com.HDEngine.UI;

import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Utilities.Render.RenderWindow;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class SimulationPage extends JFrame {
    private World world;
    private RenderWindow window;
    private JFrame frame;
    private JPanel background;
    private JPanel screenPanel;
    private JPanel attributePanel;

    public SimulationPage() {
        frame = new JFrame();
        frame.setTitle("Simulation Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 615);
        frame.setLocationRelativeTo(null);

        background = new JPanel();
        background.setBackground(new Color(20, 20, 20));
        background.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        background.setLayout(null);

        screenPanel = new JPanel();
        screenPanel.setBounds(40, 30, 810, 320);
        screenPanel.setBorder(new LineBorder(Color.BLACK, 2));
        screenPanel.setBackground(Color.GRAY);
        background.add(screenPanel);

        attributePanel = new JPanel();
        attributePanel.setBounds(40, 365, 810, 200);
        attributePanel.setBorder(new LineBorder(Color.BLACK, 2));
        attributePanel.setBackground(Color.GRAY);
        attributePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Add small panels with left margin
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 15, 0, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        attributePanel.add(createSmallPanel("屬性欄 一‥"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 15);
        attributePanel.add(createSmallPanel("屬性欄 二‥"), gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        attributePanel.add(createSmallPanel("屬性欄 三‥"), gbc);

        // Add spacer to push the big panels to the right
        gbc.gridx = 3;
        gbc.weightx = 1.0; // This will push the big panels to the right
        attributePanel.add(Box.createHorizontalGlue(), gbc);

        // Add big panels
        gbc.gridx = 4;
        gbc.weightx = 0.0; // Reset weightx
        gbc.insets = new Insets(0, 0, 0, 15);
        gbc.fill = GridBagConstraints.BOTH; // Ensure that BigPanel fills available space
        attributePanel.add(createBigPanel("屬性欄 四:", 1), gbc);

        gbc.gridx = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        attributePanel.add(createBigPanel("屬性欄 五:", 2), gbc);

        // Add final strut to ensure right margin
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        attributePanel.add(Box.createHorizontalStrut(15), gbc);

        background.add(attributePanel);

        frame.add(background);
        //frame.setResizable(false);

        SwingUtilities.invokeLater(() -> {
            window = new RenderWindow(world.getChildren());
            String[] processingArgs = {"window"};
            PApplet.runSketch(processingArgs, window);
            PSurfaceAWT surf = (PSurfaceAWT) window.getSurface();
            if (surf != null) {
                PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
                screenPanel.add(smoothCanvas);
                frame.revalidate();
            }
        });

        frame.setVisible(true);
    }

    public static JPanel createSmallPanel(String s) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 170));
        panel.setMinimumSize(new Dimension(100, 170));
        panel.setMaximumSize(new Dimension(100, 170));
        panel.setBorder(new LineBorder(Color.BLACK, 2));
        panel.setLayout(new BorderLayout());

        // Create a label with vertical text
        StringBuilder verticalText = new StringBuilder("<html>");
        for (char c : s.toCharArray()) {
            verticalText.append(c).append("<br>");
        }
        verticalText.append("</html>");
        JLabel label = new JLabel(verticalText.toString());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        // Add a JTextField at the bottom
        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
        panel.add(textField, BorderLayout.SOUTH);
        return panel;
    }

    public static JPanel createBigPanel(String s, int buttonId) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS

        // Add property lines
        for (int i = 1; i <= 5; i++) {
            JPanel linePanel = new JPanel();
            linePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("Parameter " + i + " for No." + buttonId + ": ");
            JTextField textField = new JTextField(4);
            textField.setEditable(false);
            textField.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
            linePanel.add(label);
            linePanel.add(textField);
            panel.add(linePanel);
            panel.add(Box.createVerticalStrut(10)); // Add vertical spacing between fields
        }

        scrollPane.setViewportView(panel);
        scrollPane.setPreferredSize(new Dimension(200, 180));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outerPanel.add(new JLabel(s), BorderLayout.NORTH);
        outerPanel.add(scrollPane, BorderLayout.CENTER);
        outerPanel.setPreferredSize(new Dimension(200, 180));

        return outerPanel; // Return the outer panel
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public RenderWindow getWindow() {
        return window;
    }
}
