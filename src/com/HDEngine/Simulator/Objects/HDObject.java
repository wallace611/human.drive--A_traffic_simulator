package com.HDEngine.Simulator.Objects;

import com.HDEngine.Utilities.*;
import processing.core.PImage;

import java.util.ArrayList;

public class HDObject implements ITransform{
    protected Vector2D location;
    protected double rotation;
    protected double scale;
    protected HDObject parent;
    protected final ArrayList<HDObject> children;
    protected final ArrayList<HDObject> childRemoveList;

    protected PImage sprite;
    protected String imagePath;

    protected boolean killed;

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
        killed = true;
        if (parent != null) {
            parent.removeChildRecursively(this);
        }
    }

    @Override
    public Vector2D getLocation() {
        return new Vector2D(location);
    }

    @Override
    public void setLocation(Vector2D location) {
        this.location = new Vector2D(location);
    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public Vector2D getGlobalLocation() {
        if (parent == null) {
            return new Vector2D(location);
        } else {
            Vector2D pGLoc = parent.getGlobalLocation();
            double pGRot = parent.getGlobalRotation();
            double pGScale = parent.getGlobalScale();

            // Rotate and scale the local location
            Vector2D rotated = location.rotateRad(Math.toRadians(pGRot));
            Vector2D scaled = rotated.multiply(pGScale);

            // Add the parent's global location
            return pGLoc.add(scaled);
        }
    }

    @Override
    public double getGlobalRotation() {
        if (parent == null) {
            return rotation;
        } else {
            return parent.getGlobalRotation() + rotation;
        }
    }

    @Override
    public double getGlobalScale() {
        if (parent == null) {
            return scale;
        } else {
            return parent.getGlobalScale() * scale;
        }
    }

    @Override
    public void setGlobalLocation(Vector2D globalLocation) {
        if (parent == null) {
            setLocation(globalLocation);
        } else {
            Vector2D pGLoc = parent.getGlobalLocation();
            double pGRot = parent.getGlobalRotation();
            double pGScale = parent.getGlobalScale();

            // Reverse the transformations applied by the parent
            Vector2D relLoc = globalLocation.sub(pGLoc);
            Vector2D scaled = relLoc.divide(pGScale);
            Vector2D rotated = scaled.rotateRad(-Math.toRadians(pGRot));

            setLocation(rotated);
        }
    }

    @Override
    public void setGlobalRotation(double globalRotation) {
        if (parent == null) {
            setRotation(globalRotation);
        } else {
            double parentGlobalRotation = parent.getGlobalRotation();
            setRotation(globalRotation - parentGlobalRotation);
        }
    }

    @Override
    public void setGlobalScale(double globalScale) {
        if (parent == null) {
            setScale(globalScale);
        } else {
            double parentGlobalScale = parent.getGlobalScale();
            setScale(globalScale / parentGlobalScale);
        }
    }

    public HDObject getParent() {
        return parent;
    }

    public void setParent(HDObject parent) {
        this.parent = parent;
    }

    public ArrayList<HDObject> getChildren() {
        return children;
    }

    public PImage getSprite() {
        return sprite;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isKilled() {
        return killed;
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
