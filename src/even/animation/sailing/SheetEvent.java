package even.animation.sailing;

import even.animation.Actor;
import even.animation.Event;


/**
 *
 * @author evenal
 */
public class SheetEvent extends Event
{
    double sheeting;

    public SheetEvent(double sheeting, int frame, Actor actor) {
        super(frame, actor);
        this.sheeting = sheeting;
    }

    public void happen() {
        Boat boat = (Boat) actor;
        boat.rig.slack = 1 - sheeting;
    }
}
