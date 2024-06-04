package com.HDEngine.Utilities.Render;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Settings;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PApplet;
import processing.core.PImage;

import static java.lang.Math.*;

import java.util.ArrayList;

public class RenderWindow extends PApplet {
    private final ArrayList<HDObject> objToRender;
    private Vector2D camLoc;
    private float camRot;
    private float camScale;

    public RenderWindow(ArrayList<HDObject> renderListRef) {
        super();
        objToRender = renderListRef;
        camLoc = new Vector2D();
        camRot = 0.0f;
        camScale = 1.0f;
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
        frameRate(Settings.fps);
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

        setupCamera(centerX, centerY);

        HDObject[] tmp = new ArrayList<>(objToRender).toArray(new HDObject[0]);
        for (HDObject object : tmp) {
            if (object.isKilled()) continue;
            renderObject(object);

        }
    }

    private void setupCamera(float centerX, float centerY) {
        translate(centerX, centerY);
        scale(camScale);
        rotate(camRot);
        translate((float) camLoc.x, (float) camLoc.y);
        translate(-centerX, -centerY);
    }

    private void renderObject(HDObject object) {
        pushMatrix();
        applyTransformations(object.getGlobalLocation(), object.getGlobalRotation(), object.getGlobalScale());
        if (object.getSprite() != null && object.getSprite().getImage() != null) {
            PImage sprite = object.getSprite();
            image(sprite, (float) -sprite.width / 2, (float) -sprite.height / 2);
            if (object instanceof RoadChunk rc && rc.isTrafficLight()) {
                if (rc.isTrafficLightGreen()) {
                    fill(0, 255, 0, 150);
                } else {
                    fill(255, 0, 0, 150);
                }
                circle(0, 0, 50);
            }
        }
        popMatrix();

        if (Settings.renderCollision) {
            CollisionArea ca = getCollisionArea(object);
            if (ca != null) {
                renderCollisionArea(ca);
            }
            if (object instanceof Vehicle v) {
                ca = v.getFrontCollision();
                if (ca != null) {
                    renderCollisionArea(ca);
                }
            }
        }
    }

    private void renderCollisionArea(CollisionArea ca) {
        pushMatrix();
        applyTransformations(ca.getGlobalLocation(), ca.getGlobalRotation(), ca.getGlobalScale());
        if (ca.renderCollided) {
            fill(200, 100, 100, 128);
        } else {
            fill(173, 216, 230, 128);
        }
        float w = (float) ca.getOffset().x * 2;
        float h = (float) ca.getOffset().y * 2;
        rect(-w / 2, -h / 2, w, h);
        popMatrix();
    }

    private void applyTransformations(Vector2D location, double rotation, double scale) {
        translate((float) location.x, (float) location.y);
        rotate(radians((float) rotation));
        scale((float) scale);
    }

    private CollisionArea getCollisionArea(HDObject object) {
        if (object instanceof RoadChunk rc) {
            return rc.getRoadArea();
        } else if (object instanceof Vehicle v) {
            return v.getBackCollision();
        }
        return null;
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
