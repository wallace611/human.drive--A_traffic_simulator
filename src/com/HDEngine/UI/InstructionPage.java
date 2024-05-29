import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class InstructionPage extends JFrame{
    
    JFrame frame = new JFrame();
    JPanel background = new JPanel(new BorderLayout());
    InstructionPage() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // 创建一个 JTextArea 用于显示文本内容
        JTextArea textArea = new JTextArea();
        textArea.setText("這是一段不可更改的文字。無論如何都無法修改這段內容。\n"
                + "你可以添加更多的文字，讓這段文字變得更長，從而需要滾動。\n"
                + "滾動條會自動出現，以便你查看所有內容。");
        textArea.setEditable(false); // 设置文本区域不可编辑
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 仅在单词边界换行
        
        // 创建一个 JScrollPane 用于滚动
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // 将滚动面板添加到主窗口中
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        // 显示主窗口
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new InstructionPage();
    }
}
