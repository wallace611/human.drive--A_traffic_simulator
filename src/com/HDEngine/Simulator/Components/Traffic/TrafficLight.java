package com.HDEngine.Simulator.Components.Traffic;

import com.HDEngine.Simulator.Objects.HDObject;

public class TrafficLight extends HDObject {
    private int groupNumber;
    private int teamNumber;

    public TrafficLight(int groupNumber, int teamNumber) {
        this.groupNumber = groupNumber;
        this.teamNumber = teamNumber;
    }
}
