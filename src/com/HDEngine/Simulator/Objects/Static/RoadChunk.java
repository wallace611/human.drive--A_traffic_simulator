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
    private int trafficLightGroup;
    private int trafficLightTeam;
    private boolean trafficLightGreen;

    // private ArrayList<Vehicle> children; from HDObject class, containing the cars which are heading here

    public RoadChunk() {
        this((byte) 0, new RoadChunk[0], new float[0]);
    }

    public RoadChunk(byte dirs) {
        super();
        roadDir = new RoadDirectionManager(dirs);
        roadArea = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(50, 50));
        roadArea.setParent(this);
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
            car.setTargetLocation(newTarget.getGlobalLocation());
            car.setGlobalLocation(locTmp);
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
}
