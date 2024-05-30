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
    JPanel attributePanel = new JPanel(); // Moved this line to be an instance variable
    MouseEventHandler handler;

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
        JPanel screenPanel = new JPanel();
        JPanel roadPanel = new JPanel();

        screenPanel.setBounds(300, 20, 550, 300);
        screenPanel.setBackground(Color.GRAY);
        attributePanel.setBounds(600, 335, 250, 200);
        attributePanel.setBackground(Color.LIGHT_GRAY);

        background.add(screenPanel);
        background.add(attributePanel);

        // carPanel
        carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
        carPanel.setBackground(Color.DARK_GRAY);

        for (int i = 1; i <= 10; i++) {
            carPanel.add(Box.createVerticalStrut(15)); // Add vertical spacing

            JButton tbutton = createtrafficlightButton("No. " + i);
            tbutton.setActionCommand("trafficlightButton" + i);
            tbutton.addActionListener(this);
            handler = new MouseEventHandler(tbutton, background, screenPanel);
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
            handler = new MouseEventHandler(rbutton, background, screenPanel);
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

        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));

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

    private void updateAttributePanel(String string, String buttonId) {
        attributePanel.removeAll(); // Clear existing components

        //在這邊的panel改紅綠燈或道路需要的參數名稱(jlabel)，可能設判斷每個i要加甚麼jlabel?

        //reply: 之後我們討論一下哪個參數要訂多少 可以考慮用static final來用文字化的描述 by.昌
        for (int i = 1; i <= 5; i++) {
            JPanel linePanel = new JPanel();
            linePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("Parameter " + i + " for No." + buttonId + ": ");
            JTextField textField = new JTextField(10);
            int parameterIndex = i; // 記錄當前參數索引

            // 新增 FocusListener  如果使用者點離開parameter就會觸發 by.昌
            textField.addFocusListener(new FocusAdapter() 
            {
                @Override
                public void focusLost(FocusEvent e) {
                    System.out.println("focusLost");
                    String newValue = textField.getText();
                    updateEditorParameter(buttonId, parameterIndex, newValue);
                }
            });//匿名內部類的使用方式 酷東西

            linePanel.add(label);
            linePanel.add(textField);
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

        public MouseEventHandler(JComponent component, JPanel dragPanel, JPanel bottomPanel) {
            this.component = component;
            this.dragPanel = dragPanel;
            this.bottomPanel = bottomPanel;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint();

            // Create a semi-transparent JWindow to use as the drag image
            ghostWindow = new JWindow();
            ghostWindow.setLayout(new BorderLayout());
            ghostWindow.setSize(component.getSize());

            // Create a JPanel with a semi-transparent button appearance
            JPanel ghostPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    component.paint(g2);
                    g2.dispose();
                }
            };
            ghostPanel.setSize(component.getSize());
            ghostWindow.add(ghostPanel);
            ghostWindow.setLocation(component.getLocationOnScreen());
            ghostWindow.setVisible(true);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int xMoved = e.getXOnScreen() - initialClick.x;
            int yMoved = e.getYOnScreen() - initialClick.y;
            Point newLocation = new Point(xMoved, yMoved);
            ghostWindow.setLocation(newLocation);
        }
        
        //在這邊擷取updateAttributePanel的jtextfield然後丟到Editor就行
        @Override
        public void mouseReleased(MouseEvent e) {
            Point releasePoint = ghostWindow.getLocation();
            SwingUtilities.convertPointFromScreen(releasePoint, bottomPanel);

            if (bottomPanel.contains(releasePoint)) {
                System.out.println("Dropped at screenPanel at: " + releasePoint);
            } else {
                System.out.println("Dropped outside screenPanel");
            }
            
            ghostWindow.dispose(); // Dispose the drag image window
        }
    }

    //new function by.昌
    private void updateEditorParameter(String buttonId, int parameterIndex, String newValue) {
        if (buttonId.startsWith("r")) {//r = road
            // 處理道路參數
            editor.updateRoadParameter(buttonId, parameterIndex, newValue);
        } else if (buttonId.startsWith("t")) { //  t = traffic light 
            // 處理紅綠燈參數
            editor.updateTrafficLightParameter(buttonId, parameterIndex, newValue);
        }
    }
}