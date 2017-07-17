package even.animation.sailing;

import even.animation.Pos;


/**
 *
 * @author evenal
 */
public class KineticsManager
{
   public static final double ACCEL_COEFF = 4;
   public static final double TOLERANCE = 0.01;

   Pos pos;
   Course hdg;
   double currentSpd;
   double targetSpd;
   double targetHdg;
   double turnRate;

   public KineticsManager() {
      pos = new Pos(100, 100);
      hdg = new Course(0);
   }

   public double update(double dt,
                        Rig rig,
                        PolarDatum twaData) {

      targetSpd = rig.slack * twaData.slpSpd +
              (1 - rig.slack) * twaData.optSpd;
      double a = (targetSpd - currentSpd) / ACCEL_COEFF;
      if (rig.state == Rig.State.Filling) a = 0.5 * a;
      if (rig.state == Rig.State.Flapping) a = -currentSpd;

      double newSpd = currentSpd + a * dt;
      double avgSpd = (currentSpd + newSpd) / 2;
      double dist = avgSpd * dt;

      if (hdg.hdg == targetHdg) {
         advance(dist, hdg.hdg);
      }
      else {
         double turnAngle = targetHdg - hdg.hdg;
         double turnT = turnAngle / turnRate;
         if (turnT < dt) {
            turn(turnT, a);
            return dt - turnT;
         }
         else {
            turn(dt, a);
         }
      }
      return dt;
   }

   void turn(double dt, double a) {
      double turnAngle = dt * turnRate;
      double halfAngle = turnAngle / 2;
      double dist = (currentSpd + a * dt) * dt;
      double turnRadius = dist / turnAngle;
      double realDist = Math.sin(halfAngle) * turnRadius * 2;
      hdg.setHdg(hdg.hdg + halfAngle);
      advance(realDist, hdg.hdg);
      hdg.setHdg(hdg.hdg + halfAngle);
   }

   private void advance(double dist, double hdg) {
      double dx = dist * Math.sin(hdg);
      double dy = dist * Math.cos(hdg);
      pos.x += dx;
      pos.y += dy;
   }

   public void setHdg(double hdg) {
      this.hdg.hdg = hdg;
      this.hdg.twa = Math.abs(hdg);
      this.hdg.tack = hdg > 0 ? Course.Tack.Port
              : Course.Tack.Starboard;
   }
}
