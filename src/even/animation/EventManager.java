package even.animation;

import java.util.PriorityQueue;


/**
 *
 * @author evenal
 */
public class EventManager
{
    PriorityQueue<Event> eventQ;
    Animation animation;

    public EventManager() {
        this.eventQ = new PriorityQueue<>();
    }

    public void handleEvents() {
        while (eventQ.peek().frame <= animation.currentFrame) {
            Event e = eventQ.poll();
            e.happen();
        }
    }
}
