package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

import com.HDEngine.Editor.Editor;

public class EditPage extends JFrame implements ActionListener {

    JFrame frame = new JFrame();
    JPanel background = new JPanel(null);
    JMenuBar menubar = new JMenuBar();
    JMenu backMenu = new JMenu("Back");
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Edit");
    JMenuItem loadItem = new JMenuItem("Load");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem backMenuItem = new JMenuItem("Home");
    JScrollPane attributeScrollPane;
    JPanel attributePanel = new JPanel(); // Moved this line to be an instance variable
    MouseEventHandler handler;
    JScrollPane screenScrollPane;
    JPanel screenPanel;

    private Editor editor;  //要有editor的實例才能操作editor by.昌

    EditPage() {
        frame.setTitle("Human.Drive - Start a new simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editor = new Editor();//初始化 by.昌

        // logo
        ImageIcon logo = new ImageIcon("src/photo/newsimulation-logo.png");
        frame.setIconImage(logo.getImage());

        background.setSize(900, 615);
        background.setBackground(new Color(20, 20, 20));
        frame.setSize(background.getWidth(), background.getHeight());

        frame.add(background);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        backMenu.add(backMenuItem);
        menubar.add(backMenu);
        menubar.add(fileMenu);
        menubar.add(editMenu);
        frame.setJMenuBar(menubar);

        backMenuItem.addActionListener(this);

        JPanel carPanel = new JPanel();
        JPanel roadPanel = new JPanel();

        screenPanel = new JPanel(new GridLayout(20, 20));
        screenPanel.setPreferredSize(new Dimension(1100, 1100));
        screenPanel.setBackground(Color.GRAY);
        screenScrollPane = new JScrollPane(screenPanel);
        screenScrollPane.setBounds(300, 20, 550, 300);
        screenScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        screenScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        screenScrollPane.getViewport().setBackground(Color.GRAY);

        // Fill the screenPanel with 20x20 grid of panels
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JLabel label = new JLabel("[" + row + "," + col + "]", JLabel.CENTER);
                cell.add(label, BorderLayout.CENTER);
                screenPanel.add(cell);
            }
        }

        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributeScrollPane = new JScrollPane(attributePanel);
        attributeScrollPane.setBounds(600, 335, 250, 200);
        attributeScrollPane.setBackground(Color.LIGHT_GRAY);
        attributeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        attributeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        background.add(screenScrollPane);
        background.add(attributeScrollPane);

        // carPanel
        carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
        carPanel.setBackground(Color.DARK_GRAY);

        for (int i = 1; i <= 10; i++) {
            carPanel.add(Box.createVerticalStrut(15)); // Add vertical spacing

            JButton tbutton = createtrafficlightButton("No. " + i);
            tbutton.setActionCommand("trafficlightButton" + i);
            tbutton.addActionListener(this);
            handler = new MouseEventHandler(tbutton, screenPanel, screenScrollPane);
            tbutton.addMouseListener(handler);
            tbutton.addMouseMotionListener(handler);

            // For horizontally centering buttons
            JPanel wrapperPanel1 = new JPanel();
            wrapperPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
            wrapperPanel1.add(tbutton);
            wrapperPanel1.setBackground(Color.DARK_GRAY);

            carPanel.add(wrapperPanel1);
        }

        // Place JPanel in JScrollPane
        JScrollPane carScrollPane = new JScrollPane(carPanel);
        carScrollPane.setBounds(35, 20, 250, 300);
        carScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        carScrollPane.setBorder(new LineBorder(Color.BLACK, 2));

        background.add(carScrollPane);

        // roadPanel
        roadPanel.setLayout(new GridBagLayout());
        roadPanel.setBackground(Color.DARK_GRAY);

        for (int i = 1; i <= 14; i++) {
            JButton rbutton = createroadButton("No. " + i);
            rbutton.setActionCommand("roadButton" + i);
            rbutton.addActionListener(this);
            handler = new MouseEventHandler(rbutton, screenPanel, screenScrollPane);
            rbutton.addMouseListener(handler);
            rbutton.addMouseMotionListener(handler);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i - 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 15, 0, 15);
            gbc.anchor = GridBagConstraints.CENTER;
            roadPanel.add(rbutton, gbc);
        }

        // Place JPanel in JScrollPane
        JScrollPane roadScrollPane = new JScrollPane(roadPanel);
        roadScrollPane.setBounds(35, 335, 550, 200);
        roadScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        roadScrollPane.setBorder(new LineBorder(Color.BLACK, 2));

        background.add(roadScrollPane);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // Create button template method
    public static JButton createtrafficlightButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 60));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 2));

        return button;
    }

    public static JButton createroadButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 150));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 2));

        return button;
    }

    public static void main(String[] args) {
        new EditPage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backMenuItem) {
            frame.dispose();
            new StartPage(); // Uncomment and define StartPage class if needed
        } else if (e.getActionCommand().startsWith("roadButton")) {
            // Handle road button clicks
            String buttonId = e.getActionCommand().substring(10); // Get button id
            System.out.println("Road button " + buttonId + " clicked!");

            // Update attributePanel with new JTextFields
            updateAttributePanel("r", buttonId);
        } else if (e.getActionCommand().startsWith("trafficlightButton")) {
            // Handle trafficlight button clicks
            String buttonId = e.getActionCommand().substring(18); // Get button id
            System.out.println("trafficlight button " + buttonId + " clicked!");

            // Update attributePanel with new JTextFields
            updateAttributePanel("t", buttonId);
        }
    }

    private void updateAttributePanel(String type, String buttonId) {
        attributePanel.removeAll(); // Clear existing components

        for (int i = 1; i <= 10; i++) {
            JPanel linePanel = new JPanel();
            linePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            if (i == 2) {
                JLabel label1 = new JLabel("Traffic Light Flag:");
                JCheckBox trueCheckBox = new JCheckBox("True");
                JCheckBox falseCheckBox = new JCheckBox("False");
                ButtonGroup group = new ButtonGroup();
                group.add(trueCheckBox);
                group.add(falseCheckBox);

                trueCheckBox.addActionListener(e -> {
                    if (trueCheckBox.isSelected()) {
                        editor.updateTrafficLightParameter(buttonId, 2, "true");
                    }
                });

                falseCheckBox.addActionListener(e -> {
                    if (falseCheckBox.isSelected()) {
                        editor.updateTrafficLightParameter(buttonId, 2, "false");
                    }
                });

                linePanel.add(label1);
                linePanel.add(trueCheckBox);
                linePanel.add(falseCheckBox);
            } else {
                JLabel label;
                switch (i) {
                    case 1:
                        label = new JLabel("Set your traffic light flag: ");
                        break;
                    case 3:
                        label = new JLabel("Set your traffic light timer: ");
                        break;
                    case 4:
                        label = new JLabel("Set your traffic light group: ");
                        break;
                    case 5:
                        label = new JLabel("Set your speed limit: ");
                        break;
                    
                    default:
                        label = new JLabel("Unknown Parameter for No." + buttonId + ": ");
                        break;
                }
                JTextField textField = new JTextField(10);
                int parameterIndex = i; // 記錄當前參數索引

                // 新增 FocusListener 如果使用者點離開parameter就會觸發 by.昌
                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        System.out.println("focusLost");
                        String newValue = textField.getText();
                        updateEditorParameter(buttonId, parameterIndex, newValue);
                    }
                });

                linePanel.add(label);
                linePanel.add(textField);
            }
            attributePanel.add(linePanel);
            attributePanel.add(Box.createVerticalStrut(10)); // Add spacing between fields
        }

        // Refresh the panel
        attributePanel.revalidate();
        attributePanel.repaint();
    }

    class MouseEventHandler extends MouseAdapter {
        JComponent component;
        JPanel dragPanel;
        JPanel bottomPanel;
        private Point initialClick;
        private JWindow ghostWindow;
        private JScrollPane screenScrollPane;
        private Timer scrollTimer;

        public MouseEventHandler(JComponent component, JPanel dragPanel, JScrollPane screenScrollPane) {
            this.component = component;
            this.dragPanel = dragPanel;
            this.screenScrollPane = screenScrollPane;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint();

            // Create a semi-transparent JWindow to use as the drag image
            ghostWindow = new JWindow();
            ghostWindow.setLayout(new BorderLayout());

            // Reduce the size of the ghost window to half of the original component size
            int width = component.getWidth() / 2;
            int height = component.getHeight() / 2;
            ghostWindow.setSize(width, height);

            // Create a JPanel with a semi-transparent button appearance
            JPanel ghostPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.scale(0.5, 0.5); // Scale down the component by 50%
                    component.paint(g2);
                    g2.dispose();
                }
            };
            ghostPanel.setSize(width, height);
            ghostWindow.add(ghostPanel);

            // Adjust the ghostWindow location to match the mouse press location
            Point locationOnScreen = e.getLocationOnScreen();
            ghostWindow.setLocation(locationOnScreen.x - (initialClick.x / 2), locationOnScreen.y - (initialClick.y / 2));
            ghostWindow.setVisible(true);

            startScrollTimer();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point locationOnScreen = e.getLocationOnScreen();
            ghostWindow.setLocation(locationOnScreen.x - (initialClick.x / 2), locationOnScreen.y - (initialClick.y / 2));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point releasePoint = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(releasePoint, dragPanel);

            if (dragPanel.contains(releasePoint)) {
                // Find the corresponding panel
                int cellWidth = screenPanel.getWidth() / 20;
                int cellHeight = screenPanel.getHeight() / 20;
                int col = releasePoint.x / cellWidth;
                int row = releasePoint.y / cellHeight;

                int index = row * 20 + col;
                if (index >= 0 && index < screenPanel.getComponentCount()) {
                    JPanel cell = (JPanel) screenPanel.getComponent(index);
                    cell.removeAll();
                    cell.add(component);
                    cell.revalidate();
                    cell.repaint();
                }

                System.out.println("Dropped at cell: [" + row + "," + col + "]");
            } else {
                System.out.println("Dropped outside screenPanel");
            }

            ghostWindow.dispose(); // Dispose the drag image window
            stopScrollTimer();
        }
        //感應滑鼠來控制滾輪
        private void startScrollTimer() {
            scrollTimer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(mousePosition, screenScrollPane);

                    JScrollBar verticalScrollBar = screenScrollPane.getVerticalScrollBar();
                    JScrollBar horizontalScrollBar = screenScrollPane.getHorizontalScrollBar();
                    int scrollSpeed = 20;
                    int edgeThreshold = 50; // 調整邊緣檢測範圍

                    if (mousePosition.y <= edgeThreshold) {
                        verticalScrollBar.setValue(verticalScrollBar.getValue() - scrollSpeed);
                    } else if (mousePosition.y >= screenScrollPane.getHeight() - edgeThreshold) {
                        verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollSpeed);
                    }

                    if (mousePosition.x <= edgeThreshold) {
                        horizontalScrollBar.setValue(horizontalScrollBar.getValue() - scrollSpeed);
                    } else if (mousePosition.x >= screenScrollPane.getWidth() - edgeThreshold) {
                        horizontalScrollBar.setValue(horizontalScrollBar.getValue() + scrollSpeed);
                    }
                }
            });
            scrollTimer.start();
        }

        private void stopScrollTimer() {
            if (scrollTimer != null) {
                scrollTimer.stop();
            }
        }
    }

    // new function by.昌
    private void updateEditorParameter(String buttonId, int parameterIndex, String newValue) {
        if (buttonId.startsWith("r")) {// r = road
            // 處理道路參數
            editor.updateRoadParameter(buttonId, parameterIndex, newValue);
        } else if (buttonId.startsWith("t")) { // t = traffic light
            // 處理紅綠燈參數
            editor.updateTrafficLightParameter(buttonId, parameterIndex, newValue);
        }
    }
}