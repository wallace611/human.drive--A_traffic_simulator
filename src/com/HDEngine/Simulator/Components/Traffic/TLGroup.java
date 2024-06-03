package com.HDEngine.Simulator.Components.Traffic;

import java.util.Arrays;

public class TLGroup {
    private final int[] teamsSec;
    private double currentSec;

    public TLGroup(int[] teamsSec) {
        this.teamsSec = Arrays.copyOf(teamsSec, teamsSec.length);
        currentSec = 0.0f;
    }

    public int getTeamSec(int team) {
        return teamsSec[team];
    }

    public int getGreenTeam() {
        double tmpCurrentSec = currentSec;
        for (int i = 0; i < teamsSec.length; i++) {
            tmpCurrentSec -= teamsSec[i];
            if (tmpCurrentSec < 0) {
                return i;
            }
        }
        return 0;
    }

    public void nextTime(double deltaTime) {
        currentSec += deltaTime;
        int sumTime = 0;
        for (int i : teamsSec) {
            sumTime += i;
        }
        if (currentSec / sumTime > 1) {
            currentSec %= sumTime;
        }
    }
}
