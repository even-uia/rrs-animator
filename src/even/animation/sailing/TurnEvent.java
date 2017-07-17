package even.animation.sailing;

import even.animation.Actor;
import even.animation.Event;


/**
 *
 * @author evenal
 */
public class TurnEvent extends Event
{
    double targetHdg;

    public TurnEvent(double targetHdg, int frame, Actor actor) {
        super(frame, actor);
        this.targetHdg = targetHdg;
    }

    public void happen() {
        Boat boat = (Boat) actor;
        boat.kineticsMgr.targetHdg = targetHdg;
    }
}
