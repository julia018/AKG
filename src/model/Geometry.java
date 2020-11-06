package model;

import java.util.List;

public class Geometry {

    private List<Triangle> triangleList;

    public Geometry(List<Triangle> triangleList) {

        this.triangleList = triangleList;
    }


    public List<Triangle> getTriangleList() {
        return triangleList;
    }
}
