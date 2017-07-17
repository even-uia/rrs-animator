package even.animation.sailing;


/**
 *
 * @author evenal
 */
public class Rig
{
    RigDesign design;
    Sail main;
    Sail jib;
    Sail spin;
    double slack;
    State state;

    public void update(PolarDatum twaData, Course hdg) {
        double angle = twaData.optAngle + slack * twaData.angRng;
        double aoa = hdg.twa - angle;
        if (aoa > design.aoaMin) state = State.Full;
        else if (aoa > design.aoaFlapping) state = State.Filling;
        else state = State.Flapping;

        main.update(angle, state, hdg.tack);
        if (jib != null) jib.update(angle, state, hdg.tack);
        if (spin != null) spin.update(angle, state, hdg.tack);
    }


    public static enum State
    {
        Full, Filling, Flapping;
    }

}
