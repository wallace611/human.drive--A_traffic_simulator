package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
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

    // 保存状态的变量
    private Map<String, Component[]> savedAttributePanelComponents = new HashMap<>();
    private Map<String, String> savedValues = new HashMap<>();
    private Map<String, String[]> savedIntersectionStates = new HashMap<>();
    private Map<String, String[]> savedWeightStates = new HashMap<>();

    private int trafficLightButtonCount = 0; // To keep track of the number of traffic light buttons added

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

        JPanel trafficlightPanel = new JPanel();
        JPanel roadPanel = new JPanel();

        screenPanel = new JPanel(new GridLayout(20, 20));
        screenPanel.setPreferredSize(new Dimension(1100, 1100)); // 设置首选大小
        screenPanel.setBackground(Color.GRAY); // 设置背景颜色为灰色
        screenScrollPane = new JScrollPane(screenPanel);
        screenScrollPane.setBounds(300, 20, 550, 300);
        screenScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        screenScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        screenScrollPane.getViewport().setBackground(Color.GRAY);

        // Fill the screenPanel with 20x20 grid of panels
        for (int row = 1; row < 21; row++) {
            for (int col = 1; col < 21; col++) {
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

        // trafficlightPanel
        trafficlightPanel.setLayout(new BoxLayout(trafficlightPanel, BoxLayout.Y_AXIS));
        trafficlightPanel.setBackground(Color.DARK_GRAY);

        // Add "Add" button at the top left
        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(50, 30));
        addButton.setActionCommand("addTrafficLightButton");
        addButton.addActionListener(this);
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(Color.LIGHT_GRAY);
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        addButton.setBorder(new LineBorder(Color.BLACK, 2));
        addPanel.add(addButton);
        addPanel.setBackground(Color.DARK_GRAY);
        trafficlightPanel.add(addPanel);

        // Place JPanel in JScrollPane
        JScrollPane trafficLightScrollPane = new JScrollPane(trafficlightPanel);
        trafficLightScrollPane.setBounds(35, 20, 250, 300);
        trafficLightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        trafficLightScrollPane.setBorder(new LineBorder(Color.BLACK, 2));

        background.add(trafficLightScrollPane);

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
            System.out.println("Traffic light button " + buttonId + " clicked!");

            // Update attributePanel with new JTextFields
            updateAttributePanel("t", buttonId);
        } else if (e.getActionCommand().equals("addTrafficLightButton")) {
            // Handle add button click
            addTrafficLightButton((JPanel)((JButton)e.getSource()).getParent().getParent());
        }
    }

    private void updateAttributePanel(String type, String buttonId) {
        attributePanel.removeAll(); // Clear existing components

        JPanel linePanel1 = createRightAlignedPanel(new JLabel("Set your Traffic Light Flag:"), createCheckBoxPanel(buttonId, 1));
        attributePanel.add(linePanel1);

        JPanel linePanel2 = createRightAlignedPanel(new JLabel("Set your Traffic Light Timer:"), createTextField(buttonId, 2));
        attributePanel.add(linePanel2);

        JPanel linePanel3 = createRightAlignedPanel(new JLabel("Set your Traffic Light Group:"), createTextField(buttonId, 3));
        attributePanel.add(linePanel3);

        JPanel linePanel4 = createRightAlignedPanel(new JLabel("Set your Speed Limit:"), createTextField(buttonId, 4));
        attributePanel.add(linePanel4);

        JPanel linePanel5 = createRightAlignedPanel(new JLabel("Set your Start Flag:"), createCheckBoxPanel(buttonId, 5));
        attributePanel.add(linePanel5);

        JPanel linePanel6 = createRightAlignedPanel(new JLabel("Set your Intersection:"), createButton("Set", e -> showIntersectionPanel(buttonId)));
        attributePanel.add(linePanel6);

        JPanel linePanel7 = createRightAlignedPanel(new JLabel("Set your Weights:"), createButton("Set", e -> showWeightsPanel(buttonId)));
        attributePanel.add(linePanel7);

        // Refresh the panel
        attributePanel.revalidate();
        attributeScrollPane.setViewportView(attributePanel);
        attributeScrollPane.revalidate();
        attributeScrollPane.repaint();
    }

    private JPanel createRightAlignedPanel(JLabel label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel componentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        labelPanel.add(label);
        componentPanel.add(component);
        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(componentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCheckBoxPanel(String buttonId, int parameterIndex) {
        JCheckBox trueCheckBox = new JCheckBox("True");
        trueCheckBox.setFocusPainted(false);
        JCheckBox falseCheckBox = new JCheckBox("False");
        falseCheckBox.setFocusPainted(false);
        ButtonGroup group = new ButtonGroup();
        group.add(trueCheckBox);
        group.add(falseCheckBox);
        trueCheckBox.addActionListener(e -> {
            if (trueCheckBox.isSelected()) {
                editor.updateTrafficLightParameter(buttonId, parameterIndex, "true");
            }
        });
        falseCheckBox.addActionListener(e -> {
            if (falseCheckBox.isSelected()) {
                editor.updateTrafficLightParameter(buttonId, parameterIndex, "false");
            }
        });
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkBoxPanel.add(trueCheckBox);
        checkBoxPanel.add(falseCheckBox);
        return checkBoxPanel;
    }

    private JTextField createTextField(String buttonId, int parameterIndex) {
        JTextField textField = new JTextField(10);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newValue = textField.getText();
                editor.updateTrafficLightParameter(buttonId, parameterIndex, newValue);
            }
        });
        return textField;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setFocusPainted(false);
        return button;
    }

    private void showIntersectionPanel(String buttonId) {
        saveCurrentAttributePanelState(buttonId);

        attributePanel.removeAll(); // Clear existing components

        attributePanel.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        JCheckBox[] checkboxes = new JCheckBox[8];
        String[] directions = {"North-West", "North", "North-East", "West", "East", "South-West", "South", "South-East"};

        for (int i = 0; i < 9; i++) {
            JPanel cell = new JPanel(new BorderLayout());
            if (i == 4) {
                cell.setBackground(Color.GRAY);
                JLabel label = new JLabel("Button ID: " + buttonId, JLabel.CENTER);
                cell.add(label, BorderLayout.CENTER);
            } else {
                checkboxes[i < 4 ? i : i - 1] = new JCheckBox(directions[i < 4 ? i : i - 1]);
                cell.add(checkboxes[i < 4 ? i : i - 1], BorderLayout.CENTER);
            }
            gridPanel.add(cell);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            saveIntersectionStates(buttonId, checkboxes);
            restorePreviousAttributePanelState(buttonId);
        });
        bottomPanel.add(backButton);

        attributePanel.add(gridPanel, BorderLayout.CENTER);
        attributePanel.add(bottomPanel, BorderLayout.SOUTH);

        // Refresh the panel
        attributePanel.revalidate();
        attributeScrollPane.setViewportView(attributePanel);
        attributeScrollPane.revalidate();
        attributeScrollPane.repaint();

        // Restore previous states if available
        restoreIntersectionStates(buttonId, checkboxes);
    }

    private void showWeightsPanel(String buttonId) {
        saveCurrentAttributePanelState(buttonId);

        attributePanel.removeAll(); // Clear existing components

        attributePanel.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        JTextField[] textFields = new JTextField[8];
        String[] directions = {"North-West", "North", "North-East", "West", "East", "South-West", "South", "South-East"};

        for (int i = 0; i < 9; i++) {
            JPanel cell = new JPanel(new BorderLayout());
            if (i == 4) {
                cell.setBackground(Color.GRAY);
                JLabel label = new JLabel("Button ID: " + buttonId, JLabel.CENTER);
                label.setHorizontalAlignment(SwingConstants.CENTER); // Center the label
                cell.add(label, BorderLayout.CENTER);
            } else {
                JPanel innerPanel = new JPanel(new BorderLayout());
                JLabel directionLabel = new JLabel(directions[i < 4 ? i : i - 1]);
                directionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                textFields[i < 4 ? i : i - 1] = new JTextField(5);
                innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
                innerPanel.add(directionLabel);
                innerPanel.add(textFields[i < 4 ? i : i - 1]);
                cell.add(innerPanel, BorderLayout.CENTER);
            }
            gridPanel.add(cell);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            saveWeightStates(buttonId, textFields);
            restorePreviousAttributePanelState(buttonId);
        });
        bottomPanel.add(backButton);

        attributePanel.add(gridPanel, BorderLayout.CENTER);
        attributePanel.add(bottomPanel, BorderLayout.SOUTH);

        // Refresh the panel
        attributePanel.revalidate();
        attributeScrollPane.setViewportView(attributePanel);
        attributeScrollPane.revalidate();
        attributeScrollPane.repaint();

        // Restore previous states if available
        restoreWeightStates(buttonId, textFields);
    }

    private void saveCurrentAttributePanelState(String buttonId) {
        
        savedAttributePanelComponents.put(buttonId, attributePanel.getComponents());
        for (Component component : attributePanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component subComponent : panel.getComponents()) {
                    if (subComponent instanceof JTextField) {
                        JTextField textField = (JTextField) subComponent;
                        savedValues.put(buttonId + textField.hashCode(), textField.getText());
                    }
                }
            }
        }
    }

    private void restorePreviousAttributePanelState(String buttonId) {
        
        attributePanel.removeAll();

        Component[] components = savedAttributePanelComponents.get(buttonId);
        if (components != null) {
            for (Component component : components) {
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    for (Component subComponent : panel.getComponents()) {
                        if (subComponent instanceof JTextField) {
                            JTextField textField = (JTextField) subComponent;
                            textField.setText(savedValues.get(buttonId + textField.hashCode()));
                        }
                    }
                }
                attributePanel.add(component);
            }
        }

        attributePanel.revalidate();
        attributeScrollPane.setViewportView(attributePanel);
        attributeScrollPane.revalidate();
        attributeScrollPane.repaint();
    }

    private void saveIntersectionStates(String buttonId, JCheckBox[] checkboxes) {
        String[] states = new String[8];
        for (int i = 0; i < checkboxes.length; i++) {
            states[i] = checkboxes[i].isSelected() ? "1" : "0";
        }
        savedIntersectionStates.put(buttonId, states);
    }

    private void restoreIntersectionStates(String buttonId, JCheckBox[] checkboxes) {
        String[] states = savedIntersectionStates.get(buttonId);
        if (states != null) {
            for (int i = 0; i < states.length; i++) {
                checkboxes[i].setSelected("1".equals(states[i]));
            }
        }
    }

    private void saveWeightStates(String buttonId, JTextField[] textFields) {
        String[] states = new String[8];
        for (int i = 0; i < textFields.length; i++) {
            states[i] = textFields[i].getText();
        }
        savedWeightStates.put(buttonId, states);
    }

    private void restoreWeightStates(String buttonId, JTextField[] textFields) {
        String[] states = savedWeightStates.get(buttonId);
        if (states != null) {
            for (int i = 0; i < states.length; i++) {
                textFields[i].setText(states[i]);
            }
        }
    }

    private void addTrafficLightButton(JPanel trafficlightPanel) {
        trafficLightButtonCount++;
        JButton tbutton = createtrafficlightButton("No. " + trafficLightButtonCount);
        tbutton.setActionCommand("trafficlightButton" + trafficLightButtonCount);
        tbutton.addActionListener(this);
        handler = new MouseEventHandler(tbutton, screenPanel, screenScrollPane);
        tbutton.addMouseListener(handler);
        tbutton.addMouseMotionListener(handler);

        // For horizontally centering buttons
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.add(tbutton);
        wrapperPanel.setBackground(Color.DARK_GRAY);

        trafficlightPanel.add(wrapperPanel);
        trafficlightPanel.revalidate();
        trafficlightPanel.repaint();
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

        private void startScrollTimer() {
            scrollTimer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(mousePosition, screenScrollPane);

                    JScrollBar verticalScrollBar = screenScrollPane.getVerticalScrollBar();
                    JScrollBar horizontalScrollBar = screenScrollPane.getHorizontalScrollBar();
                    int scrollSpeed = 20;
                    int edgeThreshold = 50; // 调整边缘检测的范围

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
