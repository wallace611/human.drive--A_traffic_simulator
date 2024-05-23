package com.HDEngine;

import com.HDEngine.Simulator.Objects.Dynamic.*;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Objects.Static.World;
import com.HDEngine.Utilities.*;


public class Test {
    public static void main(String[] args) throws InterruptedException {
        CollisionArea c = new CollisionArea(new Vector2D(), 0.0, new Vector2D(2, 2));
        CollisionArea d = new CollisionArea(new Vector2D(6.1, 0), 45, new Vector2D(4, 4));
        RoadChunk r = new RoadChunk((byte) 0, new RoadChunk[0], new float[0]);
        r.setLocation(new Vector2D(0, 0));
        Vehicle v = new Vehicle(0);
        v.setLocation(new Vector2D(11.2, 0));
        v.setRotation(45);

        System.out.println(r.getRoadArea().getLocation() + "; " + r.getRoadArea().getOffset());
        System.out.println(v.getCollision().getLocation() + "; " + v.getCollision().getOffset());
        System.out.println(CollisionArea.areOverlapping(r.getRoadArea(), v.getCollision()));
    }



}
