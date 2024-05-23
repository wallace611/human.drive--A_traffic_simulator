package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Components.RoadDirectionManager;
import com.HDEngine.Utilities.Vector2D;

import java.util.ArrayList;

public class RoadChunk extends HDObject {
    private final RoadDirectionManager roadDir;
    private CollisionArea roadArea;

    // private ArrayList<Vehicle> children; from HDObject class, containing the cars which are heading here

    private ArrayList<Vehicle> removeList;

    public RoadChunk(byte dirs, RoadChunk[] connectRoad, float[] roadWeight) {
        if (connectRoad.length != roadWeight.length) throw new RuntimeException("");
        roadDir = new RoadDirectionManager(dirs);
        for (int i = 0; i < connectRoad.length; i++) {
            roadDir.addRoadRef(connectRoad[i], roadWeight[i]);
        }
        roadArea = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(10, 10));
        roadArea.setParent(this);

        removeList = new ArrayList<>();
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);

        for (HDObject object : children) {
            if (object instanceof Vehicle) {
                Vehicle c = (Vehicle) object;
                c.tick(deltaTime);
                if (c.isArrived()) {
                    carArrived(c);
                }
                if (c.isKilled()) {
                    removeList.add(c);
                }
            }
        }
        for (Vehicle c : removeList) {
            children.remove(c);
        }
        removeList.clear();
    }

    public void spawnVehicle(Vehicle car) {
        carArrived(car);
        car.setLocation(location);
    }

    public void carArrived(Vehicle car) {
        try {
            RoadChunk newTarget = roadDir.accessRoad();
            newTarget.addChild(car);
            setParent(newTarget);
            removeList.add(car);
            car.setTargetLocation(newTarget.getLocation());
        } catch (Exception e) {
            removeList.add(car);
            car.kill();
        }

    }

    public CollisionArea getRoadArea() {
        return new CollisionArea(
                roadArea.getLocation().add(location),
                0,
                roadArea.getOffset());
    }
}
