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
    public static SimulationPage simulator;

    private static SimulationPage constructSimulatePage(World world) throws InterruptedException {
        final SimulationPage[] uiContainer = new SimulationPage[1];

        SwingUtilities.invokeLater(() -> {
            uiContainer[0] = new SimulationPage();
            uiContainer[0].setWorld(world);
        });


        while (uiContainer[0] == null || uiContainer[0].getWindow() == null || uiContainer[0].getWorld() == null) {
            Thread.sleep(100);
            System.out.println("Waiting...");
        }
        return uiContainer[0];
    }

    public static void main(String[] args) throws InterruptedException {
        //FileManager file = FileManager.loadFromFile("src/com/HDEngine/Utilities/SavedFile/editor_map.obj");

        world = new World(100, 100);
        simulator = constructSimulatePage(world);

        RoadChunk rc1 = new RoadChunk();
        //rc1.setSprite(simulator.window.loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc1.setScale(0.2f);
        simulator.getWorld().addRoadChunk(1, 1, rc1);

        RoadChunk rc2 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc1}, new float[]{1.0f});
        //rc2.setSprite(simulator.window.loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc2.setScale(0.2f);
        simulator.getWorld().addRoadChunk(2, 1, rc2);

        RoadChunk rc3 = new RoadChunk((byte) 0b100, new RoadChunk[]{rc2}, new float[]{1.0f});
        //rc3.setSprite(simulator.window.loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc3.setScale(0.2f);
        simulator.getWorld().addRoadChunk(3, 1, rc3);

        RoadChunk rc4 = new RoadChunk((byte) 0b11000, new RoadChunk[]{rc2, rc3}, new float[]{1.0f, 1.0f});
        //rc4.setSprite(simulator.window.loadImage("src\\com\\HDEngine\\Simulator\\image\\testimg.png"));
        //rc4.setScale(0.2f);
        simulator.getWorld().addRoadChunk(3, 2, rc4);


        PImage img = simulator.getWindow().loadImage("src/com/HDEngine/Simulator/image/car.png");
        int count = 0;
        while (true) {
            Thread.sleep(5);
            simulator.getWorld().tick((double) 5 / 1000);

            if (count % 200 == 0) {
                Vehicle v1 = new Vehicle(100);
                v1.setSprite(img);
                simulator.getWorld().spawnVehicle(3, 2, v1);
            }
            count += 1;
        }
    }
}
