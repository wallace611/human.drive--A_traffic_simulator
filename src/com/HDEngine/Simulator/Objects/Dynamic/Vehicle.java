package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import static java.lang.Math.*;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected boolean arrived;
    protected double speed; // x meters per second
    protected double maxSpeed;
    protected double acceleration;
    protected double deceleration;
    protected MovingState movingState;
    protected Vector2D targetLocation;
    protected CollisionArea collision;
    protected CollisionArea frontCollision;

    public Vehicle(double maxSpeed) {
        speed = 100.0f;
        this.maxSpeed = maxSpeed;
        acceleration = 50.0f;
        deceleration = 80.0f;
        movingState = MovingState.IDLE;
        collision = new CollisionArea(new Vector2D(-20, 0), 0.0f, new Vector2D(30, 10));
        collision.setParent(this);
        frontCollision = new CollisionArea(new Vector2D(30, 0), 0.0f, new Vector2D(15, 15));
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

        setNextSpeed(deltaTime);
        move(deltaTime);
        movingState = MovingState.FORWARD;
    }

    private void setNextSpeed(double deltaTime) {
        switch (movingState) {
            case FORWARD:
                setSpeed(speed + acceleration * deltaTime);
                break;

            case BREAK:
                setSpeed(speed - acceleration * deltaTime);
                break;

            case IDLE:
                break;
        }
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

    public void setSpeed(double speed) {
        if (speed < 0) this.speed = 0;
        else this.speed = Math.min(speed, maxSpeed);
    }

    public CollisionArea getCollision() {
        return collision;
    }

    public CollisionArea getFrontCollision() {
        return frontCollision;
    }

    public void frontCollide(HDObject target) {
        if (target != this) {
            movingState = MovingState.BREAK;
        }
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
