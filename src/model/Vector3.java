package model;

public class Vector3 {

    private float x;
    private float y;
    private float z;


    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 getNormalized() {
        float length = getLength();
        float normX = this.x / length;
        float normY = this.y / length;
        float normZ = this.z / length;
        return new Vector3(normX, normY, normZ);
    }

    private float getLength() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 substractVector(Vector3 vector) {
        return new Vector3(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Vector3 getCrossProduct(Vector3 vector) {
        return new Vector3(this.y * vector.z - this.z * vector.y, this.z * vector.x - this.x * vector.z, this.x * vector.y - this.y * vector.x);
    }

    public float getScalarProduct(Vector3 vector) {
        return (this.x * vector.x + this.y * vector.y + this.z * vector.z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
