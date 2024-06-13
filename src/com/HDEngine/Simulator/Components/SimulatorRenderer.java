package com.HDEngine.Simulator.Components;

import com.HDEngine.Simulator.Settings.Info;
import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Simulator.Objects.Static.RoadChunk;
import com.HDEngine.Simulator.Settings.Settings;
import com.HDEngine.Utilities.RenderWindow;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;

import static java.lang.Math.toDegrees;

public class SimulatorRenderer extends RenderWindow {
    private boolean[] isKeyPressed;
    private float camAcc;
    private float camMovXSpeed;
    private float camMovYSpeed;
    private float camRotSpeed;
    private float maxMovSpeed;
    private float maxRotSpeed;
    public SimulatorRenderer(ArrayList<HDObject> renderListRef) {
        super(renderListRef);
        windowWidth = Settings.windowWidth;
        windowHeight = Settings.windowHeight;
        isKeyPressed = new boolean[256];
        camAcc = 1.0f;
        camMovXSpeed = 0.0f;
        camMovYSpeed = 0.0f;
        maxMovSpeed = 50.0f;
        maxRotSpeed = 5.0f;
    }

    @Override
    public void settings() {
        super.settings();
    }

    @Override
    public void setup() {
        super.setup();
        frameRate(Settings.fps);
    }

    @Override
    public void draw() {
        super.draw();

        getKeyActions();
        frameRate(Settings.fps);
        renderWindowState();
    }

    private void renderWindowState() {
        textSize(15);
        fill(0);
        text(String.format("FPS: %d\nTPS: %d",(int) frameRate, (int) Info.tps), 10, 15);
        if (Settings.speed > 1.0001 || Settings.speed < 0.9999) {
            text(String.format("Speed: %.1f", Settings.speed), 10, 75);
        }
    }

    @Override
    protected void renderObject(HDObject object) {
        pushMatrix();
        applyTransformations(object.getGlobalLocation(), object.getGlobalRotation(), object.getGlobalScale());
        if (object.getSprite() != null && object.getSprite().getImage() != null) {
            PImage sprite = object.getSprite();
            image(sprite, (float) -sprite.width / 2, (float) -sprite.height / 2);
            if (object instanceof RoadChunk rc && rc.isTrafficLight()) {
                renderRCTrafficLight(rc);
            }
        }
        popMatrix();

        if (Settings.debugMode) {
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
            renderInfo(object);
        }

    }

    private void renderInfo(HDObject object) {
        pushMatrix();
        applyTransformations(object.getGlobalLocation(), -toDegrees(camRot), 1.0f);
        Vector2D axis = object.getGlobalLocation();
        textSize(1 / camScale * 10);
        fill(0);
        if (object instanceof Vehicle v) {
            text(String.format("%.1f, %.1f", axis.x, axis.y), -30, -20);
            text(String.format("%.1f\nheading to %s\nignore TL: %b\nstate: %s\nstop time: %f\ncollided obj: %s",
                            v.getSpeed(),
                            v.getTargetRoadChunk(),
                            v.isIgnoreTrafficLight(),
                            v.getMovingState(),
                            v.getStopTime(),
                            v.getFrontVehicle()),
                    -30, 20
            );

        } else if (object instanceof RoadChunk rc) {
            text(rc.toString(), -40, -40);
        }
        popMatrix();
    }

    private void renderRCTrafficLight(RoadChunk rc) {
        if (rc.isTrafficLightGreen()) {
            fill(0, 255, 0, 150);
        } else {
            fill(255, 0, 0, 150);
        }
        drawLowPolyCircle((float) 0, (float) 0, 20, 20);
    }

    void drawLowPolyCircle(float x, float y, float radius, int numPoints) {
        float angleStep = TWO_PI / numPoints;
        beginShape();
        for (float angle = 0; angle < TWO_PI; angle += angleStep) {
            float sx = x + cos(angle) * radius;
            float sy = y + sin(angle) * radius;
            vertex(sx, sy);
        }
        endShape(CLOSE);
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

    private void getKeyActions() {
        float friction = 0.9f;

        if (isKeyPressed['W']) {
            camMovXSpeed += camAcc * sin(camRot);
            camMovYSpeed += camAcc * cos(camRot);
        }
        if (isKeyPressed['S']) {
            camMovXSpeed -= camAcc * sin(camRot);
            camMovYSpeed -= camAcc * cos(camRot);
        }
        if (isKeyPressed['A']) {
            camMovXSpeed += camAcc * cos(camRot);
            camMovYSpeed -= camAcc * sin(camRot);
        }
        if (isKeyPressed['D']) {
            camMovXSpeed -= camAcc * cos(camRot);
            camMovYSpeed += camAcc * sin(camRot);
        }
        if (isKeyPressed['Q']) {
            camRotSpeed += 0.01;
        }
        if (isKeyPressed['E']) {
            camRotSpeed -= 0.01;
        }
        if (isKeyPressed['=']) {
            camScale *= 1.01f;
        }
        if (isKeyPressed['-']) {
            camScale /= 1.01f;
        }

        camMovXSpeed = constrain(camMovXSpeed, -maxMovSpeed, maxMovSpeed);
        camMovYSpeed = constrain(camMovYSpeed, -maxMovSpeed, maxMovSpeed);
        camRotSpeed = constrain(camRotSpeed, -maxRotSpeed, maxRotSpeed);

        camLoc.x += camMovXSpeed;
        camLoc.y += camMovYSpeed;
        camRot += camRotSpeed;

        camMovXSpeed *= friction;
        camMovYSpeed *= friction;
        camRotSpeed *= friction;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        isKeyPressed[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
        isKeyPressed[event.getKeyCode()] = false;
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);
        System.out.println(event.getCount());
    }
}
