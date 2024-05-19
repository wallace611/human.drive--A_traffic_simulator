package com.HDEngine.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class test{
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

            JButton button = new JButton("Drag Me");
            button.setBounds(150, 50, 100, 30);
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
            
            GhostDragListener ghostDragListener = new GhostDragListener(button, frame, bottomPanel);
            button.addMouseListener(ghostDragListener);
            button.addMouseMotionListener(ghostDragListener);

            frame.add(button);
            frame.setVisible(true);
        });
    }

    static class GhostDragListener extends MouseAdapter {
        private final JComponent component;
        private final JFrame frame;
        private final JPanel bottomPanel;
        private Point initialClick;
        private JWindow ghostWindow;
        private Point initialLocation;

        public GhostDragListener(JComponent component, JFrame frame, JPanel bottomPanel) {
            this.component = component;
            this.frame = frame;
            this.bottomPanel = bottomPanel;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint();
            initialLocation = component.getLocation();

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
            // 计算新的位置
            int xMoved = e.getXOnScreen() - initialClick.x;
            int yMoved = e.getYOnScreen() - initialClick.y;
            Point newLocation = new Point(xMoved, yMoved);

            // 获取父容器的位置
            Point parentLocation = component.getParent().getLocationOnScreen();
            int newX = newLocation.x - parentLocation.x;
            int newY = newLocation.y - parentLocation.y;

            // 计算按钮的新的边界
            Rectangle buttonBounds = new Rectangle(newX, newY, component.getWidth(), component.getHeight());

            // 获取 bottomPanel 的边界
            Rectangle bottomBounds = bottomPanel.getBounds();
            Point bottomPanelLocation = bottomPanel.getLocationOnScreen();
            bottomBounds.setLocation(bottomPanelLocation);

            // 检查按钮的所有边界是否在 bottomPanel 内
            if (bottomBounds.contains(newX, newY) &&
                bottomBounds.contains(newX + component.getWidth(), newY) &&
                bottomBounds.contains(newX, newY + component.getHeight()) &&
                bottomBounds.contains(newX + component.getWidth(), newY + component.getHeight())) {
                // 移动按钮到新的位置
                component.setLocation(newX, newY);
            } else {
                // 恢复按钮到初始位置
                component.setLocation(initialLocation);
            }
            ghostWindow.dispose();
        }
    }
}