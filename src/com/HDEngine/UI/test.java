package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Draggable JButton Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.setLayout(null);  // 使用 null layout 以便自由定位组件

            // 创建三分区的面板
            JPanel topPanel = new JPanel();
            JPanel middlePanel = new JPanel();
            JPanel bottomPanel = new JPanel();

            topPanel.setBackground(Color.RED);
            middlePanel.setBackground(Color.YELLOW);
            bottomPanel.setBackground(Color.GREEN);

            topPanel.setBounds(0, 0, 400, 200);
            middlePanel.setBounds(0, 200, 400, 200);
            bottomPanel.setBounds(0, 400, 400, 200);

            frame.add(topPanel);
            frame.add(middlePanel);
            frame.add(bottomPanel);

            // 创建一个容器来存放拖曳的按钮
            JPanel dragPanel = new JPanel(null);
            dragPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            frame.add(dragPanel);

            JButton button = new JButton("Drag Me");
            button.setBounds(150, 50, 100, 30);
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
            
            GhostDragListener ghostDragListener = new GhostDragListener(button, dragPanel, bottomPanel);
            button.addMouseListener(ghostDragListener);
            button.addMouseMotionListener(ghostDragListener);

            frame.add(button);
            frame.setVisible(true);
        });
    }

    static class GhostDragListener extends MouseAdapter {
        private final JComponent component;
        private final JPanel dragPanel;
        private final JPanel bottomPanel;
        private Point initialClick;
        private JWindow ghostWindow;

        public GhostDragListener(JComponent component, JPanel dragPanel, JPanel bottomPanel) {
            this.component = component;
            this.dragPanel = dragPanel;
            this.bottomPanel = bottomPanel;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint();

            // 创建一个半透明的 JWindow 作为拖动的影子
            ghostWindow = new JWindow();
            ghostWindow.setLayout(new BorderLayout());
            ghostWindow.setSize(component.getSize());
            
            // 创建一个带有半透明按钮外观的 JPanel
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

        @Override
        public void mouseReleased(MouseEvent e) {
            // 获取拖曳结束后的位置
            Point screenPoint = e.getLocationOnScreen();
            Point containerPoint = SwingUtilities.convertPointFromScreen(screenPoint, dragPanel);

            // 检查按钮的位置是否在底部面板的区域内
            if (bottomPanel.contains(containerPoint)) {
                // 在拖曳结束后创建新的按钮并添加到 dragPanel 中
                JButton newButton = new JButton("Drag Me");
                newButton.setBounds(containerPoint.x, containerPoint.y, component.getWidth(), component.getHeight());
                newButton.setBackground(Color.LIGHT_GRAY);
                newButton.setFocusPainted(false);
                
                GhostDragListener newGhostDragListener = new GhostDragListener(newButton, dragPanel, bottomPanel);
                newButton.addMouseListener(newGhostDragListener);
                newButton.addMouseMotionListener(newGhostDragListener);

                dragPanel.add(newButton);
                dragPanel.repaint(); // 重新绘制以显示新的按钮
            }

            ghostWindow.dispose(); // 关闭拖动的影子窗口
        }
    }
}
