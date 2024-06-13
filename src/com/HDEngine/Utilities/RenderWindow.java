package com.HDEngine.Utilities;

import com.HDEngine.Simulator.Info;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Settings;
import processing.core.PApplet;
import processing.core.PImage;

import static java.lang.Math.*;

import java.util.ArrayList;

public class RenderWindow extends PApplet {
    protected final ArrayList<HDObject> objToRender;
    protected Vector2D camLoc;
    protected float camRot;
    protected float camScale;
    protected int windowWidth, windowHeight;

    public RenderWindow(ArrayList<HDObject> renderListRef) {
        super();
        objToRender = renderListRef;
        camLoc = new Vector2D();
        camRot = 0.0f;
        camScale = 1.0f;
        windowWidth = Settings.windowWidth;
        windowHeight = Settings.windowHeight;
    }

    @Override
    public void settings() {
        System.out.println("Renderer thread: Loading settings...");
        size(windowWidth, windowHeight);
        System.out.println("Renderer thread: Done!");
    }

    @Override
    public void setup() {
        System.out.println("Renderer thread: Setting everything up...");
        frameRate(Settings.fps);
        background(200);
        translate(0, 0);
        surface.setVisible(false);
        System.out.println("Renderer thread: All stuff has been set up, it's time for rendering something :D");
    }
    @Override
    public void draw() {
        surface.setSize(windowWidth, windowHeight);
        background(200);
        float centerX = width >> 1;
        float centerY = height >> 1;

        setupCamera(centerX, centerY);

        HDObject[] tmp = new ArrayList<>(objToRender).toArray(new HDObject[0]);
        for (HDObject object : tmp) {
            if (object == null || object.isKilled()) continue;
            renderObject(object);

        }
        resetMatrix();
    }

    protected void renderObject(HDObject object) {
        pushMatrix();
        applyTransformations(object.getGlobalLocation(), object.getGlobalRotation(), object.getGlobalScale());
        if (object.getSprite() != null && object.getSprite().getImage() != null) {
            PImage sprite = object.getSprite();
            image(sprite, (float) -sprite.width / 2, (float) -sprite.height / 2);
        }
        popMatrix();
    }

    protected void setupCamera(float centerX, float centerY) {
        translate(centerX, centerY);
        scale(camScale);
        rotate(camRot);
        translate((float) camLoc.x, (float) camLoc.y);
        translate(-centerX, -centerY);
    }

    private void applyTransformations(Vector2D location, double rotation, double scale) {
        translate((float) location.x, (float) location.y);
        rotate(radians((float) rotation));
        scale((float) scale);
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

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

}
