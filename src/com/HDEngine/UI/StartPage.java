import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;

public class StartPage extends JFrame implements ActionListener{  

    
    Image backgroundimg;
    JPanel imagePanel = null;
    JButton startbtn = new JButton("開始編輯");
    JButton archivebtn = new JButton("先前存檔");
    JButton instructionbtn = new JButton("使用說明");
    JButton quitbtn = new JButton("離開");

    StartPage() {
        setTitle("Human.Drive");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        // logo
        ImageIcon logo = new ImageIcon("src/photo/human.drive-logo.png");
        setIconImage(logo.getImage());

        // setting window zise
        ImageIcon backgroundImageIcon = new ImageIcon("src/photo/startpage.png");
        setSize(backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());

        // 创建 JLabel 以承载背景图片
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(backgroundImageIcon);

        // 设置 JLabel 的大小和位置，以填充整个 JFrame
        backgroundLabel.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());

        imagePanel = (JPanel) getContentPane();
        imagePanel.setOpaque(false);
        getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));
        
        // adding background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setPreferredSize(new Dimension(getWidth(), getHeight()));
        backgroundPanel.setOpaque(false);
        //backgroundPanel.setBackground(Color.PINK);
        add(backgroundPanel);
        
        backgroundPanel.setLayout(new BorderLayout());
        
        // adding subpanels
        JPanel areaofbtn = new JPanel();
        JPanel bound1 = new JPanel();
        JPanel bound2 = new JPanel();
        JPanel bound3 = new JPanel();
        bound1.setPreferredSize(new Dimension(getWidth()-100, 150));
        bound2.setPreferredSize(new Dimension(10, 10));
        bound3.setPreferredSize(new Dimension(10, 10));
        
        bound1.setOpaque(false);
        bound2.setOpaque(false);
        bound3.setOpaque(false);
        areaofbtn.setOpaque(false);
        backgroundPanel.add(bound1, BorderLayout.NORTH);
        backgroundPanel.add(bound2, BorderLayout.EAST);
        backgroundPanel.add(bound3, BorderLayout.WEST);
        backgroundPanel.add(areaofbtn, BorderLayout.CENTER);

        // area of name in the program
        bound1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
        JPanel transparency = new JPanel();
        transparency.setBackground(new Color(255, 255, 255, 65));
        transparency.setPreferredSize(new Dimension(300, 80));
        bound1.add(transparency);
        transparency.setLayout(new BorderLayout());

        JLabel HumandotDrive = new JLabel("Human.Drive");
        HumandotDrive.setFont(new Font("Impact", Font.BOLD, 45));
        HumandotDrive.setForeground(Color.WHITE);
        HumandotDrive.setHorizontalAlignment(JLabel.CENTER);
        HumandotDrive.setVerticalAlignment(JLabel.CENTER);

        transparency.add(HumandotDrive, BorderLayout.CENTER);

        Font font1 = new Font("微軟正黑體", Font.BOLD, 25); // create font
        Border roundedBorder = new RoundBorder(15); // create a rounded border
        areaofbtn.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // add buttons
        Border customizedbtn = new RoundBorder(15);//custom rounded border
    
        startbtn.setFocusable(false);
        startbtn.setPreferredSize(new Dimension(330, 60));
        startbtn.setBorder(customizedbtn);
        startbtn.setBackground(Color.DARK_GRAY);
        startbtn.setForeground(Color.WHITE);
        startbtn.setFocusPainted(false); // remove stroke effect from button text
        startbtn.setFont(font1);
        areaofbtn.add(startbtn);
        startbtn.addActionListener(this);

        archivebtn.setFocusable(false);
        archivebtn.setPreferredSize(new Dimension(330, 60));
        archivebtn.setBorder(customizedbtn);
        archivebtn.setBackground(Color.DARK_GRAY);
        archivebtn.setForeground(Color.WHITE);
        archivebtn.setFocusPainted(false); // remove stroke effect from button text
        archivebtn.setFont(font1);
        areaofbtn.add(archivebtn);

        instructionbtn.setFocusable(false);
        instructionbtn.setPreferredSize(new Dimension(330, 60));
        instructionbtn.setBorder(customizedbtn);
        instructionbtn.setBackground(Color.DARK_GRAY);
        instructionbtn.setForeground(Color.WHITE);
        instructionbtn.setFocusPainted(false); // remove stroke effect from button text
        instructionbtn.setFont(font1);
        areaofbtn.add(instructionbtn);

        quitbtn.setFocusable(false);
        quitbtn.setPreferredSize(new Dimension(330, 60));
        quitbtn.setBorder(customizedbtn);
        quitbtn.setBackground(Color.DARK_GRAY);
        quitbtn.setForeground(Color.WHITE);
        quitbtn.setFocusPainted(false); // remove stroke effect from button text
        quitbtn.setFont(font1);
        areaofbtn.add(quitbtn);

        // center the window
        setLocationRelativeTo(null);

        // disable window resizing
        setResizable(false);

        // display window
        setVisible(true);
    }

    public static void main(String[] args) {
        new StartPage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startbtn) {
            dispose(); // close the window
           NewSimulationPage NSPage = new NewSimulationPage();
        }
    }

}