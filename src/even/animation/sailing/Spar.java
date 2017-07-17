package even.animation.sailing;

import java.awt.Shape;


/**
 *
 * @author evenal
 */
public class Spar
{
   SparType type;
   double length;
   double radius;
   Shape shape;

   public Spar(double length, double radius, SparType type) {
      this.type = type;
      this.length = length;
      this.radius = radius;
   }


   public enum SparType
   {
      BOOM, POLE, SPRIT;
   }
}
