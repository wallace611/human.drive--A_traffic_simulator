package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Components.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected boolean arrived;
    protected double speed; // x meters per second
    protected Vector2D targetLocation;
    protected boolean killed;
    protected CollisionArea collision;

    public Vehicle(double speed) {
        this.speed = speed;
        collision = new CollisionArea(new Vector2D(), 0.0f, new Vector2D(5, 5));
        targetLocation = null;
        arrived = false;
        killed = false;
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);

        move(deltaTime);
    }

    private void move(double deltaTime) {
        Vector2D moveDir = targetLocation.sub(location);
        try {
            Vector2D moveSpd = moveDir.normalize().multiply(deltaTime * speed);
            if (moveDir.getMagnitude() < moveSpd.getMagnitude()) {
                arrived = true;
                System.out.println("arrived!");
            } else {
                location.addOn(moveSpd);
            }
        } catch (Exception e) {
            arrived = true;
            System.out.println("arrived!");
        }
    }

    @Override
    public void kill() {
        super.kill();

        System.out.println("good");
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }

    public double getSpeed() {
        return speed;
    }

    public CollisionArea getCollision() {
        return collision;
    }

    public Vector2D getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Vector2D targetLocation) {
        this.targetLocation = targetLocation;
        arrived = false;
    }

    public boolean isArrived() {
        return arrived;
    }
}
