/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package even.animation;

import java.util.ArrayList;


/**
 *
 * @author evenal
 */
public class ActorManager
{
    ArrayList<Actor> actors;

    public ActorManager() {
        actors = new ArrayList<>();
    }

    public void act() {
        for (Actor a : actors) a.act();
    }
}
