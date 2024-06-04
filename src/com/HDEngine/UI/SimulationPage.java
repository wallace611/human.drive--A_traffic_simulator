package com.HDEngine.UI;

import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Utilities.Render.RenderWindow;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.*;

public class SimulationPage extends JFrame {
    private World world;
    private RenderWindow window;
    private JFrame frame;
    private JPanel background;
    private JPanel screenPanel;
    private ResizablePanel attributePanel; // Use ResizablePanel instead of JPanel
    private JPanel topPanel;

    private JButton startButton;
    private JButton pauseButton;

    private ActionListener buttonHandler;

    // HashMap to store JTextFields
    private Map<String, JTextField> textFieldMap = new HashMap<>();

    public SimulationPage() {
        frame = new JFrame();
        frame.setTitle("Simulation Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 615);
        frame.setLocationRelativeTo(null);

        background = new JPanel(new BorderLayout());
        background.setBackground(new Color(20, 20, 20));
        frame.setContentPane(background);

        // Create and configure topPanel
        topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        background.add(topPanel, BorderLayout.CENTER);

        // Create and add transparent panels for centering
        JPanel topNorthPanel = new JPanel();
        JPanel topSouthPanel = new JPanel();
        JPanel topEastPanel = new JPanel();
        JPanel topWestPanel = new JPanel();

        topNorthPanel.setOpaque(false);
        topSouthPanel.setOpaque(false);
        topEastPanel.setOpaque(false);
        topWestPanel.setOpaque(false);

        topPanel.add(topNorthPanel, BorderLayout.NORTH);
        topPanel.add(topSouthPanel, BorderLayout.SOUTH);
        topPanel.add(topEastPanel, BorderLayout.EAST);
        topPanel.add(topWestPanel, BorderLayout.WEST);

        // Create screenPanel
        screenPanel = new JPanel(new BorderLayout());
        screenPanel.setBorder(new LineBorder(Color.BLACK, 2));
        screenPanel.setBackground(Color.GRAY);
        topPanel.add(screenPanel, BorderLayout.CENTER);

        // Create and configure resizable attributePanel
        attributePanel = new ResizablePanel();
        attributePanel.setBorder(new LineBorder(Color.BLACK, 2));
        attributePanel.setBackground(Color.GRAY);
        background.add(attributePanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Add small panels with left margin
        gbc.gridx = 0;
        gbc.gridy = 0;
        attributePanel.add(createSmallPanel("屬性欄 一‥"), gbc);

        gbc.gridx = 1;
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
        attributePanel.add(Box.createHorizontalStrut(15), gbc);

        // Add buttons to topNorthPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center buttons with 20px horizontal gap and 10px vertical gap

        startButton = createTextButton("  ▶️");
        startButton.setActionCommand("start");
        pauseButton = createTextButton("  ⏸️");
        pauseButton.setActionCommand("pause");

        startButton.addActionListener(e -> swapButton(buttonPanel, startButton, pauseButton));
        pauseButton.addActionListener(e -> swapButton(buttonPanel, pauseButton, startButton));

        JButton speedUpButton = createTextButton("  ⏩️");
        speedUpButton.setActionCommand("speedUp");
        JButton slowDownButton = createTextButton("  ⏪️");
        slowDownButton.setActionCommand("slowDown");
        JButton resetSpeedButton = createTextButton("🔄");
        resetSpeedButton.setActionCommand("resetSpeed");

        buttonPanel.add(slowDownButton);
        buttonPanel.add(startButton);
        buttonPanel.add(speedUpButton);
        buttonPanel.add(resetSpeedButton);

        buttonHandler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        };

        slowDownButton.addActionListener(buttonHandler);
        startButton.addActionListener(buttonHandler);
        pauseButton.addActionListener(buttonHandler);
        speedUpButton.addActionListener(buttonHandler);
        resetSpeedButton.addActionListener(buttonHandler);

        topNorthPanel.setLayout(new BorderLayout());
        topNorthPanel.add(buttonPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            window = new RenderWindow(world.getChildren());
            String[] processingArgs = {"window"};
            PApplet.runSketch(processingArgs, window);
            PSurfaceAWT surf = (PSurfaceAWT) window.getSurface();
            if (surf != null) {
                PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
                screenPanel.add(smoothCanvas, BorderLayout.CENTER);
                frame.revalidate();
            }
        });

        frame.setVisible(true);
    }

    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand when hovering over text
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Normal", Font.BOLD, 15));
        return button;
    }

    private void swapButton(JPanel panel, JButton oldButton, JButton newButton) {
        panel.remove(oldButton);
        panel.add(newButton, 0); // Add new button at the same position as old button
        panel.revalidate();
        panel.repaint();
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

    public JPanel createBigPanel(String s, int buttonId) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout with Y_AXIS

        // Add property lines
        for (int i = 1; i <= 5; i++) {
            JPanel linePanel = new JPanel();
            linePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            
            //label部分可以等你決定好要顯示那些參數後再調整 by.ㄐㄐ
            JLabel label = new JLabel("Parameter " + i + " for No." + buttonId + ": ");
            JTextField textField = new JTextField(4);
            textField.setEditable(false);
            textField.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));

            // Store JTextField in the map with a unique key
            //這部分看你怎樣比較方便，需要的話你也可以直接改key的設定 by.ㄐㄐ
            String key = "button" + buttonId + "_param" + i;
            textField.setName(key); // Set name for JTextField
            textFieldMap.put(key, textField);

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

    //顯示輸入數值的函式
    public void updateTextFields(Map<String, Integer> values) {
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            JTextField textField = textFieldMap.get(key);
            if (textField != null) {
                textField.setText(value);
            } else {
                System.out.println("No JTextField found for key: " + key);
            }
        }
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

    public static void main(String[] args) {
        SimulationPage simulationPage = new SimulationPage();

        // 可以照這幾個例子從你那邊輸入數值到這邊的jtextfield by.ㄐㄐ
        Map<String, Integer> intValues = new HashMap<>();
        intValues.put("button1_param3", 123);
        intValues.put("button2_param2", 456);
        simulationPage.updateTextFields(intValues);
    }

    // 可以拉動panel大小的超派類別
    class ResizablePanel extends JPanel {
        private Point initialClick;
        private int initialHeight;
        private boolean dragging = false;

        public ResizablePanel() {
            setLayout(new GridBagLayout());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isInResizeArea(e.getPoint())) {
                        initialClick = e.getLocationOnScreen();
                        initialHeight = getHeight();
                        dragging = true;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragging = false;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragging) {
                        Point current = e.getLocationOnScreen();
                        int deltaY = initialClick.y - current.y;
                        int newHeight = Math.max(50, initialHeight + deltaY);
                        setPreferredSize(new Dimension(getWidth(), newHeight));
                        revalidate();
                        repaint();
                    }
                }
            });
        }

        private boolean isInResizeArea(Point p) {
            int RESIZE_MARGIN = 10;
            return p.y <= RESIZE_MARGIN;
        }
    }
}
