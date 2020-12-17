package model;

import logic.Camera;
import logic.Transformation;

import java.util.*;
import java.util.stream.Stream;

public class Triangle {

    private List<Vertex> vertices;
    private List<Side> sides; // clock-wise dir
    private Vector3 normal;
    private Vertex min;
    private Vertex max;

    public Vector3 getNormal() {
        return normal;
    }

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

    private List<Integer> getMinMaxY() {
        HashMap<Integer, Float> map = new HashMap<>();
        LinkedHashMap<Integer, Float> soretedmap = new LinkedHashMap<>();
        for(int i = 0; i < vertices.size(); i++) {
            map.put(i, vertices.get(i).getNewPosition().getY());
        }
        //Collections.sort(yCoordinates);
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> soretedmap.put(x.getKey(), x.getValue()));
        return new ArrayList<Integer>(soretedmap.keySet());
    }

    public List<Side> getScanLines() {
        List<Side> res = new ArrayList<>();
        List<Integer> vertexIndexes = getMinMaxY();
        min = vertices.get(vertexIndexes.get(0));
        max = vertices.get(vertexIndexes.get(2));
        int yMin = Math.round(min.getNewPosition().getY());
        float yMax = Math.round(max.getNewPosition().getY());
        for(int i = yMin; i <= yMax; i++) {
            Side scanLine = getScanLine(i);
            if(scanLine != null) {
                res.add(scanLine);
            }
        }
        return res;
    }

    private List<Float> getBaricCoords(float x, float y, float z) {
        List<Float> res = new ArrayList<>();
        return new ArrayList<>();
    }

    private Side getScanLine(int y) {
        List<Point> ends = new ArrayList<>();
        for(Side side: sides) {
            if(side.isYBelongigng((float)y)) {
                float x = side.getXCross(y);
                float z = side.getZofCross(y);
                float w = side.getWCross(y);
                ends.add(new Point(x, z, side.getNormalOfCross(y, x, z), w));
            }
        }
        if(ends.size() >= 2) {
            return new Side(ends.get(0).getX(), y, ends.get(0).getZ(), ends.get(1).getX(), y,  ends.get(1).getZ(), ends.get(0).getNormal(), ends.get(1).getNormal(), ends.get(0).getW(), ends.get(1).getW());
        } else return null;
    }

    public void updateSides() {

        Vertex vert1 = vertices.get(0);
        Vertex vert2 = vertices.get(1);
        Vertex vert3 = vertices.get(2);

        Side side1 = new Side(vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ(), vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert2.getNewNormal(), vert1.getNewNormal(), vert2.getW(), vert1.getW());
        Side side2 = new Side(vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ(), vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ(), vert2.getNewNormal(), vert3.getNewNormal(), vert2.getW(), vert3.getW());
        Side side3 = new Side(vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ(), vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert3.getNewNormal(), vert1.getNewNormal(), vert3.getW(), vert1.getW());

        sides.clear();
        sides.add(side1);
        sides.add(side2);
        sides.add(side3);
        updateNormal();
    }

    private void updateNormal() {
        Side side1 = sides.get(0);
        Side side2 = sides.get(1);
        Vector3 firstSideVector = new Vector3(side1.getxEnd() - side1.getxStart(), side1.getyEnd() - side1.getyStart(), side1.getzEnd() - side1.getzStart());
        Vector3 secondSideVector = new Vector3(side2.getxEnd() - side2.getxStart(), side2.getyEnd() - side2.getyStart(), side2.getzEnd() - side2.getzStart());

        Vector3 normal = firstSideVector.getCrossProduct(secondSideVector).getNormalized();
        this.normal = normal;
    }

    public void sortNewVertices() {
        List<Vertex> sortedList = new ArrayList<>();
        Collections.sort(this.vertices);
        sortedList.add(vertices.get(0));
        vertices.remove(0);

        Collections.sort(vertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex h1, Vertex h2) {
                if(h1.getNewPosition().getY() < h2.getNewPosition().getY()) {
                    return -1;
                } else if(h1.getNewPosition().getY() == h2.getNewPosition().getY()) {
                    if(h1.getNewPosition().getX() < h2.getNewPosition().getX()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });

        sortedList.addAll(vertices);
        vertices = sortedList;

    }

    public boolean isVisible(Vector3 view) {
        float csalarProduct = normal.getScalarProduct(view);
        if(csalarProduct > 0) {
            return false;
        }
        return true;
    }
}
