package UX;

import UX.weather.CurrentWeather;
import UX.weather.CurrentWeatherController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import metrics.CarActionsFilter;
import org.json.JSONException;
import trips.DrivingHistory;
import trips.Trip;

import java.io.IOException;

public class UXController
{
    @FXML private Label driverDistanceDrivenLabel;
    @FXML private Label driverFuelConsumedLabel;
    @FXML private Label driverSpeedLabel;
    @FXML private Label preferencesWarningLabel;
    @FXML private Label weeklyEarnedBeesLabel;
    @FXML private Label driverEarnedBeesLabel;
    @FXML private TextField insuranceLimitInput;
    @FXML private TextArea driverWarningsTextArea;

    @FXML private AnchorPane driverAnchorPane;
    @FXML private AnchorPane commuterAnchorPane;
    @FXML private AnchorPane statisticsAnchorPane;
    @FXML private AnchorPane preferencesAnchorPane;

    @FXML private Tab driverTab;
    @FXML private Tab commuterTab;
    @FXML private TabPane tabPane;

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

        newTrip(new Trip(this));
        trip.start("src/metrics/TestData/data3.json", CarActionsFilter.vehicle_speed, CarActionsFilter.fuel_consumed_since_restart,
                CarActionsFilter.odometer);
        updateWeather();
    }

    public void newTrip(Trip trip) {
        this.trip = trip;
        //TODO: Set all labels for current trips = 0
    }

    // ---------------------------------------------Update Methods---------------------------------------------

    public void updateFuelUsedLabel() {
        driverFuelConsumedLabel.setText(String.format("%.1f", trip.getFuelBurntPer10Km()));
    }

    public void updateSpeedLabel() {
        driverSpeedLabel.setText(String.format("%.1f", trip.getSpeed()));
    }

    public void updateTotalDistanceDrivenLabel() {
        driverDistanceDrivenLabel.setText(String.format("%.1f", drivingHistory.getDistanceThisYear() + trip.getTotalDistance()));
    }

    public void updateWarningsLabel(String warning) {
        driverWarningsTextArea.setText(warning);
    }

    public void updateTripEarnedBeesLabel() {
//        driverEarnedBeesLabel.setText(); //TODO: Legg til bier i TripLabel
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
            driverAnchorPane.setDisable(true);
        }
    }

    public void fuelConsumptionWarning() {
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg()) {
            driverFuelConsumedLabel.setTextFill(Color.RED);
            driverWarningsTextArea.setText(fuelConsumptionIsAboveAverageWarning);
        }
    }

    public void insuranceLimitWarning() {
        if (drivingHistory.getDistanceThisYear() > drivingHistory.getInsuranceDistance() && drivingHistory.getInsuranceDistance() != 0L) {
            driverDistanceDrivenLabel.setTextFill(Color.RED);
            driverWarningsTextArea.setText(totalDistanceIsAboveInsuranceLimitWarning);
        }
    }

    // ---------------------------------------------Listeners---------------------------------------------

    private ChangeListener<? super String> insuranceLimitInputListener = ((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d+") && !newValue.equals("")) {
            writeDriverWarning(numberValidationWarning);
        } else if (newValue.equals("")) {
            msgLabelPreferencesLooksGood();
            setInsuranceLimit(0L);
        } else if (!isInsuranceLimitValid(insuranceLimitInput.getText())) {
            writeDriverWarning(validInsuranceLimitNumber);
        } else {
            setInsuranceLimit();
            msgLabelPreferencesLooksGood();
        }
    });

    @FXML public void nightModeButtonClicked(ActionEvent event) {
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
        driverAnchorPane.setStyle("-fx-background-color: grey");
        commuterAnchorPane.setStyle("-fx-background-color: grey");
        statisticsAnchorPane.setStyle("-fx-background-color: grey");
        preferencesAnchorPane.setStyle("-fx-background-color: grey");
    }

    private void toggleNightModeOff() {
        driverAnchorPane.setStyle("-fx-background-color: #F8F5F3");
        commuterAnchorPane.setStyle("-fx-background-color: #F8F5F3");
        statisticsAnchorPane.setStyle("-fx-background-color: #F8F5F3");
        preferencesAnchorPane.setStyle("-fx-background-color: #F8F5F3");
    }

    private boolean isInsuranceLimitValid(String limit) {
        int limitNumber = Integer.parseInt(limit);
        return !(limitNumber < 0 || limitNumber > 500000);
    }


    private void writeDriverWarning(String msg) {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:  #ab4642");
        preferencesWarningLabel.setText(msg);
    }

    private void msgLabelPreferencesLooksGood() {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #a1b56c");
        preferencesWarningLabel.setText("Preferences looks good.");
    }

    // ---------------------------------------------Weather---------------------------------------------

    private CurrentWeatherController currentWeatherController = new CurrentWeatherController();
    private CurrentWeather ntnuTrondheim;

    @FXML private Label weatherTimeLabel;
    @FXML private Label weatherTemperatureLabel;
    @FXML private Label weatherHumidityLabel;
    @FXML private Label weatherPrecipChanceLabel;
    @FXML private Label weatherSummaryLabel;
    @FXML private Label weatherLocationLabel;
    @FXML private ImageView weatherIconImageView;

    private void updateWeather() {
        try {
            ntnuTrondheim = currentWeatherController.getCurrentDetails();

            weatherTimeLabel.setText(ntnuTrondheim.getFormattedTime());
            weatherTemperatureLabel.setText(String.format("%.1f", ntnuTrondheim.getTemperature()) + " ℃");
            weatherHumidityLabel.setText(String.format("%.0f", ntnuTrondheim.getHumidity() * 100) + " %");
            weatherPrecipChanceLabel.setText(String.format("%.0f", ntnuTrondheim.getPrecipChance()) + " %");
            weatherSummaryLabel.setText(ntnuTrondheim.getSummary());
            weatherIconImageView.setFitHeight(30);
            weatherIconImageView.setFitWidth(30);
            weatherIconImageView.setImage(ntnuTrondheim.getIconId());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLocationAndTimeAlignment() {
        int timeLength = ntnuTrondheim.getFormattedTime().length();
        if (timeLength == 8) {
            weatherLocationLabel.setLayoutX(675);
            weatherLocationLabel.setLayoutY(14);
            weatherTimeLabel.setLayoutX(655);
            weatherTimeLabel.setLayoutY(31);
        } else {
            weatherLocationLabel.setLayoutX(675);
            weatherLocationLabel.setLayoutY(14);
            weatherTimeLabel.setLayoutX(665);
            weatherTimeLabel.setLayoutY(31);
        }
    }
}
