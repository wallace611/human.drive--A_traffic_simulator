package com.HDEngine.Simulator.Render;

import com.HDEngine.Simulator.Objects.Dynamic.Vehicle;
import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Simulator.Objects.Static.CollisionArea;
import com.HDEngine.Utilities.Vector2D;
import processing.core.PApplet;
import static java.lang.Math.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class RenderWindow extends PApplet {
    private ArrayList<HDObject> objToRender;
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
        size(800, 600);
    }

    @Override
    public void setup() {
        frameRate(100);
        background(200);
        translate(0, 0);
        surface.setVisible(false);
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
        // 平移回摄像机位置的相反方向
        translate(-centerX, -centerY);


        HDObject[] tmp = new ArrayList<>(objToRender).toArray(new HDObject[0]);
        for (HDObject object : tmp) {
            pushMatrix();
            if (object instanceof CollisionArea ca) {
                float x = (float) ((float) object.getParent().getLocation().x + object.getLocation().x);
                float y = (float) ((float) object.getParent().getLocation().y + object.getLocation().y);
                float r = radians((float) (object.getParent().getRotation() + object.getRotation()));
                float w = (float) ca.getOffset().x;
                float h = (float) ca.getOffset().y;

                translate(x, y);
                rotate(r);
                rect(-w / 2, -h / 2, w, h);
            } else {
                float x = (float) object.getLocation().x;
                float y = (float) object.getLocation().y;
                float r = radians((float) object.getRotation());
                float s = (float) object.getScale();
                float w = object.getSprite().width;
                float h = object.getSprite().height;

                translate(x, y);
                rotate(r);
                scale(s);
                image(object.getSprite(), -w / 2, -h / 2);
            }
            popMatrix();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RenderWindow(null);
        });
    }
}
