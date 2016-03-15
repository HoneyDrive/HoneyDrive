package UX;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

    @FXML private AnchorPane driverTab;
    @FXML private AnchorPane commuterTab;
    @FXML private AnchorPane statisticsTab;
    @FXML private AnchorPane preferencesTab;

    @FXML private Button nightModeButton;
    private boolean switchedOn = false;

    private final String totalDistanceIsAboveInsuranceLimitWarning = "Distance this year \nis above insurance \ndistance!";
    private final String fuelConsumptionIsAboveAverageWarning = "Fuel consumption \nis above average!";
    private final String numberValidationWarning = "Please only add numbers, you know that ;)";
    private final String validInsuranceLimitNumber = "Please enter a valid insurance limit between 0 and 500.000";

    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize() {
        drivingHistory = new DrivingHistory();
        msgLabelPreferencesLooksGood();
        insuranceLimitInput.textProperty().addListener(insuranceLimitInputListener);
    }

    public void newTrip(Trip trip){
        this.trip = trip;
        //TODO: Set all labels for current trips = 0
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

    public void disableDriverTabIfCommuting() {
        if (this.trip.getIsCommuting()) {
            driverTab.setDisable(true);
        }
    }

    public void fuelConsumptionWarning() {
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg()) {
            fuelConsumedLabel.setTextFill(Color.RED);
            warningsTextArea.setText(fuelConsumptionIsAboveAverageWarning);
        }
    }

    public void insuranceLimitWarning() {
        if (drivingHistory.getDistanceThisYear() > drivingHistory.getInsuranceDistance() && drivingHistory.getInsuranceDistance() != 0L) {
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
            writeWarning(validInsuranceLimitNumber);
        } else {
            setInsuranceLimit();
            msgLabelPreferencesLooksGood();
        }
    });

    @FXML
    public void nightModeButtonClicked(ActionEvent event) {
        if (switchedOn) {
            nightModeButton.setText("OFF");
            nightModeButton.setStyle("-fx-background-color: grey;-fx-text-fill:black;");
            switchedOn = !switchedOn;
            toggleNightModeOff();
        } else {
            nightModeButton.setText("ON");
            nightModeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            switchedOn = !switchedOn;
            toggleNightModeOn();

        }
    }

    // ---------------------------------------------Help Methods---------------------------------------------

    private void toggleNightModeOn() {
        driverTab.setStyle("-fx-background-color: grey");
        commuterTab.setStyle("-fx-background-color: grey");
        statisticsTab.setStyle("-fx-background-color: grey");
        preferencesTab.setStyle("-fx-background-color: grey");

    }

    private void toggleNightModeOff() {
        driverTab.setStyle("-fx-background-color: #F8F5F3");
        commuterTab.setStyle("-fx-background-color: #F8F5F3");
        statisticsTab.setStyle("-fx-background-color: #F8F5F3");
        preferencesTab.setStyle("-fx-background-color: #F8F5F3");
    }

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
