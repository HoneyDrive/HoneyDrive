package UX;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import trips.DrivingHistory;
import trips.Trip;


public class UXController {

    @FXML private Label totalDistanceDrivenLabel;
    @FXML private Label fuelConsumedLabel;
    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
        

    }

    public void newTrip(Trip trip){
        this.trip=trip;
        //TODO: Set all labels for current trips=0
    }
    public void upDateFuelUsedLabel(){
        fuelConsumedLabel.setText(String.valueOf(trip.getFuelUsed()));
    }

    public void updateTotalDistanceDrivenLabel() {
        drivingHistory = new DrivingHistory();
        totalDistanceDrivenLabel.setText(String.valueOf(drivingHistory.getCommutingDistanceThisYear()));
    }
}
