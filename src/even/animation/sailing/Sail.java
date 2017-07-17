package even.animation.sailing;

import java.awt.Shape;


/**
 *
 * @author evenal
 */
public class Sail
{
    SailDesign design;
    double angle;
    boolean isSet;
    Rig.State state;
    Course.Tack tack;
    Shape shape;

    public void update(double angle, Rig.State state, Course.Tack tack) {
        this.angle = angle;
        this.state = state;
        this.tack = tack;

        shape = design.getShape(tack, state);
    }
}
