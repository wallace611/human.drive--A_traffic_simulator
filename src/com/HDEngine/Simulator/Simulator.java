package com.HDEngine.Simulator;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Simulator.Settings.Info;
import com.HDEngine.Simulator.Settings.Setter;
import com.HDEngine.Simulator.Settings.Settings;
import com.HDEngine.Simulator.Components.RenderWindow;
import com.HDEngine.UI.SimulationPage;
import com.HDEngine.Utilities.FileManageTools.FileManager;
import processing.core.PImage;

import javax.swing.*;
import java.awt.*;
import java.net.URISyntaxException;

public class Simulator {
    public static World world;
    public static SimulationPage ui;
    public static String rcImagePath;
    public static String vehicleImagePath;

    public static World loadFile(String path) {
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
                    rc.setIntersection(erc.getIntersection());
                    rc.setImagePath(rcImagePath);
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

    public static void setupImage(World world, RenderWindow renderer) {
        System.out.println("Simulator Main thread: Start loading image");
        for (HDObject object : world.getChildren()) {
            try {
                object.setSprite(renderer.loadImage(object.getImagePath()));
            } catch (Exception ignore) {}
        }
        System.out.println("Simulator Main Thread: Done!");
    }

    private static SimulationPage constructSimulatePage() throws InterruptedException {
        final SimulationPage[] uiContainer = new SimulationPage[1];

        System.out.println("Simulator Main thread: Start the process for simulator page");
        try {
            SwingUtilities.invokeLater(() -> {
                try {
                    uiContainer[0] = new SimulationPage();
                } catch (Exception e) {
                    e.printStackTrace();  // 更好的錯誤處理
                }
            });

            while (uiContainer[0] == null || uiContainer[0].getWindow() == null) {
                Thread.sleep(10);  // 避免過度佔用 CPU
            }
        } catch (Exception e) {
            e.printStackTrace();  // 處理可能的異步錯誤
        }
        return uiContainer[0];
    }

    public static String getFilePath(String title) {
        FileDialog dialog = new FileDialog((Frame) null, title);
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory() + dialog.getFile();
        dialog.dispose();
        System.out.println(file);
        return file;
    }

    public static void loadFileInBackground() {
        SwingWorker<String, Void> fileLoader = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return getFilePath("Select an obj");
            }

            @Override
            protected void done() {
                try {
                    String path = get();
                    try {
                        world = loadFile(path);
                    } catch (Exception e) {
                        world = Settings.getDemoWorld1();
                    }
                    Info.world = world;
                    while (Info.uiPage == null) {
                        Thread.sleep(10);
                    }
                    setupImage(world, Info.uiPage.getWindow());
                    PImage img = ui.getWindow().loadImage(vehicleImagePath);
                    world.setCarImage(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        fileLoader.execute();
    }

    public static void setupImagePath() throws URISyntaxException {
        String classpath = String.valueOf(Simulator.class.getProtectionDomain().getCodeSource().getLocation());
        rcImagePath = getFilePath("Select road image");
        vehicleImagePath = getFilePath("Select vehicle image");
    }

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        setupImagePath();

        world = Settings.getDemoWorld1();
        Info.world = world;

        ui = constructSimulatePage();
        Info.uiPage = ui;

        while (Info.uiPage == null) {
            Thread.sleep(10);
        }
        setupImage(world, Info.uiPage.getWindow());
        PImage img = ui.getWindow().loadImage(vehicleImagePath);
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
                Info.world.tick((double) deltaTime * Settings.speed);
                Info.tps = (int) (1 / deltaTime);
            } else {
                Thread.sleep(10);
                Info.tps = 0;
            }
        }
    }
}
