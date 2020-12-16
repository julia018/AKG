package logic;

import model.Vector3;

import java.awt.*;

public class Phong {


    float[] mat_ambient ={ 0.2125f, 0.1275f, 0.054f, 1.0f };
    float[] mat_diffuse ={ 0.714f, 0.4284f, 0.18144f, 1.0f };
    float[] mat_specular ={ 0.393548f, 0.271906f, 0.166721f, 1.0f };
    float shine = 25.6f;

    private int rBack;
    private int gBack;
    private int bBack;

    private int rDiff;
    private int gDiff;
    private int bDiff;

    private int rMirror;
    private int gMirror;
    private int bMirror;

    private Vector3 lightPoint;


    public Phong(int rBack, int gBack, int bBack, int rDiff, int gDiff, int bDiff, int rMirror, int gMirror, int bMirror, Vector3 light) {
        this.rBack = rBack;
        this.gBack = gBack;
        this.bBack = bBack;
        this.rDiff = rDiff;
        this.gDiff = gDiff;
        this.bDiff = bDiff;
        this.rMirror = rMirror;
        this.gMirror = gMirror;
        this.bMirror = bMirror;
        this.lightPoint = light;

    }

    private float[] getBackgroundLight() {
        float[] res = new float[4];
        res[0] = mat_ambient[0]; //r
        res[1] = mat_ambient[1]; //g
        res[2] = mat_ambient[2]; //b
        res[3] = mat_ambient[3]; //a
        return res;
    }

    private float[] getDiffuseLght(Vector3 vector, Vector3 normal) {
        float cos = getCos(vector, normal, lightPoint);
        float[] res = new float[4];
        res[0] = mat_diffuse[0] * cos;
        res[1] = mat_diffuse[1] * cos;
        res[2] = mat_diffuse[2] * cos;
        res[3] = mat_diffuse[3];
        return res;
    }

    private float getCos(Vector3 vector, Vector3 normal, Vector3 lightVector) {
        Vector3 lightDir = lightVector.substractVector(vector).getNormalized();
        float scalarProduct = normal.getScalarProduct(lightDir);
        return Math.max(0, scalarProduct);
    }

    private float[] getMirrorLight(Vector3 eye, Vector3 vector, Vector3 normal) {
        float[] res = new float[4];
        Vector3 reflectedLight = getReflectedLight(this.lightPoint, vector, normal).getNormalized();
        Vector3 eyeVector = eye.substractVector(vector).getNormalized();
        float result = (float) Math.pow(reflectedLight.getScalarProduct(eyeVector), shine);
        res[0] = mat_specular[0] * result;
        res[1] = mat_specular[1] * result;
        res[2] = mat_specular[2] * result;
        res[3] = 1;
        return res;
    }

    private Vector3 getReflectedLight(Vector3 light, Vector3 vector, Vector3 normal) {
        Vector3 invertedLight = light.substractVector(vector).getNormalized();
        float scalarProduct = invertedLight.getScalarProduct(normal);
        return invertedLight.substractVector(normal.multByValue(2 * scalarProduct));
    }

    public Color getResultPhongColor(Vector3 vector, Vector3 normal, Vector3 eyePoint) {

        float[] backColor = getBackgroundLight();
        float[] diffColor = getDiffuseLght(vector, normal);
        float[] mirrorColor = getMirrorLight(eyePoint, vector, normal);
        //int newRed = backColor.getRed();
        //int newGreen = backColor.getGreen();
        //int newBlue = backColor.getBlue();
        return new Color(backColor[0] + diffColor[0], backColor[1] + diffColor[1], backColor[2] + diffColor[2], 1f);
    }
}
