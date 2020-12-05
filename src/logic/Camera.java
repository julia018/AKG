package logic;

import model.Vector3;

public class Camera {

    private Transformation transformation;
    private Transformation projection;
    private Transformation viewport;
    private Transformation observer;

    private Vector3 eye;
    private Vector3 target;
    private Vector3 up;

    private float modelX;
    private float modelY;
    private float modelZ;


    private final double XMIN = 0.0;
    private final double YMIN = 0.0;

    public Camera(Vector3 eye, Vector3 target, Vector3 up) {
        this.eye = eye;
        this.target = target;
        this.up = up;
        this.modelX = target.getX();
        this.modelY = target.getY();
        this.modelZ = target.getZ();
    }


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
        this.projection.setMatrixElement(14, -1.0);
        this.projection.setMatrixElement(15, 0.0);

    }

    public void setPerspProjectionFOV(double fov, double aspect, double near, double far) {
        this.projection = new Transformation();
        System.out.println(aspect);
        this.projection.setMatrixElement(0, 1.0 / (aspect * Math.tan(Math.toRadians(fov) / 2)));
        this.projection.setMatrixElement(5, 1.0 / Math.tan(Math.toRadians(fov) / 2));
        this.projection.setMatrixElement(10, far / (near - far));
        this.projection.setMatrixElement(11, near * far / (near - far));
        this.projection.setMatrixElement(14, -1.0);
        this.projection.setMatrixElement(15, 0.0);

    }

    public void setViewport(double width, double height) {
        this.viewport = new Transformation();
        this.viewport.setMatrixElement(0, width / 2.0);
        this.viewport.setMatrixElement(3,  XMIN + width / 2.0);
        this.viewport.setMatrixElement(5, -height / 2.0);
        this.viewport.setMatrixElement(7,  YMIN + height / 2.0);
    }


    public Transformation getTransformation() {
        return new Transformation().translate(this.modelX, this.modelY, this.modelZ);
    }

    public Transformation getProjection() {

        return  this.projection;
    }

    public Transformation getViewport() {
        return viewport;
    }

    public Transformation getObserver() {
        Transformation observer = new Transformation();
        System.out.println("in observer ");
        System.out.println("eye");
        System.out.println(eye);
        System.out.println("in observer ");
        System.out.println("target");
        System.out.println(target);
        Vector3 zAxis = eye.substractVector(target).getNormalized();
        Vector3 xAxis = up.getCrossProduct(zAxis).getNormalized();
        Vector3 yAxis = up;
        observer.setMatrixElement(0, xAxis.getX());
        observer.setMatrixElement(1,  xAxis.getY());
        observer.setMatrixElement(2, xAxis.getZ());
        observer.setMatrixElement(3,  -1 * xAxis.getScalarProduct(eye));
        observer.setMatrixElement(4, yAxis.getX());
        observer.setMatrixElement(5,  yAxis.getY());
        observer.setMatrixElement(6, yAxis.getZ());
        observer.setMatrixElement(7,  -1 * yAxis.getScalarProduct(eye));
        observer.setMatrixElement(8, zAxis.getX());
        observer.setMatrixElement(9,  zAxis.getY());
        observer.setMatrixElement(10, zAxis.getZ());
        observer.setMatrixElement(11,  -1 * zAxis.getScalarProduct(eye));
        System.out.println("res observer ");
        System.out.println(observer);
        return observer;
    }

    public void addEyeX(double value) {
        this.eye.addValueToVectorElement(0, value);
    }

    public void addEyeY(double value) {
        this.eye.addValueToVectorElement(1, value);
    }

    public void addEyeZ(double value) {
        this.eye.addValueToVectorElement(2, value);
    }

    public Vector3 getTarget() {
        return target;
    }

    public double getTargetZ() {
        return this.eye.getZ() - this.target.getZ();
    }

    public void addModelX(float value) {
        this.modelX += value;
        this.target.setVectorElement(0, this.modelX);
    }

    public void addModelY(double value) {
        this.modelY += value;
        this.target.setVectorElement(1, this.modelY);
    }

    public void addModelZ(double value) {
        this.modelZ += value;
        this.target.setVectorElement(2, this.modelZ);
    }
}
