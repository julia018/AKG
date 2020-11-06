package model;

public class Vertex {

    private Vector3 position;
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
}
