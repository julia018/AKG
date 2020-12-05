package model;

public class Vector3 {

    private float x;
    private float y;
    private float z;
    private float[] vector4;


    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vector4 = new float[]{x, y, z, 1};
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

    public float[] getVector4() {
        return vector4;
    }

    public float getVectorElement(int i) {
        return this.vector4[i];
    }

    public void setVectorElement(int i, float value) {
        this.vector4[i] = value;
        if(i == 0) {
            this.x = value;
        } else if(i == 1) {
            this.y = value;
        } else if(i == 2) {
            this.z = value;
        }
    }

    public void divideByW() {
        this.setVectorElement(0, this.vector4[0] / this.vector4[3]);
        this.setVectorElement(1, this.vector4[1] / this.vector4[3]);
        this.setVectorElement(2, this.vector4[2] / this.vector4[3]);
    }

    public void addValueToVectorElement(int index, double value) {
        this.vector4[index] += value;
        if(index == 0) {
            this.x += value;
        } else if(index == 1) {
            this.y += value;
        } else if(index == 2) {
            this.z += value;
        }
    }

    public String toString() {
        String res = " ";
        res += vector4[0] + " " + vector4[1] + " " + vector4[2];
        return res;
    }
}
