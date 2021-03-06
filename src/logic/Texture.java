package logic;

import model.Vector2;
import model.Vector3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {

    private final String specularFileName = "Specular.png";
    private final String normalFileName = "Normal.png";
    private final String albedoFileName = "Albedo.png";

    private int[] specularTextureColors;
    private Integer specImageWidth;
    private Integer specImageHeight;
    private int[] normalTextureColors;
    private int normalImageWidth;
    private int normalImageHeight;
    private int[] albedoTextureColors;
    private int albedoImageWidth;
    private int albedoImageHeight;

    public Texture() {
        try {
            specularTextureColors = loadTexture(specularFileName);
            albedoTextureColors = loadTexture(albedoFileName);
            normalTextureColors = loadTexture(normalFileName);
        } catch (IOException e) {
            System.out.println("Error in texture file loading!");
            e.printStackTrace();
        }
    }

    private int[] loadTexture(String filename) throws IOException {

        File textureFile = new File("res/" + filename);
        BufferedImage bi = ImageIO.read( textureFile );
        BufferedImage img2 = new BufferedImage( bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB );
        img2.getGraphics().drawImage( bi, 0, 0, null );
        int[] data = ( (DataBufferInt) img2.getRaster().getDataBuffer() ).getData();
        int imageWidth = img2.getWidth();
        int imageHeight = img2.getHeight();
        int length = data.length;

        switch (filename) {
            case specularFileName:
                setSpecImageWidth(imageWidth);
                setSpecImageHeight(imageHeight);
                break;
            case normalFileName:
                setNormalImageWidth(imageWidth);
                setNormalImageHeight(imageHeight);
                break;
            case albedoFileName:
                setAlbedoImageWidth(imageWidth);
                setAlbedoImageHeight(imageHeight);
                break;
        }

        return data;
    }

    public Color getSpecularColor(Vector2 uvVector) {
        float u = uvVector.getX();
        float v = uvVector.getY();
        int b = 0;
        int widthIndex = (int)(u * (specImageWidth-1) + 0.5f);
        int heightIndex = (int)((1 - v) * (specImageHeight-1) + 0.5f);
        try {
            b = specularTextureColors[(heightIndex) * specImageWidth + (widthIndex)];
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(uvVector.getX());
            System.out.println(uvVector.getY());
        }

        try{
            return new Color(b);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            System.out.println(b);
            return null;
        }

    }

    public Vector3 getNormals(Vector2 uvVector) {
        float u = uvVector.getX();
        float v = uvVector.getY();
        int b = 0;
        int widthIndex = (int)(u * normalImageWidth);
        int heightIndex = (int)((1 - v) * normalImageHeight);
        try {
            b = normalTextureColors[(heightIndex) * normalImageWidth + (widthIndex)];
            } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(uvVector.getX());
            System.out.println(uvVector.getY());
        }

        try{
            Color colorNormal = new Color(b, true);
            return new Vector3((float) (colorNormal.getRed()/255.0), (float)(colorNormal.getGreen()/255.0), (float)(colorNormal.getBlue()/255.0)).multByValue(2).substractVector(new Vector3(1, 1, 1)).getNormalized();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            System.out.println(b);
            return null;
        }

    }

    public Color getAlbedoColor(Vector2 uvVector) {
        float u = uvVector.getX();
        float v = uvVector.getY();
        int b = 0;
        int widthIndex = (int)(u * albedoImageWidth);
        int heightIndex = (int)((1 - v) * albedoImageHeight);
        try {
            b = albedoTextureColors[(heightIndex) * albedoImageWidth + (widthIndex)];
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(uvVector.getX());
            System.out.println(uvVector.getY());
        }

        try{
            return new Color(b);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            System.out.println(b);
            return null;
        }

    }

    public Integer getSpecImageWidth() {
        return specImageWidth;
    }

    public void setSpecImageWidth(Integer specImageWidth) {
        this.specImageWidth = specImageWidth;
    }

    public Integer getSpecImageHeight() {
        return specImageHeight;
    }

    public void setSpecImageHeight(Integer specImageHeight) {
        this.specImageHeight = specImageHeight;
    }

    public int getNormalImageWidth() {
        return normalImageWidth;
    }

    public void setNormalImageWidth(int normalImageWidth) {
        this.normalImageWidth = normalImageWidth;
    }

    public int getNormalImageHeight() {
        return normalImageHeight;
    }

    public void setNormalImageHeight(int normalImageHeight) {
        this.normalImageHeight = normalImageHeight;
    }

    public int getAlbedoImageWidth() {
        return albedoImageWidth;
    }

    public void setAlbedoImageWidth(int albedoImageWidth) {
        this.albedoImageWidth = albedoImageWidth;
    }

    public int getAlbedoImageHeight() {
        return albedoImageHeight;
    }

    public void setAlbedoImageHeight(int albedoImageHeight) {
        this.albedoImageHeight = albedoImageHeight;
    }
}
