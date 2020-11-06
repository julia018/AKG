import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class NewMain extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    public static JFrame frame = new JFrame("3D");


    public int startpointX, endpointX;
    public int startpointY, endpointY;

    public NewMain() {
        setPreferredSize(new Dimension(870, 675));
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public static void main(String[] args) {
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


        Bresenhime.drawBresenhamLine(2, 2, 1000, 1000, frame.getGraphics());
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

