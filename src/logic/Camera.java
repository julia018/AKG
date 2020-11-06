package logic;

import model.Vector3;

public class Camera {

    private Transformation position;
    private Transformation projection;
    private Transformation viewport;
    private Transformation observer;

    private final double XMIN = 0.0;
    private final double YMIN = 0.0;

    public void setOrthoProjection(double width, double height, double near, double far) {
        this.projection = new Transformation();
        this.projection.setMatrixElement(0, 2.0 / width);
        this.projection.setMatrixElement(5, 2.0 / height);
        this.projection.setMatrixElement(10, far / (near - far));
        this.projection.setMatrixElement(11, near / (near - far));
    }

    public void setPerspProjection(double width, double height, double near, double far) {
        this.projection = new Transformation();
        this.projection.setMatrixElement(0, 2.0 * near / width);
        this.projection.setMatrixElement(5, 2.0 * near / height);
        this.projection.setMatrixElement(10, far / (near - far));
        this.projection.setMatrixElement(11, near * far / (near - far));
        this.projection.setMatrixElement(14, -1);

    }

    public void setViewport(double width, double height, double near, double far) {
        this.viewport = new Transformation();
        this.projection.setMatrixElement(0, width / 2.0);
        this.projection.setMatrixElement(3,  XMIN + width / 2.0);
        this.projection.setMatrixElement(5, -height / 2.0);
        this.projection.setMatrixElement(3,  YMIN + height / 2.0);
    }

    public void setObserver(Vector3 eye, Vector3 target, Vector3 up) {
        this.observer = new Transformation();

        Vector3 zAxis = eye.substractVector(target).getNormalized();
        Vector3 xAxis = up.getCrossProduct(zAxis).getNormalized();
        Vector3 yAxis = up;
        this.projection.setMatrixElement(0, xAxis.getX());
        this.projection.setMatrixElement(1,  xAxis.getY());
        this.projection.setMatrixElement(2, xAxis.getZ());
        this.projection.setMatrixElement(3,  -1 * xAxis.getScalarProduct(eye));
        this.projection.setMatrixElement(4, yAxis.getX());
        this.projection.setMatrixElement(5,  yAxis.getY());
        this.projection.setMatrixElement(6, yAxis.getZ());
        this.projection.setMatrixElement(7,  -1 * yAxis.getScalarProduct(eye));
        this.projection.setMatrixElement(8, zAxis.getX());
        this.projection.setMatrixElement(9,  zAxis.getY());
        this.projection.setMatrixElement(10, zAxis.getZ());
        this.projection.setMatrixElement(11,  -1 * zAxis.getScalarProduct(eye));
    }

}
