import logic.Camera;
import logic.Transformation;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Display extends Canvas implements MouseWheelListener, MouseListener, MouseMotionListener, KeyListener
{
    private double targetZ;

    private int brack = 0;

    /** The window being used for display */
    private final JFrame m_frame;
    /** The bitmap representing the final image to display */
    private final Bitmap         m_frameBuffer;
    /** Used to display the framebuffer in the window */
    private final BufferedImage m_displayImage;
    /** The pixels of the display image, as an array of byte components */
    private final byte[]         m_displayComponents;
    private float[] zBuffer;
    /** The buffers in the Canvas */
    private final BufferStrategy m_bufferStrategy;
    /** A graphics object that can draw into the Canvas's buffers */
    private final Graphics       m_graphics;

    private final Camera camera;
    private final int a = 255;
    private final int b = 150;
    private final int g = 120;
    private final int rr = 220;
    private final Color color = new Color(rr, g, b, a);

    private Geometry geometry;

    private double xAngle;
    private double yAngle;

    private double cameraXAngle;
    private double cameraYAngle;

    private double scale;

    public int startpointX, endpointX;
    public int startpointY, endpointY;

    /**
     * Creates and initializes a new display.
     *
     * @param width  How wide the display is, in pixels.
     * @param height How tall the display is, in pixels.
     * @param title  The text displayed in the window's title bar.
     */
    public Display(int width, int height, String title, Camera camera, Geometry geometry)
    {
        //Set the canvas's preferred, minimum, and maximum size to prevent
        //unintentional resizing.
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        //Creates images used for display.


        //m_frameBuffer.Clear((byte)0x80);
        //m_frameBuffer.DrawPixel(100, 100, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF);

        //Create a JFrame designed specifically to show this Display.
        m_frame = new JFrame();


        m_frame.add(this);
        m_frame.pack();
        m_frame.setSize(size);
        m_frame.setResizable(false);
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_frame.setLocationRelativeTo(null);
        m_frame.setTitle(title);
        m_frame.setVisible(true);
        System.out.println(m_frame.getSize());
        //Allocates 1 display buffer, and gets access to it via the buffer
        //strategy and a graphics object for drawing into it.
        createBufferStrategy(1);
        m_bufferStrategy = getBufferStrategy();
        m_graphics = m_bufferStrategy.getDrawGraphics();

        int m_frame_width = m_frame.getWidth();
        int m_frame_height = m_frame.getHeight();

        m_frameBuffer = new Bitmap(m_frame_width, m_frame_height);
        m_displayImage = new BufferedImage(m_frame_width, m_frame_height, BufferedImage.TYPE_4BYTE_ABGR);
        m_displayComponents =
                ((DataBufferByte)m_displayImage.getRaster().getDataBuffer()).getData();
        this.xAngle = 0;
        this.yAngle = 0;
        this.cameraXAngle = 0;
        this.cameraYAngle = 0;
        this.scale = 1;

        this.camera = camera;
        camera.setPerspProjectionFOV(45, (float)m_frame_width/m_frame_height, 0.2, 25.0);
        camera.setViewport(m_frame_width, m_frame_height);
        this.targetZ = camera.getTargetZ();
        this.geometry = geometry;

        this.zBuffer = new float[m_frame_width * m_frame_height];

        System.out.println(m_displayComponents.length);
        System.out.println(m_frame_height * m_frame_width * 4);

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        drawImage();
    }

    public void drawPixel(int x, int y, float z, float[] zBuffer, byte a, byte b, byte g, byte r) {
        try {
            //System.out.println("X" + x);
            //System.out.println("Y" + y);
            int width = getWidth();
            int height = getHeight();
            if((x <= width) && (y <= height)) {
                if((zBuffer[y * width + x] - z) < 0) {
                    m_frameBuffer.DrawPixel(x, y, a, b, g, r);
                    zBuffer[y * width + x] = z;
                } else {
                    //brack++;
                    System.out.println("Otbracovka!");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //System.out.println("Index out of bounds!!!!!!!");
        }
    }

    private void drawImage() {
        clearScreen();
        initZBuffer();
        System.out.println("target");
        System.out.println(camera.getTarget());
        System.out.println("trans");
        System.out.println(camera.getTransformation());
        /*int a = 150;
        int b = r.nextInt(256);
        int g = r.nextInt(256);
        int rr = r.nextInt(256);*/

         for(Triangle triangle: geometry.getTriangleList()) {
             //triangle.sortNewVertices();
            Vertex v1 = triangle.getVertexByIndex(0);
            //System.out.println(v1);
            //Transformation tr = camera.getTransformation();

            Vertex v2 = triangle.getVertexByIndex(1);
            //System.out.println(v2);
            Vertex v3 = triangle.getVertexByIndex(2);
            //Transformation temp = this.transformMatrix.multiplyByMatrix(camera.getObserver().transpose()).multiplyByMatrix(camera.getProjection().transpose());
            //Transformation res = this.constMatrix.multiplyByMatrix(this.transformMatrix);
            //System.out.println(res1);
             //res = temp;
             //System.out.println("transf");
             //System.out.println(camera.getTransformation());
             Transformation res1 = camera.getViewport().multiplyByMatrix(camera.getProjection()).multiplyByMatrix(camera.getObserver().rotateX(cameraXAngle).rotateY(cameraYAngle)).multiplyByMatrix(camera.getTransformation().rotateY(yAngle).rotateX(xAngle).scale(scale));
                //Transformation t = camera.getViewport();
           /* Vector3 vector1 = t.multiplyByVector(res.multiplyByVector(v1.getPosition()));
            //System.out.println(vector1);

            Vector3 vector2 = t.multiplyByVector(res.multiplyByVector(v2.getPosition()));
            //System.out.println(vector2);
            Vector3 vector3 = t.multiplyByVector(res.multiplyByVector(v3.getPosition()));
            //System.out.println(vector3);*/
             Vector3 vector1 = res1.multiplyByVector(v1.getPosition());
             //System.out.println("W" + vector1.getVectorElement(3));
             vector1.divideByW();
             v1.setNewPosition(vector1);

             Vector3 vector2 = res1.multiplyByVector(v2.getPosition());
             vector2.divideByW();
             v2.setNewPosition(vector2);
             //System.out.println(vector2);
             Vector3 vector3 = res1.multiplyByVector(v3.getPosition());
             vector3.divideByW();
             v3.setNewPosition(vector3);
             //triangle.sortNewVertices();
             triangle.updateSides();

             /*int a = 100;
             int b = r.nextInt(256);
             int g = r.nextInt(256);
             int rr = r.nextInt(256);*/
             if(triangle.isVisible(camera.getTarget().substractVector(camera.getEye()))){
                 drawRasterizedTriangle(triangle.gerScanLines(), color, zBuffer);
             } else {
                 System.out.println("Not normal!");
                 brack++;
             }

             System.out.println("Bracks = " + brack);
             //System.out.println(vector3);
             //swapBuffers();
           /* Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), vector1.getVectorElement(2), vector2.getVectorElement(2), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)),this, a, b, g, rr, zBuffer);
            Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), vector1.getVectorElement(2), vector3.getVectorElement(2), Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), this, a, b, g, rr, zBuffer);
            Bresenhime.drawBresenhamLine(Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), vector3.getVectorElement(2), vector2.getVectorElement(2), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)), this, a, b, g, rr, zBuffer);
            */

        }
        swapBuffers();
    }



    private void initZBuffer() {
        brack = 0;
        for(int i = 0; i < zBuffer.length; i++) {
            zBuffer[i] = Integer.MIN_VALUE;
        }
    }

    private void drawRasterizedTriangle(List<Side> sides, Color color, float[] zBuffer) {
        for(Side side: sides) {
            Bresenhime.drawBresenhamLine(Math.round(side.getxStart()), Math.round(side.getyStart()), side.getzStart(), side.getzEnd(), Math.round(side.getxEnd()), Math.round(side.getyEnd()), this, (byte) color.getAlpha(), (byte) color.getBlue(), (byte) color.getGreen(), (byte) color.getRed(), zBuffer);
        }
    }

    public void clearScreen() {
        m_frameBuffer.Clear((byte)0xFF);
        Arrays.fill(m_displayComponents, (byte)0xFF);
    }

    /**
     * Displays in the window.
     */
    private void swapBuffers()
    {
        //Display components should be the byte array used for displayImage's pixels.
        //Therefore, this call should effectively copy the frameBuffer into the
        //displayImage.
        Arrays.fill(m_displayComponents, (byte)0xFF);
        //m_graphics.drawImage(m_displayImage, 0, 0, m_frameBuffer.GetWidth(), m_frameBuffer.GetHeight(), null);
        //m_bufferStrategy.show();
        m_frameBuffer.CopyToByteArray(m_displayComponents);
        System.out.println(m_frameBuffer.GetHeight());
        System.out.println(m_frameBuffer.GetWidth());
        //m_graphics.clearRect(0, 0, m_frameBuffer.GetWidth(), m_frameBuffer.GetHeight());
        //m_displayImage.getData()
        m_graphics.drawImage(m_displayImage, 0, 0, m_frameBuffer.GetWidth(), m_frameBuffer.GetHeight(), null);
        m_bufferStrategy.show();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("MouseWheelListenerDemo.mouseWheelMoved");

        // If wheel rotation value is a negative it means rotate up, while
        // positive value means rotate down
        if (e.getWheelRotation() < 0) {
            System.out.println("Rotated Up... " + e.getWheelRotation());
            this.scale *= (Math.abs(e.getWheelRotation()) * 1.2) ;

        } else {
            System.out.println("Rotated Down... " + e.getWheelRotation());
            this.scale /= (Math.abs(e.getWheelRotation()) * 1.2);
        }

        //this.transformMatrix = (new Transformation().scale(this.scale)).rotateY(this.yAngle).rotateX(this.xAngle);

        // Get scrolled unit amount
        System.out.println("ScrollAmount: " + e.getScrollAmount());
        System.out.println("ScrollUnitsAmount: " + e.getUnitsToScroll());


        // WHEEL_UNIT_SCROLL representing scroll by unit such as the
        // arrow keys. WHEEL_BLOCK_SCROLL representing scroll by block
        // such as the page-up or page-down key.
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            System.out.println("MouseWheelEvent.WHEEL_UNIT_SCROLL");



        }

        if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
            System.out.println("MouseWheelEvent.WHEEL_BLOCK_SCROLL");
        }
        drawImage();
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

        int deltaX = endpointX - startpointX;
        int deltaY = endpointY - startpointY;

        System.out.println("Rotate X to :" + deltaX);
        System.out.println("Rotate Y to :" + deltaY);

        startpointX = endpointX;
        startpointY = endpointY;

        double targetZ = camera.getTargetZ();

        double xAngle = Math.atan(deltaY / targetZ * 0.5);
        System.out.println(xAngle);

        double yAngle = Math.atan(deltaX / targetZ * 0.5);
        System.out.println(yAngle);

        this.yAngle += yAngle;
        this.xAngle += -xAngle;

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


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case 81: // "Q"
                System.out.println("Q");
                camera.addEyeZ(-0.1);
                break;
            case 69: // "E"
                System.out.println("E");
                camera.addEyeZ(0.1);
                break;
            case 87: // "W"
                System.out.println("W");
                camera.addEyeY(0.1);
                break;
            case 83: // "S"
                System.out.println("S");
                camera.addEyeY(-0.1);
                break;
            case 65: // "A"
                System.out.println("A");
                camera.addEyeX(-0.1);
                break;
            case 68: // "D"
                System.out.println("D");
                camera.addEyeX(0.1);
                break;
            // camera rotation
            case 37: // LEFT arrow
                System.out.println("LEFT");
                cameraYAngle -= 0.2;
                break;
            case 38: // UP arrow
                System.out.println("UP");
                cameraXAngle += 0.2;
                break;
            case 39: // RIGHT arrow
                System.out.println("RIGHT");
                cameraYAngle += 0.2;
                break;
            case 40: // DOWN arrow
                System.out.println("DOWN");
                cameraXAngle -= 0.2;
                break;
            case 100: // num4
                System.out.println("num4");
                camera.addModelX(1);
                break;
            case 102: // num6
                System.out.println("num6");
                camera.addModelX(-1);
                break;
            case 104: // num8
                System.out.println("num8");
                camera.addModelY(0.5);
                break;
            case 98: // num2
                System.out.println("num2");
                camera.addModelY(-0.5);
                break;
            case 103: // num7
                System.out.println("num7");
                camera.addModelZ(1);
                break;
            case 105: // num9
                System.out.println("num9");
                camera.addModelZ(-1);
                break;
        }
        drawImage();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}