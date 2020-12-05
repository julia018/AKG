package model;

public class Vertex {

    private Vector3 position;
    private Vector3 newPosition; // position after all stuff for projection
    private Vector3 normal;
    private Vector2 uv; // for texture

    public Vertex(Vector3 position, Vector3 normal, Vector2 uv) {
        this.position = position;
        this.normal = normal;
        this.uv = uv;
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
    }
}
