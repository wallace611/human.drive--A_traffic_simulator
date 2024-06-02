package com.HDEngine;

import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Render.RenderWindow;
import com.HDEngine.Utilities.*;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        HDObject object1 = new HDObject();
        object1.setLocation(new Vector2D(20, 20));
        System.out.println(object1.getLocation());
        System.out.println(object1.getGlobalLocation());
        HDObject object2 = new HDObject();
        object1.addChild(object2);
        object2.setGlobalLocation(new Vector2D(0, 0));
        System.out.println(object2.getLocation());
        System.out.println(object2.getGlobalLocation());

        object1.setRotation(180);
        object1.setScale(2.0);
        System.out.println(object2.getLocation());
        System.out.println(object2.getGlobalLocation());
    }

    private int tmp = 0;
    public void test(RenderWindow sw) {
        sw.background(tmp++);
    }
}
