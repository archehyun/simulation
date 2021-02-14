/*
 *  ====================================================================
 *  DemoAnimation.java : This applet animates a butterfly and from a
 *  java programming perspective is interesting in a couple of ways.
 *  First, the program uses a buffered image and affine transformations
 *  (scaling, translation, rotation) to transform the size and motion
 *  of the butterfly. The graphics animation works by storing the
 *  transformed image in a buffer and flipping it to the screen after
 *  applying the transformation to the buffered image.  Thus, you see
 *  the butterfly moving around and changing size. Buttons are
 *  provided to start and stop the animation.
 *
 *  The second interest aspect of this program is the animation canvas,
 *  which runs as a separate thread of execution. The affine trans-
 *  formation is applied to the image that is stored in the buffered
 *  image.
 *
 *  Adapted from : Pantham S., Pure JFC Swing, 1999.
 *  Modified by : Mark Austin                                March, 2001
 *  ====================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.net.URL;

public class Test extends JApplet {
    DemoAnimationCanvas canvas;
    JButton startButton, stopButton;
    URL codeBase;      // URL for applet codebase

    public void init() {

        // 1. Get the content pane and assign layout

        Container container = getContentPane();

        // 2. Get codebase for applet ....

        codeBase = getCodeBase();

        // 3. Add the canvas with rectangles

        canvas = new DemoAnimationCanvas( codeBase );
        container.add(canvas);

        // 4. Add buttons to start or stop the animation

        startButton = new JButton("Start Animation");
        startButton.addActionListener(new ButtonListener());

        stopButton  = new JButton("Stop Animation");
        stopButton.addActionListener(new ButtonListener());

        JPanel panel = new JPanel();
        panel.add(startButton); panel.add(stopButton);
        container.add(BorderLayout.SOUTH, panel);

    }

    // 5. Button listener

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton temp = (JButton) e.getSource();

            if (temp.equals(startButton)) {
                canvas.start();
            }
            else if (temp.equals(stopButton)) {
                canvas.stop();
            }
        }
    }
}

// 6. Definition of the canvas that displays animation.

class DemoAnimationCanvas extends JPanel implements Runnable {
    Thread thread;

    Image image;
    BufferedImage bi;

    double x, y, xi, yi; 
    int rotate;
    double scale; int UP = 0; int DOWN = 1;
    int scaleDirection;

    URL butterflyURL;  // URL for butterfly image ...

    // 7. DemoAnimationCanvas constructor ...

    DemoAnimationCanvas( URL codeBase ) {

        setBackground(Color.green);  // For canvas background color
       
        // Construct URL for image icon ....

        try {
           butterflyURL = new URL( codeBase, "images/butterfly.gif" );
        } catch ( java.net.MalformedURLException e ) {
           System.out.println("Badly specified URL!!");
        }

        // Download and save image icon ....

        image = new ImageIcon( butterflyURL ).getImage();

        MediaTracker mt = new MediaTracker( this );
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading image.");
        }

        // If the image has an unknown width, the image is not created
        // by using the specified file. Therefore exit the program.  

        if (image.getWidth(this) == -1) {
            System.out.println("*** Make sure you have the image "
                + "(butterfly.gif) file in the same directory.***");
            System.exit(0);
        }

        rotate = (int) (Math.random() * 360);
        scale = Math.random() * 1.5;
        scaleDirection = DOWN;

        xi = 50.0; yi = 50.0;

    }

    // This method computes the step size for animation.

    public void step( int w, int h ) {
        // upgrade the translation coordinates
        x += xi; y += yi;

        // the x and y exceed the dimensions of canvas
        if (x > w) {
            x = w - 1;
            xi = Math.random() * -w/32;
        }
        if (x < 0) {
            x = 2;
            xi =  Math.random() * w/32;
        }
        if (y > h) {
            y = h - 2;
            yi = Math.random() * -h/32;
        }
        if (y < 0) {
            y = 2;
            yi = Math.random() * h/32;
        }

        // upgrade the rotation coordinates

        if ((rotate += 5) == 360) {
            rotate = 0;
        }

        // upgrade the scaling coordinates depending on the
        // increase or decrease in size. If the increase in size
        // exceeds a limit of 1.5, decrease the size. If the
        // decrease in size falls below 0.5, increase the size.

        if (scaleDirection == UP) {

            if ((scale += 0.5) > 1.5) {
                scaleDirection = DOWN;
            }

        }

        // upgrade the scaling coordinates

        else if (scaleDirection == DOWN) {
            if ((scale -= .05) < 0.5) {
                scaleDirection = UP;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();

        bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D big =  bi.createGraphics();

        step(d.width, d.height);

        AffineTransform at = new AffineTransform();
        at.setToIdentity();
        at.translate(x, y);
        at.rotate(Math.toRadians(rotate));
        at.scale(scale, scale);
        big.drawImage(image, at, this);

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(bi, 0 , 0, null);

        big.dispose();
    }

    // Starts the thread

    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    // Stops the thread

    public void stop() {
        if (thread != null)
            thread.interrupt();
        thread = null;
    }

    // Runs the thread

    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
        }
        thread = null;
    }
}