package com.HDEngine.Simulator.Objects;

import com.HDEngine.Utilities.*;

public class HDObject {
    protected Vector2D location;
    protected double rotation;

    public HDObject() {
        location = new Vector2D();
        rotation = 0.0f;
    }

    public void begin() {

    }

    public void tick(double deltaTime) {

    }

    public void kill() {

    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
