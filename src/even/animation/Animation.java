/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package even.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author evenal
 */
public class Animation implements ActionListener
{
    private EventManager eventMgr;
    private ActorManager actorMgr;
    private RenderManager renderMgr;

    int currentFrame; // the time
    int maxFrame; // animation ends here
    int frameRate = 10;
    Timer timer;

    public Animation(JPanel canvas) {
        renderMgr = new RenderManager(canvas);
        actorMgr = new ActorManager();
        eventMgr = new EventManager();
        currentFrame = 0;
        timer = new Timer(1000 / frameRate, this);
    }

    public void start() {
        timer.start();
    }

    /**
     * Compute the actions, and animate a frame
     */
    public void animateFrame() {
        currentFrame++;
        if (currentFrame > maxFrame) timer.stop();
        eventMgr.handleEvents();
        actorMgr.act();
        renderMgr.render();
    }

    /**
     * Implements actionlistener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        animateFrame();
    }
}
