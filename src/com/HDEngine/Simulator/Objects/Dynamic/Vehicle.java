package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import static java.lang.Math.*;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected boolean arrived;
    protected double speed; // x meters per second
    protected Vector2D targetLocation;
    protected CollisionArea collision;
    protected CollisionArea frontCollision;

    public Vehicle(double speed) {
        this.speed = speed;
        collision = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(50, 25));
        collision.setParent(this);
        frontCollision = new CollisionArea(new Vector2D(50, 0), 0.0f, new Vector2D(10, 10));
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
        if (targetLocation == null) {
            arrived = true;
            return;
        }

        Vector2D currentLocation = getGlobalLocation();
        Vector2D moveDir = targetLocation.sub(currentLocation);
        double distanceToTarget = moveDir.getMagnitude();

        if (distanceToTarget == 0) {
            arrived = true;
            return;
        }

        Vector2D moveSpd = moveDir.normalize().multiply(deltaTime * speed);
        double moveDistance = moveSpd.getMagnitude();

        if (distanceToTarget <= moveDistance) {
            // 如果即将移动的距离大于或等于到目标的距离，认为已到达
            setGlobalLocation(targetLocation); // 直接设置位置为目标位置
            arrived = true;
        } else {
            // 更新位置和旋转
            rotation = toDegrees(atan2(moveDir.y, moveDir.x));
            Vector2D newLocation = currentLocation.add(moveSpd);
            setGlobalLocation(newLocation); // 更新全局位置
            arrived = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (parent != null) {
            parent.removeChildRecursively(this);
        }
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

    public CollisionArea getFrontCollision() {
        return frontCollision;
    }

    public void collide(Vehicle target) {

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
