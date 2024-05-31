package com.HDEngine.Simulator.Objects.Static;

import com.HDEngine.Simulator.Objects.HDObject;
import com.HDEngine.Utilities.Vector2D;

public class CollisionArea extends HDObject {
    // half of width and height
    private Vector2D offset;

    public CollisionArea(Vector2D location, double rotation, Vector2D offset) {
        super();

        this.location = new Vector2D(location);
        this.rotation = rotation;
        this.offset = new Vector2D(offset);
    }

    // return 4 vertex of the collision shape
    public Vector2D[] getVertex() {
        Vector2D[] vertices = new Vector2D[4];
        double globalRotation = getGlobalRotation();
        double globalScale = getGlobalScale();
        Vector2D globalLocation = getGlobalLocation();

        // 缩放偏移量
        Vector2D scaledOffset = offset.multiply(globalScale);

        vertices[0] = globalLocation.add(scaledOffset.rotateDeg(globalRotation));
        vertices[1] = globalLocation.add((new Vector2D(-scaledOffset.x, scaledOffset.y)).rotateDeg(globalRotation));
        vertices[2] = globalLocation.sub(scaledOffset.rotateDeg(globalRotation));
        vertices[3] = globalLocation.add((new Vector2D(scaledOffset.x, -scaledOffset.y)).rotateDeg(globalRotation));
        return vertices;
    }

    public boolean isOverlappingWith(CollisionArea others) {
        return areOverlapping(this, others);
    }

    public static boolean areOverlapping(CollisionArea ca1, CollisionArea ca2) {
        Vector2D[] rect1 = ca1.getVertex(), rect2 = ca2.getVertex();
        Vector2D[] normals = getNormals(rect1, rect2);

        for (Vector2D normal : normals) {
            if (!projectionsOverlap(normal, rect1, rect2)) {
                return false;
            }
        }
        return true;
    }

    private static Vector2D[] getNormals(Vector2D[]... rects) {
        Vector2D[] normals = new Vector2D[rects.length << 2];
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

    public Vector2D getOffset() {
        return new Vector2D(offset);
    }

    public void setOffset(Vector2D offset) {
        this.offset = new Vector2D(offset);
    }

}