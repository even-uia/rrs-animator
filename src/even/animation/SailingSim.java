/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package even.animation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author evenal
 */
public class SailingSim extends JFrame implements ActionListener
{

    private static final double HDG_INC = Math.PI / 360;
    private static final double WORLD_SIZE = 32;
    private static final int CANVAS_SIZE = 600;
    private static final double SCALE = CANVAS_SIZE / WORLD_SIZE;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SailingSim sim = new SailingSim();
        sim.start();
    }

    Canvas canvas;
    Boat boat;
    Timer timer;

    public SailingSim() {
        super("SailingSim");
        canvas = new Canvas();
        canvas.setBackground(Color.blue);
        canvas.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));

        getContentPane().add(canvas, BorderLayout.CENTER);
        boat = new Boat();
    }

    public void start() {
        pack();
        setVisible(true);

        timer = new Timer(50, this);
        timer.start();
    }

    int count = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (count > 720) return;
        count++;

        boat.x = 16 + 15 * Math.cos(boat.hdg);
        boat.y = 16 - 15 * Math.sin(boat.hdg);
        boat.update();

        canvas.repaint();
        timer.restart();
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }


    public class Canvas extends JPanel
    {

        AffineTransform scaleTx;

        public Canvas() {
            scaleTx = AffineTransform.getScaleInstance(SCALE, SCALE);
            setBackground(Color.blue);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(Color.blue);
            g2.transform(scaleTx);
            boat.render(g2);
        }
    }


    public class Boat
    {

        Path2D hull;
        double x, y, hdg, spd;
        AffineTransform last;
        Sail mainsail;

        public Boat() {
            x = 35;
            y = 20;
            hull = new Path2D.Double();
            hull.moveTo(0, -2.5);
            hull.curveTo(0.8, -0.5, 0.8, 1, 0.5, 2);
            hull.lineTo(-0.5, 2);
            hull.curveTo(-0.8, 1, -0.8, -0.5, 0, -2.5);
            hdg = 0;

            mainsail = new Sail(-0.5, 2.5, 0.2);
        }

        public void update() {
            hdg += HDG_INC;
            if (hdg > Math.PI) hdg = hdg - 2 * Math.PI;

            mainsail.updateSheetAngle(hdg);
        }

        public void erase(Graphics2D g2) {
            if (last == null) return;
            AffineTransform sav = g2.getTransform();
            g2.setTransform(last);
            g2.setColor(Color.blue);
            g2.fill(hull);
            g2.setTransform(sav);

        }

        public void render(Graphics2D g2) {
            AffineTransform save = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(-hdg);

            last = g2.getTransform();
            g2.setColor(Color.blue);
            g2.fill(hull);
            g2.setColor(Color.red);
            g2.fill(hull);
            g2.setColor(Color.black);
            g2.setStroke((new BasicStroke(0f)));
            g2.draw(hull);

            mainsail.render(g2);

            g2.setTransform(save);
        }
    }

    public static double hdgToTwa(double hdg) {
        double twa = hdg;
        while (twa > Math.PI) twa -= 2 * Math.PI;
        while (twa < -Math.PI) twa += 2 * Math.PI;
        return twa;
    }


    private class Sail implements Renderable
    {

        double ymast;
        double sheetAngle;
        boolean trimmed = false;
        boolean flapping;
        boolean starboard;
        Path2D starboardShape, portShape, flappingShape;

        public Sail(double ymast, double chord, double camber) {
            this.ymast = ymast;
            starboardShape = new Path2D.Double();
            starboardShape.moveTo(0, 0);
            starboardShape.curveTo(-camber, chord / 4,
                                   -camber, chord / 2,
                                   0, chord);
            portShape = new Path2D.Double();
            portShape.moveTo(0, 0);
            portShape.curveTo(camber, chord / 4,
                              camber, chord / 2,
                              0, chord);
            flappingShape = new Path2D.Double();
            flappingShape.moveTo(0, 0);
            flappingShape.curveTo(camber, chord / 8,
                                  -camber, 3 * chord / 8,
                                  0, chord / 2);
            flappingShape.curveTo(camber, 5 * chord / 8,
                                  -camber, 7 * chord / 8,
                                  0, chord);
        }

        public void updateSheetAngle(double hdg) {
            starboard = hdg > 0;
            double twa = starboard ? hdg : -hdg;

            if (trimmed) {
                if (twa < 0.5) {
                    sheetAngle = twa;
                }
                else if (twa < 0.78) {
                    sheetAngle = 0.5;
                }
                else {
                    sheetAngle = 0.5 + (twa - 0.78) * (1.57 - 0.5) / (3.14 -
                            0.78);
                }
            }
            else {
                sheetAngle = twa;
                if (sheetAngle > 1.5) sheetAngle = 1.5;
            }
            flapping = isFlapping(twa, sheetAngle);
        }

        boolean isFlapping(double twa, double sheetAngle) {
            double aoa = twa - sheetAngle;
            flapping = aoa < 0.105;
            return flapping;
        }

        public Path2D getShape() {
            if (flapping) return flappingShape;
            else if (starboard) return starboardShape;
            else return portShape;
        }

        public void render(Graphics2D g2) {
            AffineTransform save = g2.getTransform();

            g2.translate(0, ymast);
            g2.rotate(starboard ? sheetAngle : -sheetAngle);
            g2.setColor(Color.white);
            g2.draw(getShape());

            g2.setTransform(save);
        }

        public List<Renderable> getChildren() {
            return new ArrayList<>();
        }
    }
}
