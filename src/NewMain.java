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


public class NewMain extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    public static JFrame frame = new JFrame("3D");


    public int startpointX, endpointX;
    public int startpointY, endpointY;
    private static Geometry g;
    private static Camera cam;
    private static Transformation res1;

    public NewMain() {
        setPreferredSize(new Dimension(870, 650));
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public static void main(String[] args) throws IOException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        NewMain newMain = new NewMain();
        JComponent newContentPane = newMain;
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
        frame.addMouseWheelListener(newMain);
        frame.addMouseListener(newMain);
        frame.addMouseMotionListener(newMain);

        Camera camera = new Camera();
        camera.setViewport(870, 650, 0, -1000);
        camera.setOrthoProjection(870, 650, 0, -1000);
        camera.setObserver(new Vector3(0, 0, 20), new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        cam = camera;
        Geometry geometry = OBJFileParser.parseOBJFile(new File("res/sphere.obj"));
        g = geometry;
        res1 = cam.getViewport().multiplyByMatrix(cam.getProjection()).multiplyByMatrix(cam.getObserver());
         drawImage();
    }

    public static void drawImage() {
        for(Triangle triangle: g.getTriangleList()) {
            Vertex v1 = triangle.getVertexByIndex(0);
            System.out.println(v1);

            Vertex v2 = triangle.getVertexByIndex(1);
            System.out.println(v2);
            Vertex v3 = triangle.getVertexByIndex(2);
            Transformation res = res1.multiplyByMatrix(new Transformation().scale(50, 50, 50));
            //System.out.println(res1);
            Vector3 vector1 = res.multiplyByVector(v1.getPosition());
            System.out.println(vector1);

            Vector3 vector2 = res.multiplyByVector(v2.getPosition());
            System.out.println(vector2);
            Vector3 vector3 = res.multiplyByVector(v3.getPosition());
            System.out.println(vector3);
            Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)), frame.getGraphics());
            Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), frame.getGraphics());
            Bresenhime.drawBresenhamLine(Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)), frame.getGraphics());

        }
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("MouseWheelListenerDemo.mouseWheelMoved");

        // If wheel rotation value is a negative it means rotate up, while
        // positive value means rotate down
        if (e.getWheelRotation() < 0) {
            System.out.println("Rotated Up... " + e.getWheelRotation());
        } else {
            System.out.println("Rotated Down... " + e.getWheelRotation());
        }

        // Get scrolled unit amount
        System.out.println("ScrollAmount: " + e.getScrollAmount());

        // WHEEL_UNIT_SCROLL representing scroll by unit such as the
        // arrow keys. WHEEL_BLOCK_SCROLL representing scroll by block
        // such as the page-up or page-down key.
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            System.out.println("MouseWheelEvent.WHEEL_UNIT_SCROLL");
        }

        if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
            System.out.println("MouseWheelEvent.WHEEL_BLOCK_SCROLL");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startpointX = e.getX();
        startpointY = e.getY();

    }


    @Override
    public void mouseDragged(MouseEvent e) {

        endpointX = e.getX();
        endpointY = e.getY();

        int deltaX=endpointX - startpointX;
        int deltaY=endpointY - startpointY;

        System.out.println("Rotate X to :" + deltaX);
        System.out.println("Rotate Y to :" + deltaY);

        startpointX = endpointX;
        startpointY = endpointY;

        frame.getGraphics().clearRect(0, 0, 870, 650);
        drawImage();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

