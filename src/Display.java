import logic.Camera;
import logic.Transformation;
import model.Geometry;
import model.Triangle;
import model.Vector3;
import model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Display extends Canvas implements MouseWheelListener, MouseListener, MouseMotionListener
{
    private final double TARGET_Z = 20.0;

    /** The window being used for display */
    private final JFrame m_frame;
    /** The bitmap representing the final image to display */
    private final Bitmap         m_frameBuffer;
    /** Used to display the framebuffer in the window */
    private final BufferedImage m_displayImage;
    /** The pixels of the display image, as an array of byte components */
    private final byte[]         m_displayComponents;
    /** The buffers in the Canvas */
    private final BufferStrategy m_bufferStrategy;
    /** A graphics object that can draw into the Canvas's buffers */
    private final Graphics       m_graphics;

    private final Camera camera;

    private final Transformation constMatrix;

    private Geometry geometry;

    private Transformation transformMatrix;

    private double xAngle;
    private double yAngle;

    private int scale;

    public int startpointX, endpointX;
    public int startpointY, endpointY;

    /**
     * Creates and initializes a new display.
     *
     * @param width  How wide the display is, in pixels.
     * @param height How tall the display is, in pixels.
     * @param title  The text displayed in the window's title bar.
     */
    public Display(int width, int height, String title, Camera camera, Transformation constMatrix, Geometry geometry)
    {
        //Set the canvas's preferred, minimum, and maximum size to prevent
        //unintentional resizing.
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        //Creates images used for display.
        m_frameBuffer = new Bitmap(width, height);
        m_displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        m_displayComponents =
                ((DataBufferByte)m_displayImage.getRaster().getDataBuffer()).getData();

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



        //Allocates 1 display buffer, and gets access to it via the buffer
        //strategy and a graphics object for drawing into it.
        createBufferStrategy(1);
        m_bufferStrategy = getBufferStrategy();
        m_graphics = m_bufferStrategy.getDrawGraphics();

        this.xAngle = 0;
        this.yAngle = 0;
        this.scale = 250;

        this.camera = camera;
        this.constMatrix = constMatrix;
        this.transformMatrix = new Transformation().scale(this.scale);
        this.geometry = geometry;



        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        drawImage();
    }

    public void drawPixel(int x, int y) {
        try {
            m_frameBuffer.DrawPixel(x, y, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xF0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!!!!!!!");
        }
    }

    private void drawImage() {
        clearScreen();
         for(Triangle triangle: geometry.getTriangleList()) {
            Vertex v1 = triangle.getVertexByIndex(0);
            //System.out.println(v1);

            Vertex v2 = triangle.getVertexByIndex(1);
            //System.out.println(v2);
            Vertex v3 = triangle.getVertexByIndex(2);
            Transformation res = this.constMatrix.multiplyByMatrix(this.transformMatrix);
            //System.out.println(res1);
            Vector3 vector1 = res.multiplyByVector(v1.getPosition());
            //System.out.println(vector1);

            Vector3 vector2 = res.multiplyByVector(v2.getPosition());
            //System.out.println(vector2);
            Vector3 vector3 = res.multiplyByVector(v3.getPosition());
            //System.out.println(vector3);
            Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)), this);
            Bresenhime.drawBresenhamLine(Math.round(vector1.getVectorElement(0)), Math.round(vector1.getVectorElement(1)), Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), this);
            Bresenhime.drawBresenhamLine(Math.round(vector3.getVectorElement(0)), Math.round(vector3.getVectorElement(1)), Math.round(vector2.getVectorElement(0)), Math.round(vector2.getVectorElement(1)), this);

        }
        swapBuffers();
    }

    public void clearScreen() {
        m_frameBuffer.Clear((byte)0x00);
    }

    /**
     * Displays in the window.
     */
    private void swapBuffers()
    {
        //Display components should be the byte array used for displayImage's pixels.
        //Therefore, this call should effectively copy the frameBuffer into the
        //displayImage.
        m_frameBuffer.CopyToByteArray(m_displayComponents);
        m_graphics.drawImage(m_displayImage, 0, 0,
                m_frameBuffer.GetWidth(), m_frameBuffer.GetHeight(), null);
        m_bufferStrategy.show();
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
        System.out.println("ScrollUnitsAmount: " + e.getUnitsToScroll());


        // WHEEL_UNIT_SCROLL representing scroll by unit such as the
        // arrow keys. WHEEL_BLOCK_SCROLL representing scroll by block
        // such as the page-up or page-down key.
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            System.out.println("MouseWheelEvent.WHEEL_UNIT_SCROLL");
            this.scale += e.getUnitsToScroll() * 5;
            this.transformMatrix = (new Transformation().scale(this.scale)).rotateY(this.yAngle).rotateX(this.xAngle);

            drawImage();
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

        int deltaX = endpointX - startpointX;
        int deltaY = endpointY - startpointY;

        System.out.println("Rotate X to :" + deltaX);
        System.out.println("Rotate Y to :" + deltaY);

        startpointX = endpointX;
        startpointY = endpointY;

        double xAngle = Math.atan(deltaY / TARGET_Z);
        System.out.println(xAngle);

        double yAngle = Math.atan(deltaX / TARGET_Z);
        System.out.println(yAngle);

        this.yAngle += yAngle;
        this.xAngle += -xAngle;

        //this.transformMatrix = this.transformMatrix.rotateX(yAngle);
        this.transformMatrix = (new Transformation().scale(this.scale)).rotateY(this.yAngle).rotateX(this.xAngle);


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