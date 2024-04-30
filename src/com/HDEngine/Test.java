package com.HDEngine;

import com.HDEngine.Simulator.Components.CollisionArea;
import com.HDEngine.Utilities.Vector2D;

public class Test {
    public static void main(String[] args) {
        CollisionArea c = new CollisionArea(new Vector2D(0, 0), 0, new Vector2D(1, 1));
        CollisionArea d = new CollisionArea(new Vector2D(2.2, 1), 270, new Vector2D(1, 2));
        System.out.println(CollisionArea.areOverlapping(c.getVertex(), d.getVertex()));
        for (Vector2D v : c.getVertex()) {
            System.out.println(v);
        }
        for (Vector2D v : d.getVertex()) {
            System.out.println(v);
        }
    }
}
