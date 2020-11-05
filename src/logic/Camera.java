package logic;

public class Camera {

    private Transformation position;
    private Transformation projection;
    private Transformation viewport;

    private final double XMIN = 0.0;
    private final double YMIN = 0.0;

    public Camera() {
        this.position = new Transformation();
        this.projection = new Transformation();
        this.viewport = new Transformation();
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
        this.projection.setMatrixElement(14, -1);

    }

    public void setViewport(double width, double height, double near, double far) {
        this.viewport = new Transformation();
        this.projection.setMatrixElement(0, width / 2.0);
        this.projection.setMatrixElement(3,  XMIN + width / 2.0);
        this.projection.setMatrixElement(5, -height / 2.0);
        this.projection.setMatrixElement(3,  YMIN + height / 2.0);
    }

}
