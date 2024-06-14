package com.HDEngine.UI;

import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Simulator.Settings.Settings;
import com.HDEngine.Simulator.Components.RenderWindow;
import com.HDEngine.Simulator.Simulator;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.*;

import static java.lang.Math.*;

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
        frame.setSize(Settings.uiWidth, Settings.uiHeight);
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
        screenPanel.setFocusable(true);
        topPanel.add(screenPanel, BorderLayout.CENTER);

        // Create and configure resizable attributePanel
        attributePanel = new ResizablePanel();
        attributePanel.setBorder(new LineBorder(Color.BLACK, 2));
        attributePanel.setBackground(Color.GRAY);
        background.add(attributePanel, BorderLayout.SOUTH);

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
        JButton debugButton = createTextButton("\uD83D\uDC1B");
        debugButton.setActionCommand("debug");
        JButton loadFileButton = createTextButton("load file");
        loadFileButton.setActionCommand("loadFile");

        buttonPanel.add(startButton);
        buttonPanel.add(slowDownButton);
        buttonPanel.add(speedUpButton);
        buttonPanel.add(resetSpeedButton);
        buttonPanel.add(debugButton);
        buttonPanel.add(loadFileButton);

        buttonHandler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                switch (cmd) {
                    case "start" -> Settings.running = true;
                    case "pause" -> Settings.running = false;
                    case "speedUp" -> Settings.speed = min(1.2f * Settings.speed, 5.0f);
                    case "slowDown" -> Settings.speed = max(Settings.speed / 1.2f, 0.2f);
                    case "resetSpeed" -> Settings.speed = 1.0f;
                    case "debug" -> Settings.debugMode = !Settings.debugMode;
                    case "loadFile" -> Simulator.loadFileInBackground();
                }
            }
        };

        slowDownButton.addActionListener(buttonHandler);
        startButton.addActionListener(buttonHandler);
        pauseButton.addActionListener(buttonHandler);
        speedUpButton.addActionListener(buttonHandler);
        resetSpeedButton.addActionListener(buttonHandler);
        debugButton.addActionListener(buttonHandler);
        loadFileButton.addActionListener(buttonHandler);

        topNorthPanel.setLayout(new BorderLayout());
        topNorthPanel.add(buttonPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            window = new RenderWindow();
            String[] processingArgs = {"window"};
            PApplet.runSketch(processingArgs, window);
            PSurfaceAWT surf = (PSurfaceAWT) window.getSurface();
            if (surf != null) {
                PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
                screenPanel.add(smoothCanvas, BorderLayout.CENTER);
                frame.revalidate();

                // 添加 ComponentListener 來監聽 screenPanel 的大小變化
                screenPanel.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        // 當 screenPanel 大小改變時，更新 RenderWindow 的大小
                        window.setWindowWidth(screenPanel.getWidth());
                        window.setWindowHeight(screenPanel.getHeight());
                    }
                });
            }
        });

        frame.setVisible(true);
        Settings.windowWidth = screenPanel.getWidth();
        Settings.windowHeight = screenPanel.getHeight();
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

    public RenderWindow getWindow() {
        return window;
    }

    public static void main(String[] args) {
        SimulationPage simulationPage = new SimulationPage();

    }

    // 可以拉動panel大小的超派類別
    static class ResizablePanel extends JPanel {
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
                        int newHeight = max(50, initialHeight + deltaY);
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
