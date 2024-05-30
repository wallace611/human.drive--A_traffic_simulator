package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Vector2D;

import java.util.ArrayList;

public class
World extends HDObject {
    private RoadChunk[][] chunks;
    private RoadChunk[] summonChunk;
    private int roadCount;

    // private ArrayList<HDObject> children; from HDObject class, containing the RoadChunk and Vehicle which need to be rendered

    public World(int x, int y) {
        chunks = new RoadChunk[x][y];
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

        for (RoadChunk[] rcArr : chunks) {
            for (RoadChunk rc : rcArr) {
                if (rc != null) {
                    rc.tick(deltaTime);
                }
            }
        }
        // collision detection
        for (int i = roadCount; i < children.size(); i++) {
            CollisionArea frontCA = ((Vehicle) children.get(i)).getCollision();
            ArrayList<RoadChunk> hitRC = new ArrayList<>();
            for (int j = 0; j < roadCount; j++) {
                if (children.get(j) instanceof RoadChunk rc) {
                    if (CollisionArea.areOverlapping(frontCA, rc.getRoadArea())) {
                        hitRC.add(rc);
                    }
                }
            }
            System.out.println(hitRC);
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

    public RoadChunk[] getSummonChunk() {
        return summonChunk;
    }
}
