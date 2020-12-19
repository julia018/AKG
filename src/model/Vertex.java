package model;

import logic.Transformation;

public class Vertex implements Comparable<Vertex>, Cloneable{

    private Vector3 position;
    private Vector3 newPosition; // position after all stuff for projection
    private Vector3 newObserverPosition;
    private Vector3 normal;
    private Vector3 newNormal;
    private float w;
    private Vector2 uv; // for texture

    public Vertex(Vector3 position, Vector3 normal, Vector2 uv) {
        this.position = position;
        this.normal = normal;
        this.uv = uv;
        this.newPosition = position;
        this.newObserverPosition = position;
        this.newNormal = normal;
        this.w = 1;
    }

    public Vector3 getPosition() {
        return position;
    }

    public String toString() {
        String res = " ";
        res += position.getX() + " " + position.getY() + " " + position.getZ();
        return res;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Vector3 newPosition) {
        this.newPosition = newPosition;
        this.w = newPosition.getVectorElement(3);
    }

    @Override
    public int compareTo(Vertex o) {
        if(this.getNewPosition().getX() < o.getNewPosition().getX()) {
            return -1;
        } else if(this.getNewPosition().getX() == o.getNewPosition().getX()) {
            if(this.getNewPosition().getY() > o.getNewPosition().getY()) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public Vector3 getNormal() {
        return normal;
    }

    public Vector3 getNewNormal() {
        return newNormal;
    }

    public void setNewNormal(Vector3 newNormal) {
        this.newNormal = newNormal;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public Vector3 transformNormal(Transformation transf, Vector3 vect, Transformation observer) {
        Transformation matr3by3  = transf.get3by3Matrix();
        matr3by3 = matr3by3.getInversedMAtrix3().transpose3();
        Vector3 resNorm = matr3by3.multiplyByVector3(vect);
        //resNorm = observer.multiplyByVector(resNorm);
        return resNorm;
    }

    public Vector3 getNewObserverPosition() {
        return newObserverPosition;
    }

    public void setNewObserverPosition(Vector3 newObserverPosition) {
        this.newObserverPosition = newObserverPosition;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO: Your custom clone logic
        return super.clone();
    }

    public Vector2 getUv() {
        return uv;
    }

    public void setUv(Vector2 uv) {
        this.uv = uv;
    }
}
