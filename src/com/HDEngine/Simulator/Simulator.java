package com.HDEngine.Simulator;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.UI.SimulationPage;
import com.HDEngine.Utilities.FileManageTools.FileManager;
import processing.core.PImage;

import javax.swing.*;

public class Simulator {
    public static World world;
    public static SimulationPage ui;

    private static SimulationPage constructSimulatePage(World world) throws InterruptedException {
        final SimulationPage[] uiContainer = new SimulationPage[1];

        System.out.println("Simulator Main thread: Start the process for simulator page");
        SwingUtilities.invokeLater(() -> {
            uiContainer[0] = new SimulationPage();
            uiContainer[0].setWorld(world);
        });

        long tmpTime = System.currentTimeMillis();
        System.out.println("Simulator Main thread: Waiting for ui respond...");
        while (uiContainer[0] == null || uiContainer[0].getWindow() == null || uiContainer[0].getWorld() == null) {
            Thread.sleep(1);
        }
        tmpTime = System.currentTimeMillis() - tmpTime;
        System.out.println("Simulator Main thread: Done!, it takes for " + (float) tmpTime / 1000 + " seconds, better than 90% users");
        return uiContainer[0];
    }

    public static void main(String[] args) throws InterruptedException {
        FileManager file = FileManager.loadFromFile("src/SavedFile/editor_map.obj");

        world = new World(100, 100);
        ui = constructSimulatePage(world);

        RoadChunk rc1 = new RoadChunk();
        rc1.setSprite(ui.getWindow().loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc1.setScale(0.2f);
        ui.getWorld().addRoadChunk(1, 1, rc1);

        RoadChunk rc2 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc1}, new float[]{1.0f});
        rc2.setSprite(ui.getWindow().loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc2.setScale(0.2f);
        ui.getWorld().addRoadChunk(2, 1, rc2);

        RoadChunk rc3 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc2}, new float[]{1.0f});
        rc3.setSprite(ui.getWindow().loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc3.setScale(0.2f);
        ui.getWorld().addRoadChunk(3, 1, rc3);

        RoadChunk rc4 = new RoadChunk((byte) 0b11000, new RoadChunk[]{rc2, rc3}, new float[]{1.0f, 1.0f});
        rc4.setSprite(ui.getWindow().loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc4.setScale(0.2f);
        ui.getWorld().addRoadChunk(3, 2, rc4);
        ui.getWorld().getSummonChunk().add(rc4);

        PImage img = ui.getWindow().loadImage("src/com/HDEngine/Simulator/image/car.png");
        world.setCarImage(img);


        while (true) {
            Thread.sleep(5);
            ui.getWorld().tick((double) 5 / 1000);

        }
    }
}
