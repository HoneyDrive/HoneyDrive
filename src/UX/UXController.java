package UX;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import trips.DrivingHistory;
import trips.Trip;

public class UXController {

    @FXML private Label totalDistanceDrivenLabel;
    @FXML private Label fuelConsumedLabel;
    @FXML private Label speedLabel;
    @FXML private Label preferencesWarningLabel;
    @FXML private Label weeklyEarnedBeesLabel;
    @FXML private Label tripEarnedBeesLabel;
    @FXML private TextField insuranceLimitInput;
    @FXML private TextArea warningsTextArea;

    private final String totalDistanceIsAboveInsuranceLimitWarning = "Distance this year \nis above insurance \ndistance!";
    private final String fuelConsumptionIsAboveAverageWarning = "Fuel consumption \nis above average!";
    private final String numberValidationWarning = "Please only add numbers, you know that ;)";
    private final String validInsuraceLimitNumber = "Please enter a valid insurance limit between 0 and 500.000";

    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
        drivingHistory = new DrivingHistory();
        msgLabelPreferencesLooksGood();
        insuranceLimitInput.textProperty().addListener(insuranceLimitInputListener);
        warningsTextArea.setText(totalDistanceIsAboveInsuranceLimitWarning);
    }

    public void newTrip(Trip trip){
        this.trip = trip;
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
        warningsTextArea.setText(warning);
    }

    public void updateTotalDistanceDrivenLabel() {
        totalDistanceDrivenLabel.setText(String.valueOf(drivingHistory.getCommutingDistanceThisYear()));
    }

    public void updateTripEarnedBeesLabel() {
//        tripEarnedBeesLabel.setText(); //TODO: Legg til bier i TripLabel
    }

    public void updateWeeklyEarnedBeesLabel() {
//        weeklyEarnedBeesLabel.setText(); //TODO: Legg til bier i WeeklyTripLabel
    }

    public void setInsuranceLimit() {
        drivingHistory.setInsuranceDistance(Long.parseLong(insuranceLimitInput.getText()));
    }

    public void setInsuranceLimit(Long number) {
        drivingHistory.setInsuranceDistance(number);
    }

    public void fuelConsumptionWarning() {
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg()) {
            fuelConsumedLabel.setTextFill(Color.RED);
            warningsTextArea.setText(fuelConsumptionIsAboveAverageWarning);
        }
    }

    public void insuranceLimitWarning() {
        if (drivingHistory.getDistanceThisYear() > drivingHistory.getInsuranceDistance()) {
            totalDistanceDrivenLabel.setTextFill(Color.RED);
            warningsTextArea.setText(totalDistanceIsAboveInsuranceLimitWarning);
        }
    }

    // ---------------------------------------------Listeners---------------------------------------------

    private ChangeListener<? super String> insuranceLimitInputListener = ((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d+") && !newValue.equals("")) {
            writeWarning(numberValidationWarning);
        } else if (newValue.equals("")) {
            msgLabelPreferencesLooksGood();
            setInsuranceLimit(0L);
        } else if (!isInsuranceLimitValid(insuranceLimitInput.getText())) {
            writeWarning(validInsuraceLimitNumber);
        } else {
            setInsuranceLimit();
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
