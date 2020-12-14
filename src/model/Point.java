package model;

public class Point {

    private float x;
    private float z;
    private Vector3 normal;

    public Point(float x, float z, Vector3 norm) {
        this.x = x;
        this.z = z;
        this.normal = norm;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Vector3 getNormal() {
        return normal;
    }
}
