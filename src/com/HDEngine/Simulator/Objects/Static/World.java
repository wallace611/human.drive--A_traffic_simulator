package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Components.Traffic.TrafficLightManager;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Settings.Info;
import com.HDEngine.Simulator.Settings.Settings;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PImage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class
World extends HDObject {
    private final RoadChunk[][] chunks;
    private final ArrayList<RoadChunk> summonChunk;
    private int roadCount;
    private float count = 0;
    private PImage carImage;
    Vehicle tmpVehicle;
    private double timer;
    private int spawnCount;
    private int arriveCount;


    // private ArrayList<HDObject> children; from HDObject class, containing the RoadChunk and Vehicle which need to be rendered

    public World(int x, int y) {
        chunks = new RoadChunk[x][y];
        summonChunk = new ArrayList<>();
        roadCount = 0;
        timer = 0.0;
        spawnCount = 0;
        arriveCount = 0;
    }

    @Override
    public void begin() {
        super.begin();

        for (RoadChunk[] rcArray : chunks) {
            System.out.print("|");
            for (RoadChunk rc : rcArray)
            {
                if (rc != null) {
                    System.out.print(".");
                } else {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            System.out.println();
        }
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        timer += deltaTime;
        collisionDetection();

        if (count >= 1.0f) {
            count = 0;
            spawnVehicleThroughArr();
        }
        count += (float) deltaTime;
        TrafficLightManager.nextTime(deltaTime);

        for (RoadChunk[] rcArr : chunks) {
            for (RoadChunk rc : rcArr) {
                if (rc != null) {
                    rc.tick(deltaTime);
                    rc.hasVehicle = false;
                }
            }
        }
        if (timer > 1.0) {
            calculateValues();
            timer = 0.0;
        }
    }

    private void calculateValues() {
        // average speed
        double sumSpd = 0.0f;
        int carCount = 0;
        for (int i = roadCount; i < children.size(); i++) {
            if (children.get(i) == tmpVehicle) {
                continue;
            }
            if (children.get(i) instanceof Vehicle v) {
                carCount += 1;
                sumSpd += v.getSpeed();
            }
        }
        Info.averageSpeed = (float) sumSpd / carCount;

        // spawn rate
        Info.spawnPerSecond = spawnCount;
        spawnCount = 0;

        // arrival rate
        Info.arrivePerSecond = arriveCount;
        arriveCount = 0;
    }

    private void collisionDetection() {
        // Record the vehicles in a single RoadChunk
        Map<RoadChunk, ArrayList<Vehicle>> collisionMap = new HashMap<>();

        // iterate through all RoadChunk
        for (int i = 0; i < roadCount; i++) {
            if (children.get(i) instanceof RoadChunk rc) {
                ArrayList<Vehicle> collidedVehicle = new ArrayList<>();
                // iterate through all Vehicle to find if it is collided with the RoadChunk
                for (int j = roadCount; j < children.size(); j++) {
                    if (children.get(j) instanceof Vehicle v) {
                        if (CollisionArea.areOverlapping(rc.getRoadArea(), v.getBackCollision())) {
                            collidedVehicle.add(v);
                            rc.hasVehicle = true;
                        }
                    }
                }
                collisionMap.put(rc, collidedVehicle);
            }
        }

        // iterate through all Vehicle to find if it's front collision area is hit or not
        for (int i = roadCount; i < children.size(); i++) {
            if (children.get(i) instanceof Vehicle v) {
                // Record the RoadChunks which is collided with Vehicle's front collision area
                ArrayList<RoadChunk> collidedRoadChunk = new ArrayList<>();
                for (int j = 0; j < roadCount; j++) {
                    if (children.get(j) instanceof RoadChunk rc) {
                        if (CollisionArea.areOverlapping(rc.getRoadArea(), v.getFrontCollision())) {
                            collidedRoadChunk.add(rc);
                        }
                    }
                }

                for (RoadChunk rc : collidedRoadChunk) {
                    // target vehicle
                    for (Vehicle tv : collisionMap.get(rc)) {
                        if (CollisionArea.areOverlapping(tv.getBackCollision(), v.getFrontCollision())) {
                            boolean flag = v.frontCollided(tv);
                            if (flag) {
                                tv.getBackCollision().renderCollided = true;
                                v.getFrontCollision().renderCollided = true;
                            }
                        } else {
                            tv.getBackCollision().renderCollided = false;
                            v.getFrontCollision().renderCollided = false;
                        }
                    }
                }
            }
        }
    }

    public void spawnVehicleThroughArr() {
        SecureRandom random = new SecureRandom();
        for (RoadChunk rc : summonChunk) {
            if (random.nextFloat() < rc.getSummonProbability()) {
                if (!rc.hasVehicle) {
                    Vehicle v = new Vehicle(200);
                    v.setScale(1.5f);
                    v.setSprite(carImage);
                    rc.spawnVehicle(v);
                    children.add(v);
                    spawnCount += 1;
                }
            }
        }
    }

    public void spawnTmpVehicle(Vector2D loc) {
        if (tmpVehicle == null) {
            tmpVehicle = new Vehicle(0);
            tmpVehicle.setGlobalLocation(loc);
            children.add(tmpVehicle);
        }
    }

    public void removeTmpVehicle() {
        children.remove(tmpVehicle);
        tmpVehicle = null;
    }

    public void spawnVehicle(int x, int y, Vehicle car) {
        chunks[x][y].spawnVehicle(car);
        children.add(car);
    }

    public void removeVehicle(int x, int y, Vehicle car) {
        chunks[x][y].removeChild(car);
        children.remove(car);
    }

    @Override
    public void removeChild(HDObject child) {
        super.removeChild(child);
    }

    @Override
    public void removeChildRecursively(HDObject child) {
        super.removeChildRecursively(child);
        arriveCount += 1;
    }

    public void addRoadChunk(int x, int y, RoadChunk rc) {
        rc.setLocation(new Vector2D(y * 100, x * 100));
        chunks[x][y] = rc;
        children.add(0, rc);
        rc.setParent(this);
        roadCount += 1;
    }

    public void removeRoadChunk(int x, int y, RoadChunk rc) {
        if (chunks[x][y] != null) {
            chunks[x][y] = null;
            children.remove(rc);
            rc.setParent(null);
        }
    }

    public ArrayList<RoadChunk> getSummonChunk() {
        return summonChunk;
    }

    public PImage getCarImage() {
        return carImage;
    }

    public void setCarImage(PImage carImage) {
        this.carImage = carImage;
    }
}
