package com.HDEngine.Utilities;

import static java.lang.Math.*;

public class Vector2D {
    public double x, y;

    public Vector2D() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        if (v != null) {
            x = v.x;
            y = v.y;
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public String toString() {
        return String.format("x: %f, y: %f", x, y);
    }

    public double getDistance(Vector2D others) {
        if (others == null) throw new NullPointerException();
        return sqrt( (x - others.x) * (x - others.x) + (y - others.y) * (y - others.y) );
    }

    public static double distance(Vector2D v1, Vector2D v2) {
        if (v1 == null || v2 == null) throw new NullPointerException();
        return sqrt( (v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y) );
    }

    public double getMagnitude() {
        return sqrt( x * x + y * y );
    }

    public Vector2D normalize() {
        return new Vector2D(x / getMagnitude(), y / getMagnitude());
    }

    public Vector2D rotateDeg(double d) {
        d = toRadians(d);
        return new Vector2D(x * cos(d) - y * sin(d), x * sin(d) + y * cos(d));
    }

    public Vector2D rotateRad(double r) {
        return new Vector2D(x * cos(r) - y * sin(r), x * sin(r) + y * cos(r));
    }

    // operators
    public Vector2D add(Vector2D others) {
        if (others == null) throw new NullPointerException();
        return new Vector2D(x + others.x, y + others.y);
    }

    public void addOn(Vector2D others) {
        if (others == null) throw new NullPointerException();
        x += others.x;
        y += others.y;
    }

    public Vector2D sub(Vector2D others) {
        if (others == null) throw new NullPointerException();
        return new Vector2D(x - others.x, y - others.y);
    }

    public void subOn(Vector2D others) {
        if (others == null) throw new NullPointerException();
        x -= others.x;
        y -= others.y;
    }

    public Vector2D multiply(double n) {
        return new Vector2D(x * n, y * n);
    }

    public void multiplyBy(double n) {
        x *= n;
        y *= n;
    }

    public Vector2D divide(double n) {
        return new Vector2D(x / n, y / n);
    }

    public void divideBy(double n) {
        x /= n;
        y /= n;
    }

}
