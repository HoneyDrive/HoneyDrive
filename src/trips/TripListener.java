package trips;

import metrics.CarAction;

/**
 * Created by hakon on 17/03/16.
 */
public interface TripListener {

    void newCarAction(CarAction carAction);
}
