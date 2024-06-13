package com.HDEngine.Simulator;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Simulator.Settings.Info;
import com.HDEngine.Simulator.Settings.Setter;
import com.HDEngine.Simulator.Settings.Settings;
import com.HDEngine.Utilities.RenderWindow;
import com.HDEngine.UI.SimulationPage;
import com.HDEngine.Utilities.FileManageTools.FileManager;
import processing.core.PImage;

import javax.swing.*;

public class Simulator {
    public static World world;
    public static SimulationPage ui;

    private static World loadFile(String path) {
        System.out.println("Simulator Main thread: Start loading file...");
        FileManager file = FileManager.loadFromFile(path);
        World world = new World(file.getMap().length, file.getMap()[0].length);

        RoadChunk[][] roadChunks = new RoadChunk[file.getMap().length][file.getMap()[0].length];

        // First pass: create all RoadChunks
        for (int i = 0; i < file.getMap().length; i++) {
            for (int j = 0; j < file.getMap()[0].length; j++) {
                EditorRoadChunk erc = file.getMap()[i][j];
                if (erc != null) {
                    int roadDir = 0;
                    for (int dir : erc.getDirection()) {
                        roadDir <<= 1;
                        roadDir += dir;
                    }
                    roadDir >>= 1;
                    RoadChunk rc = new RoadChunk((byte) roadDir);
                    rc.setImagePath("src\\com\\HDEngine\\Simulator\\image\\testimg.png");
                    world.addRoadChunk(i, j, rc);
                    roadChunks[i][j] = rc;

                    if (erc.haveTrafficLight()) {
                        rc.setTrafficLight(erc.getTrafficLightGroup(), erc.getTrafficLightTeams());
                    }
                }
            }
        }

        // Second pass: connect RoadChunks
        for (int i = 0; i < file.getMap().length; i++) {
            for (int j = 0; j < file.getMap()[0].length; j++) {
                EditorRoadChunk erc = file.getMap()[i][j];
                if (erc != null) {
                    RoadChunk rc = roadChunks[i][j];
                    int[] connections = erc.getDirection();
                    for (int k = 0; k < connections.length; k++) {
                        if (connections[k] == 1) {
                            int ni = i + (int) Math.round(Math.sin(Math.PI / 4 * k));
                            int nj = j + (int) Math.round(Math.cos(Math.PI / 4 * k));
                            if (ni >= 0 && ni < file.getMap().length && nj >= 0 && nj < file.getMap()[0].length && roadChunks[ni][nj] != null) {
                                rc.addRoad(roadChunks[ni][nj], 1.0f); // Assuming equal weight for all connections
                            }
                        }
                    }
                    if (erc.startCheck()) {
                        world.getSummonChunk().add(rc);
                    }
                }
            }
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
        world = Settings.getDemoWorld1();
        ui = constructSimulatePage(world);
        setupImage(world, ui.getWindow());
        PImage img = ui.getWindow().loadImage("src/com/HDEngine/Simulator/image/car.png");
        world.setCarImage(img);

        // wait for render window setting up
        while (!ui.getWindow().isSet()) {
            Thread.sleep(10);
        }

        Thread setterThread = new Thread(new Setter());
        setterThread.start();

        long targetDeltaTime;
        long startMS;
        while (true) {
            if (Settings.running) {
                targetDeltaTime = (long) ((double) 1 / Settings.tps * 1e9f / Settings.speed);
                startMS = System.nanoTime();
                long currentMS;
                do {
                    currentMS = System.nanoTime();
                } while (currentMS - startMS <= targetDeltaTime);

                double deltaTime = (double) (currentMS - startMS) / 1e9f;
                startMS = currentMS;
                ui.getWorld().tick((double) deltaTime * Settings.speed);
                Info.tps = (int) (1 / deltaTime);
            } else {
                Thread.sleep(10);
                Info.tps = 0;
            }
        }
    }
}
