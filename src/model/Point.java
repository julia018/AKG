package model;

public class Point {

    private float x;
    private float z;
    private Vector3 normal;
    private Vector2 uv;
    private float w;

    public Vector2 getUv() {
        return uv;
    }

    public Point(float x, float z, Vector3 norm, float w, Vector2 uv) {
        this.x = x;
        this.z = z;
        this.normal = norm;
        this.w = w;
        this.uv = uv;
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
