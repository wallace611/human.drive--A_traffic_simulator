package com.HDEngine.Utilities;

public interface ITransform {
    public Vector2D getLocation();

    public void setLocation(Vector2D location);

    public double getRotation();

    public void setRotation(double rotation);

    public double getScale();

    public void setScale(double scale);

    public Vector2D getGlobalLocation();

    public double getGlobalRotation();

    public double getGlobalScale();

    public void setGlobalLocation(Vector2D globalLocation);

    public void setGlobalRotation(double globalRotation);

    public void setGlobalScale(double globalScale);
}