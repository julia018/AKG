package model;

import logic.Camera;
import logic.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Triangle {

    private List<Vertex> vertices;
    private List<Side> sides;


    public Triangle(List<Vertex> vertexList) {

        this.vertices = vertexList;
        this.sides = new ArrayList<>();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertexByIndex(int index) {
        return this.vertices.get(index);
    }

    public void setVertexByIndex(int index, Vertex vertex) {
        this.vertices.set(index, vertex);
    }

    private Vector2 getMinMaxY() {
        List<Float> yCoordinates = new ArrayList<>();
        for(Vertex vertex: vertices) {
            yCoordinates.add(vertex.getNewPosition().getY());
        }
        Collections.sort(yCoordinates);
        return new Vector2(yCoordinates.get(0), yCoordinates.get(2));
    }

    public List<Side> gerScanLines() {
        List<Side> res = new ArrayList<>();
        Vector2 yMinMax = getMinMaxY();
        int yMin = Math.round(yMinMax.getX());
        float yMax = Math.round(yMinMax.getY());
        for(int i = yMin; i <= yMax; i++) {
            Side scanLine = getScanLine(i);
            if(scanLine != null) {
                res.add(scanLine);
            }
        }
        return res;
    }

    private Side getScanLine(int y) {
        List<Point> ends = new ArrayList<>();
        for(Side side: sides) {
            if(side.isYBelongigng((float)y)) {
                float x = side.getXCross(y);
                float z = side.getZofCross(y);
                ends.add(new Point(x, z));
            }
        }
        if(ends.size() >= 2) {
            return new Side(ends.get(0).getX(), y, ends.get(0).getZ(), ends.get(1).getX(), y,  ends.get(1).getZ());
        } else return null;
    }

    public void updateSides() {

        Vertex vert1 = vertices.get(0);
        Vertex vert2 = vertices.get(1);
        Vertex vert3 = vertices.get(2);

        Side side1 = new Side(vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ());
        Side side2 = new Side(vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ());
        Side side3 = new Side(vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ(), vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ());

        sides.clear();
        sides.add(side1);
        sides.add(side2);
        sides.add(side3);
    }
}
