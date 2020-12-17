package model;

public class Point {

    private float x;
    private float z;
    private Vector3 normal;
    private float w;

    public Point(float x, float z, Vector3 norm, float w) {
        this.x = x;
        this.z = z;
        this.normal = norm;
        this.w = w;

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

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }
}
