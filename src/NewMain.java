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


        Camera camera = new Camera();
        camera.setViewport(800, 600, 0, -1000);
        camera.setOrthoProjection(800, 600, 0, -1000);
        camera.setObserver(new Vector3(0, 0, 20), new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        cam = camera;
        Geometry geometry = OBJFileParser.parseOBJFile(new File("res/Model.obj"));
        g = geometry;
        res1 = cam.getViewport().multiplyByMatrix(cam.getProjection()).multiplyByMatrix(cam.getObserver());
        d = new Display(800, 600, "Software Rendering", camera, res1, geometry);

    }



}

