package even.animation.sailing;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author even
 */
public class PolarData
{
    List<PolarDatum> data;
    PolarDatum buffer;

    public PolarData() {
        data = new ArrayList<>();
    }

    public PolarDatum lookup(double twa) {
        int i = 0;
        while (i < data.size() && data.get(i).twa < twa)
            i++;

        if (i == 0) buffer.interpolate(twa, data.get(0));
        else if (i < data.size()) {
            PolarDatum dat0 = data.get(i - 1);
            PolarDatum dat1 = data.get(i);
            buffer.interpolate(twa, dat0, dat1);
        }
        else buffer.interpolate(twa, data.get(data.size() - 1));
        return buffer;
    }

    public void addDatum(PolarDatum d) {
        data.add(d);
    }
}
