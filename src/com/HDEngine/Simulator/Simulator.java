package com.HDEngine.Simulator;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import com.HDEngine.Simulator.Components.Traffic.TLGroup;
import com.HDEngine.Simulator.Components.Traffic.TrafficLightManager;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Utilities.Render.RenderWindow;
import com.HDEngine.UI.SimulationPage;
import com.HDEngine.Utilities.FileManageTools.FileManager;
import processing.core.PImage;

import javax.swing.*;
import java.util.ArrayList;

public class Simulator {
    public static World world;
    public static SimulationPage ui;

    private static World loadFile(String path) {
        System.out.println("Simulator Main thread: Start loading file...");
        FileManager file = FileManager.loadFromFile("src/SavedFile/editor_map.obj");
        World world = new World(file.getMap().length, file.getMap()[0].length);

        ArrayList<RoadChunk> rcToConnect = new ArrayList<>();
        for (EditorRoadChunk[] ercArr : file.getMap()) {
            for (EditorRoadChunk erc : ercArr) {
                if (erc != null) {
                    byte roadDir = 0;
                    for (int i : erc.getIntersection()) {
                        roadDir <<= 1;
                        roadDir += i;
                    }
                    RoadChunk rc = new RoadChunk(roadDir);
                    rc.setImagePath("src\\com\\HDEngine\\Simulator\\image\\testimg.png");
                    world.addRoadChunk(erc.getIDX(), erc.getIDY(), rc);
                    rcToConnect.add(rc);
                }
            }
        }
        for (RoadChunk rc : rcToConnect) {
            // TODO
        }

        System.out.println("Simulator Main Thread: Done!");
        return world;
    }

    private static void setupImage(World world, RenderWindow renderer) {
        System.out.println("Simulator Main thread: Start loading image");
        for (HDObject object : world.getChildren()) {
            try {
                object.setSprite(renderer.loadImage(object.getImagePath()));
            } catch (Exception e) {
                System.out.println("shit");
            }
        }
        System.out.println("Simulator Main Thread: Done!");
    }

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
        //world = loadFile("src/SavedFile/editor_map.obj");

        world = new World(100, 100);
        ui = constructSimulatePage(world);
        setupImage(world, ui.getWindow());

        PImage roadImg = ui.getWindow().loadImage("src/com/HDEngine/Simulator/image/testimg.png");
        RoadChunk rc22 = new RoadChunk((byte) 0);
        rc22.setSprite(roadImg);
        world.addRoadChunk(2, 2, rc22);

        RoadChunk rc23 = new RoadChunk((byte) 1, new RoadChunk[]{rc22}, new float[]{1.0f});
        rc23.setSprite(roadImg);
        world.addRoadChunk(2, 3, rc23);

        RoadChunk rc24 = new RoadChunk((byte) 1, new RoadChunk[]{rc23}, new float[]{1.0f});
        rc24.setSprite(roadImg);
        world.addRoadChunk(2, 4, rc24);

        RoadChunk rc25 = new RoadChunk((byte) 3, new RoadChunk[]{rc24}, new float[]{1.0f});
        rc25.setSprite(roadImg);
        world.addRoadChunk(2, 5, rc25);

        RoadChunk rc26 = new RoadChunk((byte) 1, new RoadChunk[]{rc25}, new float[]{1.0f});
        rc26.setSprite(roadImg);
        world.addRoadChunk(2, 6, rc26);

        RoadChunk rc27 = new RoadChunk((byte) 1, new RoadChunk[]{rc26}, new float[]{1.0f});
        rc27.setSprite(roadImg);
        world.addRoadChunk(2, 7, rc27);
        world.getSummonChunk().add(rc27);

        RoadChunk rc37 = new RoadChunk((byte) 0);
        rc37.setSprite(roadImg);
        world.addRoadChunk(3, 7, rc37);

        RoadChunk rc36 = new RoadChunk((byte) 1, new RoadChunk[]{rc37}, new float[]{1.0f});
        rc36.setSprite(roadImg);
        world.addRoadChunk(3, 6, rc36);

        RoadChunk rc35 = new RoadChunk((byte) 3, new RoadChunk[]{rc36}, new float[]{1.0f});
        rc35.setSprite(roadImg);
        world.addRoadChunk(3, 5, rc35);

        RoadChunk rc34 = new RoadChunk((byte) 1, new RoadChunk[]{rc35}, new float[]{1.0f});
        rc34.setSprite(roadImg);
        world.addRoadChunk(3, 4, rc34);

        RoadChunk rc33 = new RoadChunk((byte) 1, new RoadChunk[]{rc34}, new float[]{1.0f});
        rc33.setSprite(roadImg);
        world.addRoadChunk(3, 3, rc33);

        RoadChunk rc32 = new RoadChunk((byte) 1, new RoadChunk[]{rc33}, new float[]{1.0f});
        rc32.setSprite(roadImg);
        world.addRoadChunk(3, 2, rc32);
        world.getSummonChunk().add(rc32);

        RoadChunk rc05 = new RoadChunk((byte) 0);
        rc05.setSprite(roadImg);
        world.addRoadChunk(0, 5, rc05);

        RoadChunk rc15 = new RoadChunk((byte) 1, new RoadChunk[]{rc05}, new float[]{1.0f});
        rc15.setSprite(roadImg);
        world.addRoadChunk(1, 5, rc15);

        RoadChunk rc45 = new RoadChunk((byte) 1, new RoadChunk[]{rc35}, new float[]{1.0f});
        rc45.setSprite(roadImg);
        world.addRoadChunk(4, 5, rc45);

        rc25.addRoad(rc15, 1.0f);
        rc35.addRoad(rc25, 1.0f);

        RoadChunk rc55 = new RoadChunk((byte) 1, new RoadChunk[]{rc45}, new float[]{1.0f});
        rc55.setSprite(roadImg);
        world.addRoadChunk(5, 5, rc55);
        world.getSummonChunk().add(rc55);

        TLGroup tl1 = new TLGroup(new int[]{3, 3});
        TrafficLightManager.addGroup(0, tl1);

        rc55.setTrafficLight(0, 0);

        PImage img = ui.getWindow().loadImage("src/com/HDEngine/Simulator/image/car.png");
        world.setCarImage(img);

        long targetDeltaTime;
        long startMS;
        while (true) {
            targetDeltaTime = (long) ((double) 1 / Settings.tps * 1e9f);
            startMS = System.nanoTime();
            long currentMS;

            do {
                currentMS = System.nanoTime();
            } while (currentMS - startMS <= targetDeltaTime);

            double deltaTime = (double) (currentMS - startMS) / 1e9f;
            startMS = currentMS;
            ui.getWorld().tick((double) deltaTime);
        }
    }
}
