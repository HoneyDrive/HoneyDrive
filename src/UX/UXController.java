package UX;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import trips.DrivingHistory;
import trips.Trip;

public class UXController {

    @FXML private Label totalDistanceDrivenLabel;

    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
    }

    public void updateTotalDistanceDrivenLabel() {
        drivingHistory = new DrivingHistory();
        totalDistanceDrivenLabel.setText(String.valueOf(drivingHistory.getCommutingDistanceThisYear()));
    }
}
