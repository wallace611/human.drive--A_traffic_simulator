package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import static java.lang.Math.*;

import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Settings;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected boolean arrived;
    protected double speed; // x meters per second
    protected double maxSpeed;
    protected double accelerationRate;
    protected double decelerationRate;
    protected double stopTime;
    protected final int timeout;
    protected MovingState movingState;
    protected RoadChunk targetRoadChunk;
    protected CollisionArea backCollision;
    protected final double bcWidth;
    protected final double bcHeight;
    protected CollisionArea frontCollision;
    protected final double fcWidth;
    protected final double fcHeight;
    protected Vehicle frontVehicle;
    protected boolean ignoreTrafficLight;
    protected boolean waitingTrafficLight;

    public Vehicle(double maxSpeed) {
        speed = 100.0f;
        this.maxSpeed = maxSpeed;
        accelerationRate = 0.2f;
        decelerationRate = 0.8f;
        stopTime = 0.0f;
        timeout = Settings.congestionTimeout;
        movingState = MovingState.IDLE;
        bcWidth = 8;
        bcHeight = 30;
        backCollision = new CollisionArea(new Vector2D(-5, 0), 0.0f, new Vector2D(bcHeight, bcWidth));
        backCollision.setParent(this);
        fcWidth = 15;
        fcHeight = 15;
        frontCollision = new CollisionArea(new Vector2D(20, 0), 0.0f, new Vector2D(fcHeight, fcWidth));
        frontCollision.setParent(this);
        targetRoadChunk = null;
        arrived = false;
        killed = false;
        frontVehicle = null;
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);

        nextState();
        setNextSpeed(deltaTime);
        if (speed < 1e-2) {
            stopTime += deltaTime;
            if (Settings.killCongestedVehicle && stopTime >= timeout) {
                kill();
            }
        } else {
            stopTime = 0.0f;
        }
        resizeCollisionShapeAccordingToSpeed();
        move(deltaTime);
        frontVehicle = null;
    }

    private void setNextSpeed(double deltaTime) {
        switch (movingState) {
            case FORWARD:
                setSpeed(speed + accelerationRate * maxSpeed * deltaTime);
                break;

            case BREAK:
                if (frontVehicle != null) {
                    double dist = Vector2D.distance(frontVehicle.getGlobalLocation(), getGlobalLocation());
                    setSpeed(speed - decelerationRate * maxSpeed / dist * 100 * deltaTime);
                } else {
                    setSpeed(speed - decelerationRate * maxSpeed * deltaTime);
                }
                break;

            case IDLE:
                break;
        }
    }

    private void nextState() {
        // speed limit
        if (targetRoadChunk.getSpeedLimit() != -1) {
            if (targetRoadChunk.getSpeedLimit() < speed) {
                movingState = MovingState.BREAK;
            } else {
                movingState = MovingState.FORWARD;
            }
        } else {
            movingState = MovingState.FORWARD;
        }
        if (Settings.speedUpAtIntersection && (targetRoadChunk.isIntersection() || targetRoadChunk.isTrafficLight())) {
            movingState = MovingState.FORWARD;
        }
        // traffic light
        waitingTrafficLight = false;
        if (!ignoreTrafficLight && targetRoadChunk.isTrafficLight() && !targetRoadChunk.isTrafficLightGreen()) {
            movingState = MovingState.BREAK;
            waitingTrafficLight = true;
            stopTime = 0.0f;
        }
        if (frontVehicle != null) {
            movingState = MovingState.BREAK;
            if (frontVehicle.isWaitingTrafficLight()) {
                stopTime = 0.0f;
            }
        }
    }

    private void resizeCollisionShapeAccordingToSpeed() {
        Vector2D fcMag = new Vector2D(
                (speed + 1) / (100 + 1) * fcHeight,
                (speed - 50) / (100 + 1) * fcWidth
        );
        Vector2D bcMag = new Vector2D(
                (maxSpeed + 50) / (speed + (maxSpeed + 50)) / 1.2 * bcHeight,
                bcWidth
        );
        frontCollision.setOffset(fcMag);
        backCollision.setOffset(bcMag);
    }

    private void move(double deltaTime) {
        if (targetRoadChunk == null) {
            arrived = true;
            return;
        }

        Vector2D currentLocation = getGlobalLocation();
        Vector2D moveDir = targetRoadChunk.getGlobalLocation().sub(currentLocation);
        double distanceToTarget = moveDir.getMagnitude();

        if (distanceToTarget == 0) {
            arrived = true;
            return;
        }

        Vector2D moveSpd = moveDir.normalize().multiply(deltaTime * speed);
        double moveDistance = moveSpd.getMagnitude();

        if (distanceToTarget <= moveDistance) {
            // 如果即将移动的距离大于或等于到目标的距离，认为已到达
            setGlobalLocation(targetRoadChunk.getGlobalLocation()); // 直接设置位置为目标位置
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed < 0) this.speed = 0;
        else this.speed = Math.min(speed, maxSpeed);
    }

    public MovingState getMovingState() {
        return movingState;
    }

    public CollisionArea getBackCollision() {
        return backCollision;
    }

    public CollisionArea getFrontCollision() {
        return frontCollision;
    }

    public boolean backCollided(HDObject target) {
        return true;
    }

    public boolean frontCollided(HDObject target) {
        if (target != this && target instanceof Vehicle v) {
            frontVehicle = v;
            return true;
        }
        return false;
    }

    public Vector2D getTargetLocation() {
        return targetRoadChunk.getGlobalLocation();
    }

    public RoadChunk getTargetRoadChunk() {
        return targetRoadChunk;
    }

    public void setTargetRoadChunk(RoadChunk targetRoadChunk) {
        this.targetRoadChunk = targetRoadChunk;
        arrived = false;
    }

    public boolean isArrived() {
        return arrived;
    }

    public boolean isIgnoreTrafficLight() {
        return ignoreTrafficLight;
    }

    public void setIgnoreTrafficLight(boolean ignoreTrafficLight) {
        this.ignoreTrafficLight = ignoreTrafficLight;
    }

    public double getStopTime() {
        return stopTime;
    }

    public boolean isWaitingTrafficLight() {
        return waitingTrafficLight;
    }

    public Vehicle getFrontVehicle() {
        return frontVehicle;
    }
}
