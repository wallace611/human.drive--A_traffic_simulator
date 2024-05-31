package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Components.RoadDirectionManager;
import com.HDEngine.Utilities.Vector2D;

public class RoadChunk extends HDObject {
    private final RoadDirectionManager roadDir;
    private final CollisionArea roadArea;
    public boolean hasVehicle = false;

    // private ArrayList<Vehicle> children; from HDObject class, containing the cars which are heading here

    public RoadChunk() {
        this((byte) 0, new RoadChunk[0], new float[0]);
    }

    public RoadChunk(byte dirs, RoadChunk[] connectRoad, float[] roadWeight) {
        super();
        if (connectRoad.length != roadWeight.length) throw new RuntimeException("");
        roadDir = new RoadDirectionManager(dirs);
        for (int i = 0; i < connectRoad.length; i++) {
            roadDir.addRoadRef(connectRoad[i], roadWeight[i]);
        }
        roadArea = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(50, 50));
        roadArea.setParent(this);

    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
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

}
