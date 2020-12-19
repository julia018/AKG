package logic;

import model.Vector3;

import java.awt.*;

public class Phong {


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


    public Phong(Vector3 light) {

        this.lightPoint = light;

    }

    private float[] getBackgroundLight(Color color) {
        float[] res = new float[4];
        res[0] = (float) (color.getRed() / 255.0); //r
        res[1] = (float) (color.getGreen() / 255.0); //g
        res[2] = (float) (color.getBlue() / 255.0); //b
        res[3] = 1.0f; //a
        return res;
    }

    private float[] getDiffuseLght(Vector3 vector, Vector3 normal, Color color) {
        float cos = getCos(vector, normal, lightPoint);
        float[] res = new float[4];
        res[0] = (float) (color.getRed() / 255.0) * cos; //r
        res[1] = (float) (color.getGreen() / 255.0) * cos; //g
        res[2] = (float) (color.getBlue() / 255.0) * cos;
        res[3] = 1.0f;
        return res;
    }

    private float getCos(Vector3 vector, Vector3 normal, Vector3 lightPoint) {
        //Vector3 lightDir = new Vector3(-1, 1, 1).getNormalized();
        Vector3 lightDir = (lightPoint).getNormalized();
        //Vector3 lightDir = new Vector3(eye.getX()*-1, eye.getY()*-1, eye.getZ() * -1).getNormalized();
        float scalarProduct = lightDir.getScalarProduct(normal);
        return Math.max(0, scalarProduct);
    }

    private float[] getMirrorLight(Vector3 eye, Vector3 light, Vector3 normal, Color color) {
        float[] res = new float[4];
        Vector3 reflectedLight = getReflectedLight(light, normal).getNormalized();
        float result = (float) Math.pow(Math.max(0, reflectedLight.getScalarProduct(eye)), shine);
        res[0] = (float) (color.getRed() / 255.0) * result; //r
        res[1] = (float) (color.getGreen() / 255.0) * result; //g
        res[2] = (float) (color.getBlue() / 255.0) * result;
        res[3] = 1.0f;
        return res;
    }

    private Vector3 getReflectedLight(Vector3 light, Vector3 normal) {
        //Vector3 invertedLight = light.substractVector(vector).getNormalized();
        float scalarProduct = Math.max(0, light.getScalarProduct(normal));
        return (normal.multByValue(2 * scalarProduct)).substractVector(light).getNormalized();
    }

    public Color getResultPhongColor(Vector3 vector, Vector3 normal, Vector3 eyePoint, Color colorSpec, Color colorAlbedo) {
        Vector3 V = (new Vector3(0, 0, 0)).substractVector(vector).getNormalized();
        Color res = null;
        float[] backColor = getBackgroundLight(colorAlbedo);
        float[] diffColor = getDiffuseLght(vector, normal, colorAlbedo);
        float[] mirrorColor = getMirrorLight(V, lightPoint, normal, colorSpec);
        float resRed = backColor[0] + diffColor[0] + mirrorColor[0];
        float resGreen = backColor[1] + diffColor[1] + mirrorColor[1];
        float resBlue = backColor[2] + diffColor[2] + mirrorColor[2];
        try {
            res = new Color(Math.min(1, resRed), Math.min(1, resGreen), Math.min(1, resBlue), 1f);

        } catch (Exception ex) {
            System.out.println(backColor[0]);
            System.out.println(diffColor[0]);
            System.out.println(mirrorColor[0]);
            System.out.println(backColor[1]);
            System.out.println(diffColor[1]);
            System.out.println(mirrorColor[1]);
            System.out.println(backColor[2]);
            System.out.println(diffColor[2]);
            System.out.println(mirrorColor[2]);
        }
        return res;
    }
}
