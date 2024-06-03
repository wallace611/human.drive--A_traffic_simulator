package com.HDEngine;

import com.HDEngine.Simulator.Components.Traffic.TLGroup;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        TLGroup tl = new TLGroup(new int[]{2, 3, 4, 5});
        for (int i = 0; i < 100; i++) {
            System.out.println(tl.getGreenTeam());
            tl.nextTime(1);
        }
    }
}
