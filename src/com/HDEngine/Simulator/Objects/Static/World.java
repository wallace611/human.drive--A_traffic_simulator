package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Components.Traffic.TrafficLightManager;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class
World extends HDObject {
    private final RoadChunk[][] chunks;
    private final ArrayList<RoadChunk> summonChunk;
    private int roadCount;
    private int count = 0;
    private PImage carImage;
    public int currentTPS;

    // private ArrayList<HDObject> children; from HDObject class, containing the RoadChunk and Vehicle which need to be rendered

    public World(int x, int y) {
        chunks = new RoadChunk[x][y];
        summonChunk = new ArrayList<>();
        roadCount = 0;

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
        currentTPS = (int) (1 / deltaTime);
        collisionDetection();

        if (count % 200 == 0) {
            count = 0;
            spawnVehicleThroughArr();
        }
        count += 1;
        TrafficLightManager.nextTime(deltaTime);

        for (RoadChunk[] rcArr : chunks) {
            for (RoadChunk rc : rcArr) {
                if (rc != null) {
                    rc.tick(deltaTime);
                    rc.hasVehicle = false;
                }
            }
        }
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
        for (RoadChunk rc : summonChunk) {
            if (!rc.hasVehicle) {
                Vehicle v = new Vehicle(100);
                v.setScale(1.5f);
                v.setSprite(carImage);
                rc.spawnVehicle(v);
                children.add(v);
            }
        }
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
