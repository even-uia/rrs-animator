package even.animation.sailing;

import java.awt.Shape;


/**
 *
 * @author evenal
 */
public class BoatType
{
    String id;
    Shape hull;
    RigDesign rig;
    PolarData polar;

    PolarDatum twaData;

    public PolarDatum getTwaData(double twa) {
        twaData = polar.lookup(twa);
        return twaData;
    }
}
