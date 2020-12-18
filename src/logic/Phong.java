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


    public Phong(Vector3 light) {

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

    private float[] getDiffuseLght(Vector3 vector, Vector3 normal, Vector3 eye, Transformation proj, Transformation vp) {
        float cos = getCos(vector, normal, lightPoint, proj, vp);
        float[] res = new float[4];
        res[0] = mat_diffuse[0] * cos;
        res[1] = mat_diffuse[1] * cos;
        res[2] = mat_diffuse[2] * cos;
        res[3] = mat_diffuse[3];
        return res;
    }

    private float getCos(Vector3 vector, Vector3 normal, Vector3 lightPoint, Transformation proj, Transformation vp) {
        //Vector3 lightDir = new Vector3(-1, 1, 1).getNormalized();
        Vector3 lightDir = (lightPoint).getNormalized();
        //Vector3 lightDir = new Vector3(eye.getX()*-1, eye.getY()*-1, eye.getZ() * -1).getNormalized();
        Vector3 normInObserver = normal;
        float scalarProduct = lightDir.getScalarProduct(normInObserver);
        return Math.max(0, scalarProduct);
    }

    private float[] getMirrorLight(Vector3 eye, Vector3 light, Vector3 normal) {
        float[] res = new float[4];
        Vector3 reflectedLight = getReflectedLight(light, normal).getNormalized();
        float result = (float) Math.pow(Math.max(0, reflectedLight.getScalarProduct(eye)), shine);
        res[0] = mat_specular[0] * result;
        res[1] = mat_specular[1] * result;
        res[2] = mat_specular[2] * result;
        res[3] = 1;
        return res;
    }

    private Vector3 getReflectedLight(Vector3 light, Vector3 normal) {
        //Vector3 invertedLight = light.substractVector(vector).getNormalized();
        float scalarProduct = Math.max(0, light.getScalarProduct(normal));
        return (normal.multByValue(2 * scalarProduct)).substractVector(light).getNormalized();
    }

    public Color getResultPhongColor(Vector3 vector, Vector3 normal, Vector3 eyePoint, Transformation proj, Transformation vp) {
        Vector3 V = (new Vector3(0, 0, 0)).substractVector(vector).getNormalized();
        Color res = null;
        float[] backColor = getBackgroundLight();
        float[] diffColor = getDiffuseLght(vector, normal, eyePoint, proj, vp);
        float[] mirrorColor = getMirrorLight(V, lightPoint, normal);
        //int newRed = backColor.getRed();
        //int newGreen = backColor.getGreen();
        //int newBlue = backColor.getBlue();
        float resRed = backColor[0] + diffColor[0] + mirrorColor[0];
        float resGreen = backColor[1] + diffColor[1] + mirrorColor[1];
        float resBlue = backColor[2] + diffColor[2] + mirrorColor[2];
        try {
            res = new Color(Math.min(1, resRed), Math.min(1, resGreen), Math.min(1, resBlue), 1f);

        } catch (Exception ex) {
            System.out.println(backColor[0]) ;
            System.out.println(diffColor[0]) ;
            System.out.println(mirrorColor[0]) ;
            System.out.println(backColor[1]);
            System.out.println(diffColor[1]) ;
            System.out.println(mirrorColor[1]) ;
            System.out.println(backColor[2]);
            System.out.println(diffColor[2]) ;
            System.out.println(mirrorColor[2]) ;
        }
        return res;
    }
}
