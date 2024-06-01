package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.ITransform;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PImage;

import java.util.ArrayList;

public class
World extends HDObject {
    private final RoadChunk[][] chunks;
    private final ArrayList<RoadChunk> summonChunk;
    private int roadCount;
    private int count = 0;
    private PImage carImage;

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
        collisionDetection();

        if (count % 200 == 0) {
            count = 0;
            spawnVehicleThroughArr();
        }

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
        // collision detection
        for (int i = roadCount; i < children.size(); i++) {
            Vehicle currentCar = (Vehicle) children.get(i); // the vehicle to detect the collision
            CollisionArea frontCA = currentCar.getFrontCollision();
            CollisionArea backCA = currentCar.getCollision();

            ArrayList<RoadChunk> hitFRC = new ArrayList<>();
            ArrayList<RoadChunk> hitBRC = new ArrayList<>();
            for (int j = 0; j < roadCount; j++) {
                if (children.get(j) instanceof RoadChunk rc) {
                    if (CollisionArea.areOverlapping(frontCA, rc.getRoadArea())) {
                        hitFRC.add(rc);
                        rc.hasVehicle = true;
                    }
                    if (CollisionArea.areOverlapping(backCA, rc.getRoadArea())) {
                        hitBRC.add(rc);
                        rc.hasVehicle = true;
                    }
                }
            }
            for (RoadChunk rc : hitFRC) {
                for (HDObject object : rc.getChildren()) {
                    if (object instanceof Vehicle v) {
                        if (CollisionArea.areOverlapping(v.getCollision(), frontCA)) {
                            currentCar.frontCollide(object);
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
