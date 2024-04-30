package com.HDEngine.Simulator.Components;

import com.HDEngine.Utilities.Vector2D;
import static java.lang.Math.*;

public class CollisionArea {
    // the center location in the world dimension
    private Vector2D location;

    // TODO: rotation

    // width and height
    private Vector2D offset;

    public CollisionArea(Vector2D location, Vector2D offset) {
        this.location = location;
        this.offset = offset;
    }

}
