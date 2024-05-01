package com.HDEngine.Simulator.Objects.Dynamic;

import com.HDEngine.Simulator.Components.CollisionArea;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Vector2D;

public class Vehicle extends HDObject {
    private enum State {
        IDLE, ACCELERATE, BREAK;
    }

    private double currentSpeed;
    private final double maxSpeed;
    private final double acceleration;
    private final double deceleration;
    private final double reactionDelay;

    private CollisionArea collision;

    State currentState;

    public Vehicle(double maxSpeed, double acceleration, double deceleration, double reactionDelay) {
        this.currentSpeed = 0.0f;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.reactionDelay = reactionDelay;
        currentState = State.ACCELERATE;
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        switch (currentState) {
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
        location.addOn(Vector2D.dPolarToCartesian(currentSpeed * deltaTime, (rotation)));
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

    public String getCurrentState() {
        return switch (currentState) {
            case IDLE -> "IDLE";
            case ACCELERATE -> "ACCELERATE";
            case BREAK -> "BREAK";
        };
    }

    public void setCurrentState(int state) {
        switch (state) {
            case 0:
                currentState = State.IDLE;
                break;

            case 1:
                currentState = State.ACCELERATE;
                break;

            case 2:
                currentState = State.BREAK;
        }
    }
}
