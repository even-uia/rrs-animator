package even.animation.sailing;

import even.animation.Actor;
import even.animation.Renderable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


/**
 *
 * @author evenal
 */
public class Boat implements Actor, Renderable
{
   private static final double NEAR_ENOUGH = 0.0001;
   BoatType type;
   String name;
   Color colour;

   Rig rig;
   KineticsManager kineticsMgr;
   double targetHdg;

   public Boat(BoatType type, String name, Color colour) {
      this.type = type;
      this.name = name;
      this.colour = colour;
      kineticsMgr = new KineticsManager();
   }

   public void setRig(Rig rig) {
      this.rig = rig;
   }

   public void setTargetHdg(double targetHdg) {
      this.targetHdg = targetHdg;
   }

   public void act() {
      double twa = kineticsMgr.hdg.twa;
      PolarDatum twaData = type.getTwaData(twa);
      rig.update(twaData, kineticsMgr.hdg);

      double dt = 1;
      while (dt > 0 + NEAR_ENOUGH);
      dt = kineticsMgr.update(dt, rig, twaData);
   }

   public void render(Graphics2D g2) {
      Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, 1, 1);

      AffineTransform baseTx = g2.getTransform();
      g2.translate(kineticsMgr.pos.x, kineticsMgr.pos.y);
      Shape s1 = g2.getTransform().createTransformedShape(rect);

      g2.rotate(kineticsMgr.hdg.hdg);
      Shape s2 = g2.getTransform().createTransformedShape(rect);

      g2.setColor(colour);
      g2.fill(type.hull);
      g2.setColor(Color.BLACK);
      g2.draw(this.type.hull);

   }
}
