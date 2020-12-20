package model;

import logic.Camera;
import logic.Texture;
import logic.Transformation;

import java.awt.*;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Triangle {

    private List<Vertex> vertices;
    private List<Side> sides; // clock-wise dir
    private Vector3 normal;
    private Vertex min;
    private Vertex max;
    private List<Vertex> sortedVertices = new ArrayList<>();


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
        sortedVertices.clear();
        List<Integer> vertexIndexes = getMinMaxY();
        this.sortedVertices.add(vertices.get(vertexIndexes.get(0)));
        this.sortedVertices.add(vertices.get(vertexIndexes.get(1)));
        this.sortedVertices.add(vertices.get(vertexIndexes.get(2)));
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

    private Side getScanLine(int y) {
        List<Point> ends = new ArrayList<>();
        for(Side side: sides) {
            if(side.isYBelongigng((float)y)) {
                float x = side.getXCross(y);
                List<Float> alphas = this.getAlphas(x, y);
                float z = side.getZofCross(alphas, vertices);
                float w = side.getWCross(y);
                Vector2 uv = side.getUVCross(y, x, z);
                ends.add(new Point(x, z, side.getNormalOfCross(y, x, z), w, uv));
            }
        }
        if(ends.size() >= 2) {
            return new Side(ends.get(0).getX(), y, ends.get(0).getZ(), ends.get(1).getX(), y,  ends.get(1).getZ(), ends.get(0).getNormal(), ends.get(1).getNormal(), ends.get(0).getW(), ends.get(1).getW(), ends.get(0).getUv(), ends.get(1).getUv());
        } else return null;
    }

    public Vector3 getInterpolatedNormal(float x, float y) {
        List<Float> alphas = this.getAlphas(x, y);
        float alpha0 = alphas.get(0);
        float alpha1 = alphas.get(1);
        float alpha2 = alphas.get(2);

        Vector3 normal0 = vertices.get(0).getNewNormal();
        Vector3 normal1 = vertices.get(1).getNewNormal();
        Vector3 normal2 = vertices.get(2).getNewNormal();

        float resx = normal0.getX() * alpha0 + normal1.getX() * alpha1 + normal2.getX() * alpha2;
        float resy = normal0.getY() * alpha0 + normal1.getY() * alpha1 + normal2.getY() * alpha2;
        float reszinv = 1f/normal0.getZ() * alpha0 + 1f/normal1.getZ() * alpha1 + 1f/normal2.getZ() * alpha2;
        return new Vector3(resx, resy, 1f/reszinv);
    }

    public float getInterpolatedZ(float x, float y) {
        List<Float> alphas = this.getAlphas(x, y);
        float alpha0 = alphas.get(0);
        float alpha1 = alphas.get(1);
        float alpha2 = alphas.get(2);

        float z0 = vertices.get(0).getNewPosition().getZ();
        float z1 = vertices.get(1).getNewPosition().getZ();
        float z2 = vertices.get(2).getNewPosition().getZ();

        float reszinv = 1f/z0 * alpha0 + 1f/z1 * alpha1 + 1f/z2 * alpha2;
        return 1f/reszinv;

    }

    public Vector3 getInterpolatedObserverVector(float x, float y) {
        List<Float> alphas = this.getAlphas(x, y);
        float alpha0 = alphas.get(0);
        float alpha1 = alphas.get(1);
        float alpha2 = alphas.get(2);

        Vector3 obsv0 = vertices.get(0).getNewObserverPosition();
        Vector3 obsv1 = vertices.get(1).getNewObserverPosition();
        Vector3 obsv2 = vertices.get(2).getNewObserverPosition();

        float resx = obsv0.getX() * alpha0 + obsv1.getX() * alpha1 + obsv2.getX() * alpha2;
        float resy = obsv0.getY() * alpha0 + obsv1.getY() * alpha1 + obsv2.getY() * alpha2;
        float reszinv = 1f/obsv0.getZ() * alpha0 + 1f/obsv1.getZ() * alpha1 + 1f/obsv2.getZ() * alpha2;
        return new Vector3(resx, resy, 1f/reszinv);
    }

    /*public Vector3 getInterpolatedUV(float x, float y) {
        List<Float> alphas = this.getAlphas(x, y);
        float alpha0 = alphas.get(0);
        float alpha1 = alphas.get(1);
        float alpha2 = alphas.get(2);

        Vector2 uv1 = vertices.get(0).getUv();
        Vector2 uv2 = vertices.get(1).getUv();
        Vector2 uv3 = vertices.get(2).getUv();

        float resx = obsv0.getX() * alpha0 + obsv1.getX() * alpha1 + obsv2.getX() * alpha2;
        float resy = obsv0.getY() * alpha0 + obsv1.getY() * alpha1 + obsv2.getY() * alpha2;
        float reszinv = 1f/obsv0.getZ() * alpha0 + 1f/obsv1.getZ() * alpha1 + 1f/obsv2.getZ() * alpha2;
        return new Vector3(resx, resy, 1f/reszinv);
    }*/

    public void updateSides() {
        Vertex vert1 = vertices.get(0);
        Vertex vert2 = vertices.get(1);
        Vertex vert3 = vertices.get(2);

        Side side1 = new Side(vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ(), vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert2.getNewNormal(), vert1.getNewNormal(), vert2.getW(), vert1.getW(), vert2.getUv(), vert1.getUv());
        Side side2 = new Side(vert2.getNewPosition().getX(), vert2.getNewPosition().getY(), vert2.getNewPosition().getZ(), vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ(), vert2.getNewNormal(), vert3.getNewNormal(), vert2.getW(), vert3.getW(), vert2.getUv(), vert3.getUv());
        Side side3 = new Side(vert3.getNewPosition().getX(), vert3.getNewPosition().getY(), vert3.getNewPosition().getZ(), vert1.getNewPosition().getX(), vert1.getNewPosition().getY(), vert1.getNewPosition().getZ(), vert3.getNewNormal(), vert1.getNewNormal(), vert3.getW(), vert1.getW(), vert3.getUv(), vert1.getUv());

        sides.clear();
        sides.add(side1);
        sides.add(side2);
        sides.add(side3);
        //updateNormal();
    }

    public void updateNormal() {
        Side side1 = sides.get(0);
        Side side2 = sides.get(1);
        Vector3 firstSideVector = new Vector3(side1.getxEnd() - side1.getxStart(), side1.getyEnd() - side1.getyStart(), side1.getzEnd() - side1.getzStart());
        Vector3 secondSideVector = new Vector3(side2.getxEnd() - side2.getxStart(), side2.getyEnd() - side2.getyStart(), side2.getzEnd() - side2.getzStart());

        Vector3 normal = firstSideVector.getCrossProduct(secondSideVector).getNormalized();
        this.normal = normal;
    }

    public void sortNewVertices() throws CloneNotSupportedException {
        List<Vertex> sortedList = new ArrayList<>();
        List<Vertex> startList = new ArrayList<>();
        for(Vertex v : vertices) {
            startList.add((Vertex) v.clone());
        }
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
        sortedVertices = sortedList;
        vertices = startList;

    }

    public boolean isVisible(Vector3 view) {
        float csalarProduct = normal.getScalarProduct(view);
        if(csalarProduct > 0) {
            return false;
        }
        return true;
    }

    private float getDoubleArea() {
        Vertex v0 = vertices.get(0);
        Vertex v1 = vertices.get(1);
        Vertex v2 = vertices.get(2);
        Vector3 firstSideVector = new Vector3(v1.getNewPosition().getX() - v0.getNewPosition().getX(), v1.getNewPosition().getY() - v0.getNewPosition().getY(), v1.getNewPosition().getZ() - v0.getNewPosition().getZ());
        Vector3 secondSideVector = new Vector3(v2.getNewPosition().getX() - v0.getNewPosition().getX(), v2.getNewPosition().getY() - v0.getNewPosition().getY(), v2.getNewPosition().getZ() - v0.getNewPosition().getZ());

        float res = (firstSideVector.getX() * secondSideVector.getY() - firstSideVector.getY() * secondSideVector.getX());
        return res;
    }

    public List<Float> getAlphas(float x, float y) {

        float trianglearea = getDoubleArea();
        List<Float> res = new ArrayList<>();
        Vertex v0 = vertices.get(0);
        Vertex v1 = vertices.get(1);
        Vertex v2 = vertices.get(2);

        // alpha0
        float firstsidex = v1.getNewPosition().getX() - x;
        float secondsidex = v2.getNewPosition().getX() - x;
        float firstsidey = v1.getNewPosition().getY() - y;
        float secondsidey = v2.getNewPosition().getY() - y;
        float areaone = (firstsidex * secondsidey - firstsidey * secondsidex);
        float alpha0 = areaone/trianglearea;
        res.add(alpha0);

        // alpha1
        float firsttsidex = v2.getNewPosition().getX() - x;
        float seconddsidex = v0.getNewPosition().getX() - x;
        float firsttsidey = v2.getNewPosition().getY() - y;
        float seconddsidey = v0.getNewPosition().getY() - y;
        float areatwo = (firsttsidex * seconddsidey - firsttsidey * seconddsidex);
        float alpha1 = areatwo/trianglearea;
        res.add(alpha1);

        // alpha2
        float firstttsidex = v0.getNewPosition().getX() - x;
        float secondddsidex = v1.getNewPosition().getX() - x;
        float firstttsidey = v0.getNewPosition().getY() - y;
        float secondddsidey = v1.getNewPosition().getY() - y;
        float areathree = (firstttsidex * secondddsidey - firstttsidey * secondddsidex);
        float alpha2 = areathree/trianglearea;
        res.add(alpha2);

        return res;
    }

    public Color getTextureColor(Texture texture) {
        Color clr1 = texture.getSpecularColor(vertices.get(0).getUv());
        Color clr2 = texture.getSpecularColor(vertices.get(1).getUv());
        Color clr3 = texture.getSpecularColor(vertices.get(2).getUv());

        int resR = Math.round((clr1.getRed() + clr2.getRed() + clr3.getRed()) / 3f);
        int resG = Math.round((clr1.getGreen() + clr2.getGreen() + clr3.getGreen()) / 3f);
        int resB = Math.round((clr1.getBlue() + clr2.getBlue() + clr3.getBlue()) / 3f);

        return new Color(resR, resG, resB);

    }
}
