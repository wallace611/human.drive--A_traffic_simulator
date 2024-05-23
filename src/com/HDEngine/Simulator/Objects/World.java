package com.HDEngine.Simulator.Objects;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Utilities.Vector2D;

import java.util.ArrayList;

public class World extends HDObject{
    private RoadChunk[][] chunks;
    private RoadChunk[] summonChunk;

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

    public void addVehicle(int x, int y, Vehicle car) {
        chunks[x][y].spawnVehicle(car);
        children.add(car);
    }

    public RoadChunk[][] getChunks() {
        return chunks;
    }

    public RoadChunk[] getSummonChunk() {
        return summonChunk;
    }
}
