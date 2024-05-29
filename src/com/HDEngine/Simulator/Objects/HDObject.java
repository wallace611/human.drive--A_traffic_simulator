package com.HDEngine.Simulator.Objects;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Utilities.*;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Vector;

public class HDObject {
    protected Vector2D location;
    protected double rotation;
    protected double scale;
    protected HDObject parent;
    protected final ArrayList<HDObject> children;
    protected final ArrayList<HDObject> childRemoveList;
    protected PImage sprite;

    public HDObject() {
        location = new Vector2D();
        rotation = 0.0f;
        scale = 1.0f;
        parent = null;
        children = new ArrayList<HDObject>();
        childRemoveList = new ArrayList<HDObject>();
    }

    public void begin() {

    }

    public void tick(double deltaTime) {
        for (HDObject object : childRemoveList) {
            if (object != null) {
                children.remove(object);
            }
        }
        childRemoveList.clear();
    }

    public void kill() {
        if (parent != null) {
            parent.removeChildRecursively(this);
        }
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

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
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

    public ArrayList<HDObject> getChildrenListRef() {
        return children;
    }

    public PImage getSprite() {
        return sprite;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public void addChild(HDObject child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(HDObject child) {
        childRemoveList.add(child);
        child.parent = null;
    }

    public void removeChildRecursively(HDObject child) {
        removeChild(child);
        if (parent != null) {
            parent.removeChildRecursively(child);
        }
    }
}
