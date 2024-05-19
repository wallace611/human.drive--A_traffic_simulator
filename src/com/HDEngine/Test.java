package com.HDEngine;

import com.HDEngine.Simulator.Objects.Dynamic.*;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.World;
import com.HDEngine.Utilities.*;


public class Test {
    public static void main(String[] args) throws InterruptedException {
        World w = new World(10, 10);
        RoadChunk[][] r = w.getChunks();
        r[0][5] = new RoadChunk((byte) 0, new RoadChunk[0], new float[0]);
        r[0][5].setLocation(new Vector2D(0, 10));
        r[1][5] = new RoadChunk((byte) 0b001, new RoadChunk[]{r[0][5]}, new float[]{1.0f});
        r[1][5].setLocation(new Vector2D(10, 50));
        r[2][5] = new RoadChunk((byte) 0b001, new RoadChunk[]{r[1][5]}, new float[]{1.0f});
        r[2][5].setLocation(new Vector2D(20, 50));
        w.begin();
        Vehicle v = new Vehicle(1.0f);
        w.addVehicle(2, 5, v);
        while (true) {
            Thread.sleep(50);
            w.tick(0.5);
            System.out.println(v.getLocation());
        }
    }



}
