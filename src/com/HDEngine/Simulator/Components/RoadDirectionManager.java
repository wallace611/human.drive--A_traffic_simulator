package com.HDEngine.Simulator.Components;

import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Utilities.Vector2D;

import java.security.SecureRandom;

public class RoadDirectionManager {
    private final byte roadDirection;
    /*   8 bits represents 8 direction:
     *
     *   3      2      1
     *          ^
     *          |
     *   4 < -- ● -- > 0
     *          |
     *          ˇ
     *   5      6      7  st bit
     *
     *   for example:
     *
     *   b'01010000        b'10010011
     *
     *                          /
     *      -- ●            -- ● --
     *         |                \
     */
    private final RoadChunk[] roadRef;
    private final float[] roadWeight;
    private int currentRoadCount;

    public RoadDirectionManager(byte dir) {
        roadDirection = dir;
        // count how many 1s (connected roads) in dir
        int count = 0;
        while (dir != 0) {
            dir &= (byte) (dir - 1);
            count += 1;
        }
        roadRef = new RoadChunk[count];
        roadWeight = new float[count];
        currentRoadCount = 0;
    }

    public void addRoadRef(RoadChunk rc, float weight) {
        if (isConstructed()) throw new RuntimeException("the road chunk had already been constructed");
        roadRef[currentRoadCount] = rc;
        roadWeight[currentRoadCount] = weight;
        currentRoadCount += 1;
    }

    // access the next road randomly
    public RoadChunk accessRoad() {
        if (!isConstructed()) throw new RuntimeException("the road chunk hasn't been constructed");
        if (roadDirection == 0) throw new RuntimeException("finish!");
        float sumWeight = 0;
        for (float w : roadWeight) {
            sumWeight += w;
        }
        if (sumWeight == 0.0f) sumWeight = 1.0f;

        float[] cumulativeSum = new float[roadWeight.length];
        cumulativeSum[0] = roadWeight[0];
        for (int i = 1; i < cumulativeSum.length; i++) {
            cumulativeSum[i] = cumulativeSum[i - 1] + roadWeight[i];
        }
        float randomNumber = (new SecureRandom()).nextFloat(sumWeight);

        int start = 0, end = roadWeight.length - 1;
        while (start < end) {
            int mid = (start + end) >> 1;
            if (randomNumber <= cumulativeSum[mid]) end = mid;
            else start = mid + 1;
        }
        return roadRef[start];
    }

    public boolean isConstructed() {
        return currentRoadCount == roadRef.length;
    }

    public byte getRoadDirection() {
        return roadDirection;
    }

    public int getCurrentRoadCount() {
        return currentRoadCount;
    }

    public RoadChunk[] getRoadRef() {
        return roadRef;
    }
}
