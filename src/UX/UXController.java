package UX;

import com.sun.corba.se.spi.logging.CORBALogDomains;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import trips.DrivingHistory;
import trips.Trip;

public class UXController {

    @FXML private Label totalDistanceDrivenLabel;
    @FXML private Label fuelConsumedLabel;
    @FXML private Label speedLabel;
    @FXML private Label warningsLabel;

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

    public void updateWarningsLabel(String warning) {
        warningsLabel.setText(warning);
    }

    public void updateTotalDistanceDrivenLabel() {
        drivingHistory = new DrivingHistory();
        totalDistanceDrivenLabel.setText(String.valueOf(drivingHistory.getCommutingDistanceThisYear()));
    }

    public void fuelConsumptionWarning(){
        drivingHistory = new DrivingHistory();
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg()) {
            fuelConsumedLabel.setTextFill(Color.color(217, 4, 41));
            warningsLabel.setText("Fuel consumption is above average!");
        }
    }
}
