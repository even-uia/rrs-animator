package even.animation.sailing;


/**
 *
 * @author evenal
 */
public class PolarDatum
{
    double twa;
    double optAngle; // optimal sail trim angle
    double optSpd; // speed when trim optimal
    double optA;  // accelertion when trim optimal

    double slpAngle;
    double slpSpd;
    double slpA;

    double angRng;
    double spdRange;
    double aRng;

    double spinSpd;

    public PolarDatum(double twa,
                      double optAngle, double optSpd, double optA,
                      double slpAngle, double slpSpd, double slpA,
                      double spinSpd) {
        this.twa = twa;
        this.optAngle = optAngle;
        this.optSpd = optSpd;
        this.optA = optA;
        this.slpAngle = slpAngle;
        this.slpSpd = slpSpd;
        this.slpA = slpA;
        this.spinSpd = spinSpd;
        this.angRng = slpAngle - optAngle;
        this.spdRange = slpSpd - optSpd;
        this.aRng = slpA - optA;
    }

    public void interpolate(double twa, PolarDatum pd0,
                            PolarDatum pd1) {
        double twa0 = pd0.twa;
        double twa1 = pd1.twa;

        double m1 = (twa - twa0) / (twa1 - twa0);
        double m0 = 1 - m1;

        this.twa = twa;
        this.optAngle = m0 * pd0.optAngle + m1 * pd1.optAngle;
        this.optSpd = m0 * pd0.optSpd + m1 * pd1.optSpd;
        this.optA = m0 * pd0.optA + m1 * pd1.optA;
        this.slpAngle = m0 * pd0.slpAngle + m1 * pd1.slpAngle;
        this.slpSpd = m0 * pd0.slpSpd + m1 * pd1.slpSpd;
        this.slpA = m0 * pd0.slpA + m1 * pd1.slpA;
        this.spinSpd = m0 * pd0.spinSpd + m1 * pd1.spinSpd;
    }

    public void interpolate(double twa, PolarDatum pd) {
        this.twa = twa;
        this.optAngle = pd.optAngle;
        this.optSpd = pd.optSpd;
        this.optA = pd.optA;
        this.slpAngle = pd.slpAngle;
        this.slpSpd = pd.slpSpd;
        this.slpA = pd.slpA;
        this.spinSpd = pd.spinSpd;
    }
}
