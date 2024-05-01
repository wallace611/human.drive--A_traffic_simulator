package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Components.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    protected enum ForwardState {
        IDLE, ACCELERATE, BREAK
    }
    protected enum TurningState {
        IDLE, LEFT, RIGHT
    }

    protected double currentSpeed; // x meters per second
    protected final double maxSpeed;
    protected final double maxTurningRate; // x degrees per second
    protected final double acceleration; // x meters per second^2
    protected final double deceleration;
    protected final int reactionDelay;
    protected Vector2D targetLocation;

    protected CollisionArea collision;

    protected ForwardState currentForwardState;
    protected TurningState currentTurningState;

    public Vehicle(double maxSpeed, double maxTurningRate, double acceleration, double deceleration, int reactionDelay) {
        this.currentSpeed = 0.0f;
        this.maxSpeed = maxSpeed;
        this.maxTurningRate = maxTurningRate;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.reactionDelay = reactionDelay;
        collision = new CollisionArea(new Vector2D(), 0.0f, new Vector2D(5, 5));
        targetLocation = null;
        currentForwardState = ForwardState.ACCELERATE;
        currentTurningState = TurningState.IDLE;
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
        switch (currentForwardState) {
            case ACCELERATE:
                currentSpeed += acceleration * deltaTime;
                if (currentSpeed > maxSpeed) {
                    currentSpeed = maxSpeed;
                }
                break;

            case BREAK:
                currentSpeed -= deceleration * deltaTime;
                if (currentSpeed < 0) {
                    currentSpeed = 0.0f;
                }
                break;

            case IDLE:
                break;
        }
        switch (currentTurningState) {
            case LEFT:
                rotation -= maxTurningRate * deltaTime;
                break;

            case RIGHT:
                rotation += maxTurningRate * deltaTime;
                break;

            case IDLE:
                break;
        }
        location.addOn(Vector2D.dPolarToCartesian(currentSpeed * deltaTime, rotation));
    }

    private void setActions() {

    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double getReactionDelay() {
        return reactionDelay;
    }

    public CollisionArea getCollision() {
        return collision;
    }

    public Vector2D getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Vector2D targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getCurrentForwardState() {
        return switch (currentForwardState) {
            case IDLE -> "IDLE";
            case ACCELERATE -> "ACCELERATE";
            case BREAK -> "BREAK";
        };
    }

    public void setCurrentForwardState(int state) {
        currentForwardState = switch (state) {
            case 0 -> ForwardState.IDLE;
            case 1 -> ForwardState.ACCELERATE;
            case 2 -> ForwardState.BREAK;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

    public String getCurrentTurningState() {
        return switch (currentTurningState) {
            case IDLE -> "IDLE";
            case LEFT -> "LEFT";
            case RIGHT -> "RIGHT";
        };
    }

    public void setCurrentTurningState(int state) {
        this.currentTurningState = switch (state) {
            case 0 -> TurningState.IDLE;
            case 1 -> TurningState.LEFT;
            case 2 -> TurningState.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }
}
