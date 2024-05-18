package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Components.CollisionArea;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Components.RoadDirectionManager;
import com.HDEngine.Utilities.Vector2D;

import java.util.ArrayList;

public class RoadChunk extends HDObject {
    private final RoadDirectionManager roadDir;
    private CollisionArea roadArea;
    private ArrayList<Vehicle> cars; // the cars which are heading here

    private ArrayList<Vehicle> removeList;

    public RoadChunk(byte dirs, RoadChunk[] connectRoad, float[] roadWeight) {
        if (connectRoad.length != roadWeight.length) throw new RuntimeException("");
        roadDir = new RoadDirectionManager(dirs);
        for (int i = 0; i < connectRoad.length; i++) {
            roadDir.addRoadRef(connectRoad[i], roadWeight[i]);
        }
        roadArea = new CollisionArea(new Vector2D(0, 0), 0.0f, new Vector2D(10, 10));
        cars = new ArrayList<>();
        removeList = new ArrayList<>();
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);

        for (Vehicle c : cars) {
            c.tick(deltaTime);
            if (c.isArrived()) {
                carArrived(c);
            }
            if (c.isKilled()) {
                removeList.add(c);
            }
        }
        for (Vehicle c : removeList) {
            cars.remove(c);
        }
        removeList.clear();
    }

    public void spawnVehicle(Vehicle car) {
        carArrived(car);
        car.setLocation(location);
    }

    public void addVehicle(Vehicle car) {
        cars.add(car);
    }

    public void carArrived(Vehicle car) {
        try {
            RoadChunk newTarget = roadDir.accessRoad();
            newTarget.addVehicle(car);
            removeList.add(car);
            car.setTargetLocation(newTarget.getLocation());
        } catch (Exception e) {
            removeList.add(car);
            car.kill();
        }

    }

    public ArrayList<Vehicle> getCars() {
        return cars;
    }

    public CollisionArea getRoadArea() {
        return roadArea;
    }
}
