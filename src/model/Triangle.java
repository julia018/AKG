package model;

import logic.Camera;
import logic.Transformation;

import java.util.List;

public class Triangle {

    private List<Vertex> vertices;

    public Triangle(List<Vertex> vertexList) {

        this.vertices = vertexList;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertexByIndex(int index) {
        return this.vertices.get(index);
    }
}
