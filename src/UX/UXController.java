package UX;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import trips.DrivingHistory;
import trips.Trip;


public class UXController {

    @FXML private Label totalDistanceDrivenLabel;
    @FXML private Label fuelConsumedLabel;
    @FXML private Label speedLabel;
    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
        

    }

    public void newTrip(Trip trip){
        this.trip=trip;
        //TODO: Set all labels for current trips=0
    }
    public void updateFuelUsedLabel(){
        fuelConsumedLabel.setText(String.valueOf(trip.getFuelBurntPerKm()));
    }
    public void updateSpeedLabel(){
        speedLabel.setText(String.valueOf(trip.getSpeed()));
    }

    public void updateTotalDistanceDrivenLabel() {
        drivingHistory = new DrivingHistory();
        totalDistanceDrivenLabel.setText(String.valueOf(drivingHistory.getCommutingDistanceThisYear()));
    }
}
