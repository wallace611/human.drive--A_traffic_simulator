package com.HDEngine.Simulator.Components;

import com.HDEngine.Utilities.Vector2D;

public class CollisionArea {
    // the center location in the world dimension
    private Vector2D location;

    private double rotation;

    // half of width and height
    private Vector2D offset;

    public CollisionArea(Vector2D location, double rotation, Vector2D offset) {
        this.location = new Vector2D(location);
        this.rotation = rotation;
        this.offset = new Vector2D(offset);
    }

    // return 4 vertex of the collision shape
    public Vector2D[] getVertex() {
        Vector2D[] vertices = new Vector2D[4];
        double rot = rotation;
        vertices[0] = location.add(offset.rotateDeg(rot));
        vertices[1] = location.add((new Vector2D(-offset.x, offset.y)).rotateDeg(rot));
        vertices[2] = location.sub(offset.rotateDeg(rot));
        vertices[3] = location.add((new Vector2D(offset.x, -offset.y)).rotateDeg(rot));
        return vertices;
    }

    public boolean isOverlappingWith(CollisionArea others) {
        Vector2D[] rect1 = this.getVertex(), rect2 = others.getVertex();
        return areOverlapping(rect1, rect2);
    }

    public static boolean areOverlapping(Vector2D[] rect1, Vector2D[] rect2) {
        Vector2D[] normals = getNormals(rect1, rect2);

        for (Vector2D normal : normals) {
            if (!projectionsOverlap(normal, rect1, rect2)) {
                return false;
            }
        }
        return true;
    }

    private static Vector2D[] getNormals(Vector2D[]... rects) {
        Vector2D[] normals = new Vector2D[rects.length * 4];
        int ind = 0;
        for (Vector2D[] rect : rects) {
            for (int i = 0; i < rect.length; i++) {
                Vector2D edge = rect[i].sub(rect[(i + 1) % rect.length]);
                Vector2D normal = new Vector2D(-edge.y, edge.x);
                normals[ind++] = normal;
            }
        }
        return normals;
    }

    private static boolean projectionsOverlap(Vector2D normal, Vector2D[] rect1, Vector2D[] rect2) {
        double[] proj1 = project(normal, rect1);
        double[] proj2 = project(normal, rect2);
        return !(proj1[1] < proj2[0] || proj2[1] < proj1[0]);
    }

    private static double[] project(Vector2D normal, Vector2D[] rect) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (Vector2D vertex : rect) {
            double projection = vertex.dot(normal);
            if (projection < min) min = projection;
            if (projection > max) max = projection;
        }
        return new double[]{min, max};
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }
}
