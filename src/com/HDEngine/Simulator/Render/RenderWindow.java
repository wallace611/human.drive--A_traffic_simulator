package com.HDEngine.Simulator.Render;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PApplet;

import static java.lang.Math.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class RenderWindow extends PApplet {
    private ArrayList<HDObject> objToRender;
    private Vector2D camLoc;
    private float camRot;
    private float camScale;
    private boolean renderCollisionArea;

    public RenderWindow(ArrayList<HDObject> renderListRef) {
        super();
        objToRender = renderListRef;
        camLoc = new Vector2D();
        camRot = 0.0f;
        camScale = 1.0f;
        renderCollisionArea = false;
    }

    @Override
    public void settings() {
        System.out.println("Simulator renderer thread: Loading settings...");
        size(800, 600);
        System.out.println("Simulator renderer thread: Done!");
    }

    @Override
    public void setup() {
        System.out.println("Simulator renderer thread: Setting everything up...");
        frameRate(100);
        background(200);
        translate(0, 0);
        surface.setVisible(false);
        System.out.println("Simulator renderer thread: All stuff has been set up, it's time for rendering something :D");
    }
    @Override
    public void draw() {
        background(200);
        float centerX = width >> 1;
        float centerY = height >> 1;

        translate(centerX, centerY);

        scale(camScale);
        rotate(camRot);
        translate((float) camLoc.x, (float) camLoc.y);
        translate(-centerX, -centerY);


        HDObject[] tmp = new ArrayList<>(objToRender).toArray(new HDObject[0]);
        for (HDObject object : tmp) {
            if (object.isKilled()) continue;
            pushMatrix();


            float x = (float) object.getGlobalLocation().x;
            float y = (float) object.getGlobalLocation().y;
            float r = radians((float) object.getGlobalRotation());
            float s = (float) object.getGlobalScale();

            translate(x, y);
            rotate(r);
            scale(s);
            if (object.getSprite() != null && object.getSprite().getImage() != null) {
                float w = object.getSprite().width;
                float h = object.getSprite().height;
                image(object.getSprite(), -w / 2, -h / 2);
            }

            popMatrix();
            if (renderCollisionArea) {
                pushMatrix();

                CollisionArea ca = null;
                if (object instanceof RoadChunk rc) {
                    ca = rc.getRoadArea();
                } else if (object instanceof Vehicle v) {
                    ca = v.getCollision();
                }
                if (ca != null) {
                    x = (float) ca.getGlobalLocation().x;
                    y = (float) ca.getGlobalLocation().y;
                    r = radians((float) ca.getGlobalRotation());
                    s = (float) ca.getGlobalScale();
                    float w = (float) ca.getOffset().x * 2;
                    float h = (float) ca.getOffset().y * 2;

                    translate(x, y);
                    rotate(r);
                    scale(s);
                    fill(173, 216, 230, 128);
                    rect(-w / 2, -h / 2, w, h);
                }

                popMatrix();
                if (object instanceof Vehicle v) {
                    ca = v.getFrontCollision();
                }
                pushMatrix();
                if (ca != null) {
                    x = (float) ca.getGlobalLocation().x;
                    y = (float) ca.getGlobalLocation().y;
                    r = radians((float) ca.getGlobalRotation());
                    s = (float) ca.getGlobalScale();
                    float w = (float) ca.getOffset().x * 2;
                    float h = (float) ca.getOffset().y * 2;

                    translate(x, y);
                    rotate(r);
                    scale(s);
                    fill(173, 216, 230, 128);
                    rect(-w / 2, -h / 2, w, h);
                }
                popMatrix();
            }
        }
    }

    @Override
    public void keyPressed() {
        float moveSpeed = 10;

        if (key == 'w') {
            camLoc.x += moveSpeed * sin(camRot);
            camLoc.y += moveSpeed * cos(camRot);
        }
        if (key == 's') {
            camLoc.x -= moveSpeed * sin(camRot);
            camLoc.y -= moveSpeed * cos(camRot);
        }
        if (key == 'a') {
            camLoc.x += moveSpeed * cos(camRot);
            camLoc.y -= moveSpeed * sin(camRot);
        }
        if (key == 'd') {
            camLoc.x -= moveSpeed * cos(camRot);
            camLoc.y += moveSpeed * sin(camRot);
        }
        if (key == '-') {
            camScale *= 1.1f;
        }
        if (key == '=') {
            camScale /= 1.1f;
        }
        if (key == 'e') {
            camRot += (float) toRadians(10);
        }
        if (key == 'q') {
            camRot -= (float) toRadians(10);
        }
    }

    public Vector2D getCamLoc() {
        return camLoc;
    }

    public float getCamRot() {
        return camRot;
    }

    public float getCamScale() {
        return camScale;
    }
}
