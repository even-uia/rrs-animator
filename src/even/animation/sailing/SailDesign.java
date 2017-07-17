package even.animation.sailing;

import java.awt.Shape;


/**
 *
 * @author evenal
 */
public class SailDesign
{
   double chord, camber;
   double minAngle;
   double maxAngle;
   double flapAngle;
   double pivot;

   Shape[][] shapes;
   Spar spar;

   public SailDesign(double chord, double camber,
                     double minAngle, double flapAngle,
                     Spar spar) {
      this.chord = chord;
      this.camber = camber;
      this.minAngle = minAngle;
      this.flapAngle = flapAngle;
   }

   Shape getShape(Course.Tack tack, Rig.State state) {
      return shapes[tack.ordinal()][state.ordinal()];
   }
}
