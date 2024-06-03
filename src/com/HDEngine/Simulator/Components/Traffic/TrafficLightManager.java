package com.HDEngine.Simulator.Components.Traffic;

import java.util.HashMap;
import java.util.Map;

public class TrafficLightManager {
    public static Map<Integer, TLGroup> tLGroupsSecs = new HashMap<>();

    public static int getSecs(int group, int team) {
        return tLGroupsSecs.get(group).getTeamSec(team);
    }

    public static int getGreenTeam(int group) {
        return tLGroupsSecs.get(group).getGreenTeam();
    }

    public static void nextTime(double timePassed) {
        for (TLGroup group : tLGroupsSecs.values()) {
            group.nextTime(timePassed);
        }
    }
}

