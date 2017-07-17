package even.animation.sailing;


/**
 *
 * @author evenal
 */
public class Course
{
    double hdg; // -PI to PI
    double twa; // 0 to PI
    Tack tack;

    public Course(double hdg) {
        setHdg(hdg);
    }

    public void setHdg(double hdg) {
        while (hdg > Math.PI) hdg = hdg - 2 * Math.PI;
        this.hdg = hdg;
        twa = Math.abs(hdg);
        tack = hdg < 0 ? Tack.Starboard : Tack.Port;
    }


    public static enum Tack
    {
        Port, Starboard;
    }
}
