package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Components.Traffic.TrafficLightManager;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Components.RoadDirectionManager;
import com.HDEngine.Utilities.Vector2D;

public class RoadChunk extends HDObject {
    private final RoadDirectionManager roadDir;
    private final CollisionArea roadArea;
    public boolean hasVehicle = false;
    private boolean hasTrafficLight;
    private boolean intersection;
    private int trafficLightGroup;
    private int trafficLightTeam;
    private boolean trafficLightGreen;
    private double speedLimit;

    // private ArrayList<Vehicle> children; from HDObject class, containing the cars which are heading here

    public RoadChunk() {
        this((byte) 0, new RoadChunk[0], new float[0]);
    }

    public RoadChunk(byte dirs) {
        super();
        roadDir = new RoadDirectionManager(dirs);
        roadArea = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(50, 50));
        roadArea.setParent(this);
        hasTrafficLight = false;
        speedLimit = 100.0f;
    }

    public RoadChunk(byte dirs, RoadChunk[] connectRoad, float[] roadWeight) {
        this(dirs);
        if (connectRoad.length != roadWeight.length) throw new RuntimeException("");
        for (int i = 0; i < connectRoad.length; i++) {
            roadDir.addRoadRef(connectRoad[i], roadWeight[i]);
        }
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        if (hasTrafficLight) {
            trafficLightGreen = TrafficLightManager.getGreenTeam(trafficLightGroup) == trafficLightTeam;
        }
        for (HDObject object : children) {
            if (object instanceof Vehicle c) {
                c.tick(deltaTime);
                if (c.isArrived()) {
                    carArrived(c);
                }
            }
        }
        for (HDObject object : childRemoveList) {
            if (object instanceof Vehicle c) {
                children.remove(c);
            }
        }
        childRemoveList.clear();
    }

    public void spawnVehicle(Vehicle car) {
        carArrived(car);
        car.setGlobalLocation(getGlobalLocation());
    }

    public void carArrived(Vehicle car) {
        try {
            Vector2D locTmp = car.getGlobalLocation();
            RoadChunk newTarget = roadDir.accessRoad();
            newTarget.addChild(car);
            childRemoveList.add(car);
            car.setTargetRoadChunk(newTarget);
            car.setGlobalLocation(locTmp);
            if (newTarget.intersection) {
                car.setIgnoreTrafficLight(true);
            } else if (car.isIgnoreTrafficLight() && newTarget.hasTrafficLight) {
                car.setIgnoreTrafficLight(true);
            } else {
                car.setIgnoreTrafficLight(false);
            }
        } catch (Exception e) {
            car.kill();
            childRemoveList.add(car);
        }

    }

    public CollisionArea getRoadArea() {
        return roadArea;
    }

    public void addRoad(RoadChunk rc, float weight) {
        roadDir.addRoadRef(rc, weight);
    }

    public boolean isTrafficLight() {
        return hasTrafficLight;
    }

    public int getTrafficLightGroup() {
        return trafficLightGroup;
    }

    public int getTrafficLightTeam() {
        return trafficLightTeam;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }

    public void setTrafficLight(int group, int team) {
        if (group == -1) {
            hasTrafficLight = false;
            return;
        }
        hasTrafficLight = true;
        trafficLightGroup = group;
        trafficLightTeam = team;
    }

    public boolean isTrafficLightGreen() {
        return trafficLightGreen;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public String toString() {
        return "RoadChunk" + String.format("%d, %d", (int) getGlobalLocation().y / 100, (int) getGlobalLocation().x / 100);
    }
}
