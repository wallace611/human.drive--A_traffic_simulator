package com.HDEngine.Simulator.Settings;

import com.HDEngine.Simulator.Components.Traffic.TLGroup;
import com.HDEngine.Simulator.Components.Traffic.TrafficLightManager;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;

import static com.HDEngine.Simulator.Simulator.*;

public class Settings {
    public static int fps = 100;
    public static int tps = 500;
    public static int uiWidth = 900;
    public static int uiHeight = 615;
    public static int windowWidth = 800;
    public static int windowHeight = 600;
    public static boolean running = false;
    public static float speed = 1.0f;
    public static boolean killCongestedVehicle = true;
    public static int congestionTimeout = 8;
    public static boolean speedUpAtIntersection = true;
    public static float reactionTime = 0.5f;
    public static boolean debugMode = false;
    public static int[] movingKey = new int[]{87, 65, 83, 68};
    public static int[] rotKey = new int[]{81, 69};
    public static int[] scaleKey = new int[]{45, 61};


    public static World getDemoWorld1() {
        World world = new World(100, 100);
        RoadChunk rc22 = new RoadChunk((byte) 0);
        rc22.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 2, rc22);

        RoadChunk rc23 = new RoadChunk((byte) 1, new RoadChunk[]{rc22}, new float[]{1.0f});
        rc23.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 3, rc23);

        RoadChunk rc24 = new RoadChunk((byte) 1, new RoadChunk[]{rc23}, new float[]{1.0f});
        rc24.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 4, rc24);

        RoadChunk rc25 = new RoadChunk((byte) 3, new RoadChunk[]{rc24}, new float[]{1.0f});
        rc25.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 5, rc25);

        RoadChunk rc26 = new RoadChunk((byte) 1, new RoadChunk[]{rc25}, new float[]{1.0f});
        rc26.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 6, rc26);

        RoadChunk rc27 = new RoadChunk((byte) 1, new RoadChunk[]{rc26}, new float[]{1.0f});
        rc27.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 7, rc27);

        RoadChunk rc28 = new RoadChunk((byte) 1 , new RoadChunk[]{rc27}, new float[]{1.0f});
        rc28.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 8, rc28);

        RoadChunk rc29 = new RoadChunk((byte) 3, new RoadChunk[]{rc28}, new float[]{1.0f});
        rc29.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 9, rc29);
        rc29.setIntersection(true);

        RoadChunk rc210 = new RoadChunk((byte) 3, new RoadChunk[]{rc29}, new float[]{1.0f});
        rc210.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 10, rc210);
        rc210.setIntersection(true);

        RoadChunk rc211 = new RoadChunk((byte) 1, new RoadChunk[]{rc210}, new float[]{1.0f});
        rc211.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 11, rc211);

        RoadChunk rc212 = new RoadChunk((byte) 1, new RoadChunk[]{rc211}, new float[]{1.0f});
        rc212.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(2, 12, rc212);
        world.getSummonChunk().add(rc212);

        RoadChunk rc311 = new RoadChunk((byte) 0);
        rc311.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 11, rc311);

        RoadChunk rc310 = new RoadChunk((byte) 3, new RoadChunk[]{rc311, rc210}, new float[]{1.0f, 1.0f});
        rc310.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 10, rc310);
        rc310.setIntersection(true);

        RoadChunk rc410 = new RoadChunk((byte) 1, new RoadChunk[]{rc310}, new float[]{1.0f});
        rc410.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(4, 10, rc410);

        RoadChunk rc510 = new RoadChunk((byte) 1, new RoadChunk[]{rc410}, new float[]{1.0f});
        rc510.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(5, 10, rc510);

        RoadChunk rc610 = new RoadChunk((byte) 1, new RoadChunk[]{rc510}, new float[]{1.0f});
        rc610.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(6, 10, rc610);
        world.getSummonChunk().add(rc610);

        RoadChunk rc69 = new RoadChunk((byte) 0);
        rc69.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(6, 9, rc69);

        RoadChunk rc59 = new RoadChunk((byte) 1, new RoadChunk[]{rc69}, new float[]{1.0f});
        rc59.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(5, 9, rc59);

        RoadChunk rc49 = new RoadChunk((byte) 1, new RoadChunk[]{rc59}, new float[]{1.0f});
        rc49.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(4, 9, rc49);

        RoadChunk rc39 = new RoadChunk((byte) 3, new RoadChunk[]{rc310, rc49}, new float[]{1.0f, 1.0f});
        rc39.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 9, rc39);
        rc39.setIntersection(true);

        rc29.addRoad(rc39, 1.0f);

        RoadChunk rc38 = new RoadChunk((byte) 1, new RoadChunk[]{rc39}, new float[]{1.0f});
        rc38.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 8, rc38);

        RoadChunk rc37 = new RoadChunk((byte) 1, new RoadChunk[]{rc38}, new float[]{1.0f});
        rc37.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 7, rc37);

        RoadChunk rc36 = new RoadChunk((byte) 1, new RoadChunk[]{rc37}, new float[]{1.0f});
        rc36.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 6, rc36);

        RoadChunk rc35 = new RoadChunk((byte) 3, new RoadChunk[]{rc36}, new float[]{1.0f});
        rc35.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 5, rc35);

        RoadChunk rc34 = new RoadChunk((byte) 1, new RoadChunk[]{rc35}, new float[]{1.0f});
        rc34.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 4, rc34);

        RoadChunk rc33 = new RoadChunk((byte) 1, new RoadChunk[]{rc34}, new float[]{1.0f});
        rc33.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 3, rc33);

        RoadChunk rc32 = new RoadChunk((byte) 1, new RoadChunk[]{rc33}, new float[]{1.0f});
        rc32.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(3, 2, rc32);
        world.getSummonChunk().add(rc32);

        RoadChunk rc011 = new RoadChunk((byte) 0);
        rc011.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 11, rc011);

        RoadChunk rc010 = new RoadChunk((byte) 1, new RoadChunk[]{rc011}, new float[]{1.0f});
        rc010.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 10, rc010);

        RoadChunk rc110 = new RoadChunk((byte) 1, new RoadChunk[]{rc010}, new float[]{1.0f});
        rc110.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(1, 10, rc110);
        rc210.addRoad(rc110, 1.0f);

        RoadChunk rc19 = new RoadChunk((byte) 1, new RoadChunk[]{rc29}, new float[]{1.0f});
        rc19.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(1, 9, rc19);

        RoadChunk rc09 = new RoadChunk((byte) 3, new RoadChunk[]{rc010, rc19}, new float[]{1.0f, 1.0f});
        rc09.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 9, rc09);

        RoadChunk rc08 = new RoadChunk((byte) 1, new RoadChunk[]{rc09}, new float[]{1.0f});
        rc08.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 8, rc08);

        RoadChunk rc07 = new RoadChunk((byte) 1, new RoadChunk[]{rc08}, new float[]{1.0f});
        rc07.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 7, rc07);

        RoadChunk rc06 = new RoadChunk((byte) 1, new RoadChunk[]{rc07}, new float[]{1.0f});
        rc06.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 6, rc06);

        RoadChunk rc05 = new RoadChunk((byte) 1, new RoadChunk[]{rc06}, new float[]{1.0f});
        rc05.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 5, rc05);

        RoadChunk rc04 = new RoadChunk((byte) 1, new RoadChunk[]{rc05}, new float[]{1.0f});
        rc04.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 4, rc04);

        RoadChunk rc03 = new RoadChunk((byte) 1, new RoadChunk[]{rc04}, new float[]{1.0f});
        rc03.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 3, rc03);

        RoadChunk rc02 = new RoadChunk((byte) 1, new RoadChunk[]{rc03}, new float[]{1.0f});
        rc02.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(0, 2, rc02);
        world.getSummonChunk().add(rc02);
        rc02.setSummonProbability(0.2f);

        RoadChunk rc15 = new RoadChunk((byte) 1, new RoadChunk[]{rc05}, new float[]{1.0f});
        rc15.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(1, 5, rc15);

        RoadChunk rc45 = new RoadChunk((byte) 1, new RoadChunk[]{rc35}, new float[]{1.0f});
        rc45.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(4, 5, rc45);

        rc25.addRoad(rc15, 1.0f);
        rc35.addRoad(rc25, 1.0f);
        rc25.setIntersection(true);
        rc35.setIntersection(true);

        RoadChunk rc55 = new RoadChunk((byte) 1, new RoadChunk[]{rc45}, new float[]{1.0f});
        rc55.setImagePath("src/com/HDEngine/Simulator/image/roadImg.png");
        world.addRoadChunk(5, 5, rc55);
        world.getSummonChunk().add(rc55);

        TLGroup tl1 = new TLGroup(new int[]{15, 10});
        TrafficLightManager.addGroup(0, tl1);

        rc24.setTrafficLight(0, 0);
        rc34.setTrafficLight(0, 0);
        rc26.setTrafficLight(0, 0);
        rc36.setTrafficLight(0, 0);
        rc04.setTrafficLight(0, 0);
        rc15.setTrafficLight(0, 1);
        rc45.setTrafficLight(0, 1);

        TLGroup tl2 = new TLGroup(new int[]{15, 10});
        TrafficLightManager.addGroup(1, tl2);
        rc28.setTrafficLight(1, 0);
        rc38.setTrafficLight(1, 0);
        rc211.setTrafficLight(1, 0);
        rc311.setTrafficLight(1, 0);
        rc19.setTrafficLight(1, 1);
        rc410.setTrafficLight(1, 1);
        rc49.setTrafficLight(1, 1);
        rc110.setTrafficLight(1, 1);

        return world;
    }
}
