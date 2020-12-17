package model;

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
    private float wStart;
    private float wEnd;


    public Vector3 getNormalStart() {
        return normalStart;
    }

    public Vector3 getNormalEnd() {
        return normalEnd;
    }

    public Side(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd, Vector3 normStart, Vector3 normEnd, float wStart, float wEnd) {

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

    // (y - y1)/(y2 - y1) = (x - x1)/(x2 - x1)
    public float getXCross(float y) {
        float res = (y - yStart) * (xEnd - xStart);
        res /= (yEnd - yStart);
        return res + xStart;
    }

    public float getZofCross(float y) {
        float part = Math.abs((y - yStart) / yDelta);
        float inv = (float) (1.0 / zStart * (1 - part) + 1.0 / zEnd * part);
        //return zStart + part * Math.abs(zDelta) * zSign;
        return 1f / inv;
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
        float u = (z - zStart) / (zEnd - zStart);
        float newZinv = 1f/normalStart.getZ() * (1-u) + 1f/normalEnd.getZ()*u;
        //Vector3 resNormal = normalStart.multByValue(u).addVector(normalEnd.multByValue(1 - u)).getNormalized();
        Vector3 resNormal = normalStart.multByValue(1-u).addVector(normalEnd.multByValue(u));
        resNormal.setVectorElement(2, 1f/newZinv);
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
}
