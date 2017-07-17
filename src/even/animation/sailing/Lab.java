package even.animation.sailing;

import even.animation.RenderManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author evenal
 */
public class Lab implements ActionListener
{
   public static final String TYPE = "singlehander";
   public static final Color COLOUR = Color.yellow;

   JFrame window;
   RenderManager rm;
   BoatFactory bf = BoatFactory.getInstance();
   Boat boat;
   Timer timer;
   int angle = -180;

   public Lab() {
      timer = new Timer(500, this);
      window = new JFrame();
      JPanel canvas = new JPanel();
      Dimension size = new Dimension(500, 500);
      canvas.setMinimumSize(size);
      canvas.setPreferredSize(size);
      window.add(canvas, BorderLayout.CENTER);
      rm = new RenderManager(canvas);
      boat = bf.buildBoat(TYPE, "Jumbo", COLOUR);
      boat.kineticsMgr.pos.x = 5;
      boat.kineticsMgr.pos.y = 5;
      rm.add(boat);

      window.pack();
      window.setVisible(true);
      timer.start();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (angle < 180) {
         boat.kineticsMgr.setHdg(Math.toRadians(angle));
         rm.render();
         System.out.format("hdg = %d\n", angle);
         angle++;
      }
      else {
         timer.stop();
         window.dispose();
      }
   }

   public static void main(String[] args) {
      Lab lab = new Lab();

   }
}
