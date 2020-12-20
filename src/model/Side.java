package model;

import java.util.List;

public class Side {

    private float xStart;
    private float yStart;
    private float xEnd;
    private float yEnd;
    private float zStart;
    private float zEnd;
    private float yDelta;
    private float zDelta;
    private float xDelta;
    private float zSign;

    private Vector3 normalStart;
    private Vector3 normalEnd;
    private Vector2 uvStart;
    private Vector2 uvEnd;
    private float wStart;
    private float wEnd;


    public Vector3 getNormalStart() {
        return normalStart;
    }

    public Vector3 getNormalEnd() {
        return normalEnd;
    }

    public Side(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd, Vector3 normStart, Vector3 normEnd, float wStart, float wEnd, Vector2 uvStart, Vector2 uvEnd) {

        this.yStart = yStart;
        this.xStart = xStart;
        this.zStart = zStart;
        this.yEnd = yEnd;
        this.xEnd = xEnd;
        this.zEnd = zEnd;
        this.yDelta = yEnd - yStart;
        this.zDelta = zEnd - zStart;
        xDelta = xEnd - xStart;
        this.zSign = zDelta <= 0 ? -1 : 1;

        this.normalStart = normStart;
        this.normalEnd = normEnd;
        this.wStart = wStart;
        this.wEnd = wEnd;
        this.uvStart = uvStart;
        this.uvEnd = uvEnd;
    }

    public boolean isYBelongigng(float y) {
        if(yStart <= yEnd) {
            if ((y >= yStart) & (y <= yEnd)) {
                return true;
            }
            return false;

        } else {
            if ((y <= yStart) & (y >= yEnd)) {
                return true;
            }
            return false;
        }
    }

    public float getWCross(float y) {
        float res = (y - yStart) * (wEnd - wStart);
        res /= (yEnd - yStart);
        return res + wStart;
    }

    public Vector2 getUVCross(float y, float x, float z) {
        float deltaX = x - xStart;
        float deltaY = y - yStart;
        float currVLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY); // get length og vector
        float fullVLength = (float) Math.sqrt(xDelta * xDelta + yDelta * yDelta); // get full length of vector
        float uStart = this.uvStart.getX();
        float uEnd = this.uvEnd.getX();
        float vStart = this.uvStart.getY();
        float vEnd = this.uvEnd.getY();
        float t = currVLength / fullVLength;
        //float t = deltaX / this.xDelta;
        float upU = (1 - t)*uStart/this.zStart + t * uEnd/zEnd;
        float downU = (1 - t)*1f/this.zStart + t * 1f/zEnd;
        float resU = upU / downU;

        float upV = (1 - t)*vStart/this.zStart + t * vEnd/zEnd;
        float downV = (1 - t)*1f/this.zStart + t * 1f/zEnd;
        float resV = upV / downV;

        return new Vector2(resU, resV);
    }



    // (y - y1)/(y2 - y1) = (x - x1)/(x2 - x1)
    public float getXCross(float y) {
        float u = (y - yStart) / (yEnd - yStart);
        return xStart * (1 - u) + xEnd * u;
    }

    public float getZofCross(List<Float> alphas, List<Vertex> vertices) {
        Vertex v1 = vertices.get(0);
        Vertex v2 = vertices.get(1);
        Vertex v3 = vertices.get(2);
        float zres = v1.getNewPosition().getZ() * alphas.get(0) + v2.getNewPosition().getZ() * alphas.get(1) + v3.getNewPosition().getZ() * alphas.get(2);
        return zres;
    }

    public float getxStart() {
        return xStart;
    }

    public Vector3 getNormalOfCross(float y, float x, float z) {
        float vectorLength = (float) Math.sqrt(xDelta * xDelta + yDelta * yDelta);
        float deltaY = y - yStart;
        float deltaX = x - xStart;
        float deltaZ = z- zStart;
        //float vectorPartLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float u = (x - xStart) / (xEnd - xStart);
        float newZinv = 1f/normalStart.getZ() * (1-u) + 1f/normalEnd.getZ()*u;
        float newX = normalStart.getX() * (1-u) + normalEnd.getX()*u;
        float newY = normalStart.getY() * (1-u) + normalEnd.getY()*u;
        Vector3 resNormal =  new Vector3(newX, newY, 1f/newZinv);
        return resNormal;
    }

    public void setxStart(float xStart) {
        this.xStart = xStart;
    }

    public float getyStart() {
        return yStart;
    }

    public void setyStart(float yStart) {
        this.yStart = yStart;
    }

    public float getxEnd() {
        return xEnd;
    }

    public void setxEnd(float xEnd) {
        this.xEnd = xEnd;
    }

    public float getyEnd() {
        return yEnd;
    }

    public void setyEnd(float yEnd) {
        this.yEnd = yEnd;
    }

    public float getzStart() {
        return zStart;
    }

    public float getzEnd() {
        return zEnd;
    }

    public float getzDelta() {
        return zDelta;
    }

    public float getzSign() {
        return zSign;
    }

    public float getwStart() {
        return wStart;
    }

    public void setwStart(float wStart) {
        this.wStart = wStart;
    }

    public float getwEnd() {
        return wEnd;
    }

    public void setwEnd(float wEnd) {
        this.wEnd = wEnd;
    }

    public Vector2 getUvStart() {
        return uvStart;
    }

    public Vector2 getUvEnd() {
        return uvEnd;
    }
}
