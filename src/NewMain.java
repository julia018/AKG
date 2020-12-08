import logic.Camera;
import logic.OBJFileParser;
import logic.Transformation;
import model.Geometry;
import model.Triangle;
import model.Vector3;
import model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;


public class NewMain extends JPanel{
    //public static JFrame frame = new JFrame("3D");
    private Display display;

    public int startpointX, endpointX;
    public int startpointY, endpointY;
    private static Geometry g;
    private static Display d;
    private static Camera cam;
    private static Transformation res1;

    public NewMain() {
        //setPreferredSize(new Dimension(870, 650));
    }

    public static void main(String[] args) throws IOException {

        Camera camera = new Camera(new Vector3(0, 0, 5), new Vector3(0, 0, 0), new Vector3(0, 1,0));
        camera.setPerspProjectionFOV(45, 900/800, 0.2, 25.0);
        camera.setViewport(900, 800);
        //camera.setOrthoProjection(800, 600, 0, -1000);
        //camera.setPerspProjection(800.0, 600.0, 20.0, 1000.0);

        //camera.setObserver(new Vector3(0, 0, 5), new Vector3(0, 0, 0), new Vector3(0, 1, 0));

        cam = camera;
        Geometry geometry = OBJFileParser.parseOBJFile(new File("res/Model.obj"));
        //Collections.reverse(geometry.getTriangleList());
        g = geometry;
        Transformation res = cam.getProjection().multiplyByMatrix(cam.getObserver());
        //Transformation obs = cam.getObserver();
        res1 = cam.getViewport().multiplyByMatrix(res);
        d = new Display(900, 800, "Software Rendering", camera, res1, geometry);

    }



}

