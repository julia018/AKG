package logic;

import model.Matrix;
import model.Vector3;

public class Transformation {

    private double[] matrix;

    public Transformation() {
        this.matrix = new double[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    }
    public Transformation(boolean i) {
        this.matrix = new double[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
    }

    public Transformation(double[] matrix) {
        this.matrix = matrix.clone();
    }

    public Transformation multiplyByMatrix(Transformation matrix2) {
        Transformation res = new Transformation();
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                double sum = 0;
                for (int k = 0; k < 4; ++k) {
                    sum += this.matrix[row * 4 + k] * matrix2.matrix[k * 4 + col];
                }
                res.matrix[row * 4 + col] = sum;
            }
        }
        return res;
    }

    //translation
    public  Transformation translate(double x, double y, double z) {
        Transformation mat = new Transformation();
        mat.matrix[3] = x;
        mat.matrix[7] = y;
        mat.matrix[11] = z;
        return this.multiplyByMatrix(mat);
    }

    //scaling
    public Transformation scale(double x) {
        Transformation mat = new Transformation();
        mat.matrix[0] = x;
        mat.matrix[5] = x;
        mat.matrix[10] = x;
        return this.multiplyByMatrix(mat);
    }

    // rotation matrix around X axis
    public Transformation rotateX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Transformation mat = new Transformation();
        mat.matrix[5] = c;
        mat.matrix[10] = c;
        mat.matrix[9] = -s;
        mat.matrix[6] = s;
        return this.multiplyByMatrix(mat);
    }

    // rotation matrix around Y axis
    public Transformation rotateY(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Transformation mat = new Transformation();
        mat.matrix[0] = c;
        mat.matrix[10] = c;
        mat.matrix[2] = s;
        mat.matrix[8] = -s;
        return this.multiplyByMatrix(mat);
    }

    // rotation matrix around Z axis
    public Transformation rotateZ(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Transformation mat = new Transformation();
        mat.matrix[0] = c;
        mat.matrix[5] = c;
        mat.matrix[4] = -s;
        mat.matrix[1] = s;
        return this.multiplyByMatrix(mat);
    }

    public Transformation transpose() {
        Transformation res = new Transformation();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                res.setMatrixElement(i * 4 + j, this.matrix[j * 4 + i]);
            }
        }
        return res;
    }

    public void setMatrixElement(int i, double value) {
        this.matrix[i] = value;
    }

    public Vector3 multiplyByVector(Vector3 vector) {
        Vector3 res = new Vector3(0, 0, 0);
        for (int row = 0; row < 4; row++) {
            float sum = 0;
            for (int col = 0; col < 4; col++) {
                sum += this.matrix[row * 4 + col] * vector.getVectorElement(col);
            }
            res.setVectorElement(row, sum);
        }
        return res;
    }

    public String toString() {
        String res = "";
        for(int i = 0; i < matrix.length; i++) {
            res += " " + matrix[i];
        }
        return res;
    }

    public double[] getMatrix() {
        return matrix;
    }

    public Transformation getInversedMAtrix() {
        Matrix matrix = new Matrix(this.matrix, 4);
        Matrix inversed = matrix.inverse();
        return new Transformation(inversed.getMatrixAsArray());
    }

    public Transformation getInversedMAtrix3() {
        Matrix matrix = new Matrix(this.matrix, 3);
        Matrix inversed = matrix.inverse();
        return new Transformation(inversed.getMatrixAsArray());
    }

    public Vector3 multiplyByVector3(Vector3 vector) {
        Vector3 res = new Vector3(0, 0, 0);
        for (int row = 0; row < 3; row++) {
            float sum = 0;
            for (int col = 0; col < 3; col++) {
                sum += this.matrix[row * 3 + col] * vector.getVectorElement(col);
            }
            res.setVectorElement(row, sum);
        }
        return res;
    }

    public Transformation transpose3() {
        Transformation res = new Transformation(true);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                res.setMatrixElement(i * 3 + j, this.matrix[j * 3 + i]);
            }
        }
        return res;
    }

    public Transformation get3by3Matrix() {
        double[] elements = new double[]{matrix[0], matrix[1], matrix[2], matrix[4], matrix[5], matrix[6], matrix[8], matrix[9], matrix[10]};
        return new Transformation(elements);
    }
}
