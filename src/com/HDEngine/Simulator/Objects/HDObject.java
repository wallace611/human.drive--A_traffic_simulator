package com.HDEngine.Simulator.Objects;

import com.HDEngine.Utilities.*;

import java.util.ArrayList;

public class HDObject {
    protected Vector2D location;
    protected double rotation;
    protected HDObject parent;
    protected final ArrayList<HDObject> children;

    public HDObject() {
        location = new Vector2D();
        rotation = 0.0f;
        children = new ArrayList<>();
    }

    public void begin() {

    }

    public void tick(double deltaTime) {

    }

    public void kill() {

    }

    public Vector2D getLocation() {
        return new Vector2D(location);
    }

    public void setLocation(Vector2D location) {
        this.location = new Vector2D(location);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public HDObject getParent() {
        return parent;
    }

    public void setParent(HDObject parent) {
        this.parent = parent;
    }

    public ArrayList<HDObject> getChildren() {
        return new ArrayList<HDObject>(children);
    }

    public void addChild(HDObject child) {
        children.add(child);
    }

    public void removeChild(HDObject child) {
        children.remove(child);
    }
}
