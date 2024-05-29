package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Utilities.Vector2D;

import java.util.ArrayList;

public class
World extends HDObject {
    private RoadChunk[][] chunks;
    private RoadChunk[] summonChunk;

    // private ArrayList<Vehicle> children; from HDObject class, containing the RoadChunk and Vehicle which need to be rendered

    public World(int x, int y) {
        chunks = new RoadChunk[x][y];
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

        for (RoadChunk[] rcArr : chunks) {
            for (RoadChunk rc : rcArr) {
                if (rc != null) {
                    rc.tick(deltaTime);
                }
            }
        }
    }

    public void spawnVehicle(int x, int y, Vehicle car) {
        chunks[x][y].spawnVehicle(car);
        children.add(car);
        children.add(car.getCollisionRef());
        children.add(car.getFrontCollisionRef());
    }

    public void removeVehicle(int x, int y, Vehicle car) {
        chunks[x][y].removeChild(car);
        children.remove(car);
        children.remove(car.getCollisionRef());
        children.remove(car.getFrontCollisionRef());
    }

    @Override
    public void removeChild(HDObject child) {
        super.removeChild(child);
        if (child instanceof Vehicle car) {
            children.remove(car.getCollisionRef());
            children.remove(car.getFrontCollisionRef());
        }
    }

    public void addRoadChunk(int x, int y, RoadChunk rc) {
        rc.setLocation(new Vector2D(y * 100, x * 100));
        chunks[x][y] = rc;
        children.add(0, rc);
        children.add(0, rc.getRoadAreaRef());
        rc.setParent(this);
    }

    public void removeRoadChunk(int x, int y, RoadChunk rc) {
        chunks[x][y] = null;
        children.remove(rc);
        rc.setParent(null);
    }

    public RoadChunk[] getSummonChunk() {
        return summonChunk;
    }
}
