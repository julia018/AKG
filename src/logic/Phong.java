package logic;

import model.Vector3;

import java.awt.*;

public class Phong {

    private final float BACKCOEFF = 0.1f;
    private final float DIFFUSECOEFF = 0.1f;
    private final float MIRRORCOEFF = 0.1f;
    private final float SURFSHINECOEFF = 1.0f;

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

    private Color getBackgroundLight() {
        return new Color(rBack * BACKCOEFF, gBack * BACKCOEFF, bBack * BACKCOEFF);
    }

    private Color getDiffuseLght(Vector3 vector, Vector3 normal) {
        float cos = getCos(vector, normal, lightPoint);
        return new Color(rDiff * DIFFUSECOEFF * cos, gDiff * DIFFUSECOEFF * cos, bDiff * DIFFUSECOEFF * cos);
    }

    private float getCos(Vector3 vector, Vector3 normal, Vector3 lightVector) {
        Vector3 lightDir = vector.substractVector(lightVector).getNormalized();
        float scalarProduct = normal.getScalarProduct(lightDir);
        return Math.max(0, scalarProduct);
    }

    private Color getMirrorLight(Vector3 eye, Vector3 vector, Vector3 normal) {
        Vector3 reflectedLight = getReflectedLight(new Vector3(-50, 50, 10), vector, normal).getNormalized();
        Vector3 eyeVector = vector.substractVector(eye).getNormalized();
        float result = MIRRORCOEFF * (float) Math.pow(reflectedLight.getScalarProduct(eyeVector), SURFSHINECOEFF);
        return new Color(result * rMirror, result * gMirror, result * bMirror);
    }

    private Vector3 getReflectedLight(Vector3 light, Vector3 vector, Vector3 normal) {
        Vector3 invertedLight = vector.substractVector(light).getNormalized();
        float scalarProduct = invertedLight.getScalarProduct(normal);
        return invertedLight.substractVector(normal.multByValue(2 * scalarProduct));
    }

    public Color getResultPhongColor(Vector3 vector, Vector3 normal, Vector3 eyePoint) {

        Color backColor = getBackgroundLight();
        Color diffColor = getDiffuseLght(vector, normal);
        Color mirrorColor = getMirrorLight(eyePoint, vector, normal);
        int newRed = backColor.getRed() + diffColor.getRed() + mirrorColor.getRed();
        int newGreen = backColor.getGreen() + diffColor.getGreen() + mirrorColor.getGreen();
        int newBlue = backColor.getBlue() + diffColor.getBlue() + mirrorColor.getBlue();
        return new Color(newRed, newGreen, newBlue);
    }
}
