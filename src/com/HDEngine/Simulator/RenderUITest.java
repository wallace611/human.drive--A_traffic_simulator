package com.HDEngine.Simulator;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Simulator.Render.RenderWindow;
import com.HDEngine.Utilities.Vector2D;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;

import javax.swing.*;
import java.awt.*;

public class RenderUITest {
    private World world;
    private JFrame frame;
    private RenderWindow window;
    public JLabel label;

    public RenderUITest() {
        world = new World(100, 100);

        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setMinimumSize(new Dimension(820, 650));

        JPanel panel = new JPanel();
        frame.add(panel);

        label = new JLabel("Test");
        label.setPreferredSize(new Dimension(300, 10));
        panel.add(label);

        SwingUtilities.invokeLater(() -> {
            window = new RenderWindow(world.getChildrenListRef());
            String[] processingArgs = {"GameLoopWindow"};
            PApplet.runSketch(processingArgs, window);
            PSurfaceAWT surf = (PSurfaceAWT) window.getSurface();
            if (surf != null) {
                PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
                panel.add(smoothCanvas);
                frame.revalidate();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        final RenderUITest[] uiContainer = new RenderUITest[1];

        SwingUtilities.invokeLater(() -> {
            uiContainer[0] = new RenderUITest();
        });

        while (uiContainer[0] == null) {
            Thread.sleep(10);
        }

        RenderUITest simulator = uiContainer[0];
        while (simulator.window == null) {
            Thread.sleep(10);
        }
        RoadChunk rc1 = new RoadChunk();
        rc1.setSprite(simulator.window.loadImage("C:\\Users\\urmom\\IdeaProjects\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        rc1.setScale(0.2f);
        simulator.world.addRoadChunk(1, 1, rc1);

        RoadChunk rc2 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc1}, new float[]{1.0f});
        rc2.setSprite(simulator.window.loadImage("C:\\Users\\urmom\\IdeaProjects\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        rc2.setScale(0.2f);
        simulator.world.addRoadChunk(2, 1, rc2);

        RoadChunk rc3 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc2}, new float[]{1.0f});
        rc3.setSprite(simulator.window.loadImage("C:\\Users\\urmom\\IdeaProjects\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        rc3.setScale(0.2f);
        simulator.world.addRoadChunk(3, 1, rc3);

        RoadChunk rc4 = new RoadChunk((byte) 0b11000, new RoadChunk[]{rc2, rc3}, new float[]{1.0f, 1.0f});
        rc4.setSprite(simulator.window.loadImage("C:\\Users\\urmom\\IdeaProjects\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        rc4.setScale(0.2f);
        simulator.world.addRoadChunk(3, 2, rc4);

        PImage img = simulator.window.loadImage("C:\\Users\\urmom\\IdeaProjects\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Simulator\\image\\test.png");
        int count = 0;
        while (true) {
            Thread.sleep(5);
            simulator.world.tick((double) 10/1000);

            if (count % 100 == 0) {
                Vehicle v1 = new Vehicle(100);
                v1.setSprite(img);
                v1.setScale(0.05f);
                simulator.world.spawnVehicle(3, 2, v1);
            }
            if (count % 1000 == 0) {
                System.gc();
                count = 0;
                System.out.println("garbage");
            }
            count += 1;
            simulator.label.setText(String.format("%s, %f", simulator.window.getCamLoc(), simulator.window.getCamRot()));
        }
    }
}
