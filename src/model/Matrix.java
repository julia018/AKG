package model;

import java.util.ArrayList;

public class Matrix {
    private double[][] data = null;
    private int rows = 0, cols = 0;

    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(double[] data, int M) {
        this.data = new double[M][M];
        rows = M;
        cols = M;
        for (int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
                this.data[i][j] = data[i * M + j];
    }


    public double[] getMatrixAsArray() {
        return this.flatten();
    }

    public double[] flatten() {
        double[] list = new double[rows * cols];

        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < cols; j++){
                list[i * cols + j] = data[i][j];
            }
        }

        return list;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public void display() {
        System.out.print("[");
        for (int row = 0; row < rows; ++row) {
            if (row != 0) {
                System.out.print(" ");
            }

            System.out.print("[");

            for (int col = 0; col < cols; ++col) {
                System.out.printf("%8.3f", data[row][col]);

                if (col != cols - 1) {
                    System.out.print(" ");
                }
            }

            System.out.print("]");

            if (row == rows - 1) {
                System.out.print("]");
            }

            System.out.println();
        }
    }

    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                result.data[col][row] = data[row][col];
            }
        }

        return result;
    }

    // Note: exclude_row and exclude_col starts from 1
    public static Matrix subMatrix(Matrix matrix, int exclude_row, int exclude_col) {
        Matrix result = new Matrix(matrix.rows - 1, matrix.cols - 1);

        for (int row = 0, p = 0; row < matrix.rows; ++row) {
            if (row != exclude_row - 1) {
                for (int col = 0, q = 0; col < matrix.cols; ++col) {
                    if (col != exclude_col - 1) {
                        result.data[p][q] = matrix.data[row][col];

                        ++q;
                    }
                }

                ++p;
            }
        }

        return result;
    }

    public double determinant() {
        if (rows != cols) {
            return Double.NaN;
        }
        else {
            return _determinant(this);
        }
    }

    private double _determinant(Matrix matrix) {
        if (matrix.cols == 1) {
            return matrix.data[0][0];
        }
        else if (matrix.cols == 2) {
            return (matrix.data[0][0] * matrix.data[1][1] -
                    matrix.data[0][1] * matrix.data[1][0]);
        }
        else {
            double result = 0.0;

            for (int col = 0; col < matrix.cols; ++col) {
                Matrix sub = subMatrix(matrix, 1, col + 1);

                result += (Math.pow(-1, 1 + col + 1) *
                        matrix.data[0][col] * _determinant(sub));
            }

            return result;
        }
    }

    public Matrix inverse() {
        double det = determinant();

        if (rows != cols || det == 0.0) {
            return null;
        }
        else {
            Matrix result = new Matrix(rows, cols);

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    Matrix sub = subMatrix(this, row + 1, col + 1);

                    result.data[col][row] = (1.0 / det *
                            Math.pow(-1, row + col) *
                            _determinant(sub));
                }
            }

            return result;
        }
    }
}
