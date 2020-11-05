package logic;

import java.lang.reflect.Array;

public class Transformation {

    private double[] matrix;

    public Transformation() {
        this.matrix = new double[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    }



    // multiply this matrix to parameter
    public Transformation multiply(Transformation matrix2) {
        Transformation res = new Transformation();
        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {
                int sum = 0;
                for (int k = 0; k < 4; ++k) {
                    sum += this.matrix[k * 4 + row] * matrix2.matrix[col * 4 + k];
                }
                res.matrix[col * 4 + row] = sum;
            }
        }
        return res;
    }

    //translation
    public  Transformation translate(double x, double y, double z) {
        Transformation mat = new Transformation();
        mat.matrix[12] = x;
        mat.matrix[13] = y;
        mat.matrix[14] = z;
        return this.multiply(mat);
    }

    //scaling
    public Transformation scale(double x, double y, double z) {
        Transformation mat = new Transformation();
        mat.matrix[0] = x;
        mat.matrix[5] = y;
        mat.matrix[10] = z;
        return this.multiply(mat);
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
        return this.multiply(mat);
    }

    // rotation matrix around Y axis
    public Transformation rotateY(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Transformation mat = new Transformation();
        mat.matrix[0] = c;
        mat.matrix[10] = c;
        mat.matrix[2] = -s;
        mat.matrix[8] = s;
        return this.multiply(mat);
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
        return this.multiply(mat);
    }

    public void setMatrixElement(int i, double value) {
        this.matrix[i] = value;
    }
}
