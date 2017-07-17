/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package even.animation;


/**
 *
 * @author evenal
 */
public abstract class Event implements Comparable<Event>
{
    int frame;
    protected Actor actor;

    public Event(int frame, Actor actor) {
        this.frame = frame;
        this.actor = actor;
    }

    @Override
    public int compareTo(Event that) {
        return this.frame - that.frame;
    }

    public abstract void happen();
}
