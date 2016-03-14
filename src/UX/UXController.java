package UX;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import trips.DrivingHistory;
import trips.Trip;

public class UXController {

    @FXML private Label totalDistanceDrivenLabel;
    @FXML private Label fuelConsumedLabel;
    @FXML private Label speedLabel;
    @FXML private Label warningsLabel;
    @FXML private Label preferencesWarningLabel;
    @FXML private TextField insuranceLimitInput;

    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
        msgLabelPreferencesLooksGood();
        insuranceLimitInput.textProperty().addListener(insuranceLimitInputListener);
    }

    public void newTrip(Trip trip){
        this.trip=trip;
        //TODO: Set all labels for current trips=0
    }

    // ---------------------------------------------Update Methods---------------------------------------------

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

    public void setInsuranceLimit() {
        drivingHistory.setInsuranceDistance(Long.parseLong(insuranceLimitInput.getText()));
    }

    public void fuelConsumptionWarning(){
        drivingHistory = new DrivingHistory();
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg()) {
            fuelConsumedLabel.setTextFill(Color.color(217, 4, 41));
            warningsLabel.setText("Fuel consumption is above average!");
        }
    }

    // ---------------------------------------------Listeners---------------------------------------------

    private ChangeListener<? super String> insuranceLimitInputListener = ((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d+") && !newValue.equals("")) {
            writeWarning("Please only add numbers, you know that ;)");
        } else if (newValue.equals("")) {
            msgLabelPreferencesLooksGood();
        } else if (!isInsuranceLimitValid(insuranceLimitInput.getText())) {
            writeWarning("Please enter a valid insurance limit between 0 and 500000");
        } else {
            msgLabelPreferencesLooksGood();
        }
    });

    // ---------------------------------------------Help Methods---------------------------------------------

    private boolean isInsuranceLimitValid(String limit) {
        int limitNumber = Integer.parseInt(limit);
        return !(limitNumber < 0 || limitNumber > 500000);
    }

    private void writeWarning(String msg) {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:  #ab4642");
        preferencesWarningLabel.setText(msg);
    }

    private void msgLabelPreferencesLooksGood() {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #a1b56c");
        preferencesWarningLabel.setText("Preferences looks good.");
    }
}
