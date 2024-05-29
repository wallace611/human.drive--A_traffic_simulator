package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import static java.lang.Math.*;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected boolean arrived;
    protected double speed; // x meters per second
    protected Vector2D targetLocation;
    protected boolean killed;
    protected CollisionArea collision;
    protected CollisionArea frontCollision;

    public Vehicle(double speed) {
        this.speed = speed;
        collision = new CollisionArea(new Vector2D(), 0.0f, new Vector2D(50, 25));
        collision.setParent(this);
        frontCollision = new CollisionArea(new Vector2D(10, 0), 90f, new Vector2D(25, 50));
        frontCollision.setParent(this);
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
            } else {
                rotation = toDegrees(atan2(moveSpd.y, moveSpd.x));
                location.addOn(moveSpd);
            }
        } catch (Exception e) {
            arrived = true;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (parent != null) {
            parent.removeChildRecursively(this);
        }
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }

    public double getSpeed() {
        return speed;
    }

    public CollisionArea getCollision() {
        return new CollisionArea(
                collision.getLocation().add(location),
                collision.getRotation() + rotation,
                collision.getOffset()
        );
    }

    public CollisionArea getCollisionRef() {
        return collision;
    }

    public CollisionArea getFrontCollision() {
        return new CollisionArea(
                frontCollision.getLocation().add(location),
                frontCollision.getRotation() + rotation,
                frontCollision.getOffset()
        );
    }

    public CollisionArea getFrontCollisionRef() {
        return frontCollision;
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
