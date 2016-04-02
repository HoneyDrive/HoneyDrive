package UX;

import UX.weather.CurrentWeather;
import UX.weather.CurrentWeatherController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import metrics.CarActionsFilter;
import org.json.JSONException;
import trips.DrivingHistory;
import trips.Trip;

import java.io.IOException;

public class UXController
{

    // Driver tab

    @FXML private Label driverDistanceDrivenLabel;
    @FXML private Label driverFuelConsumedLabel;
    @FXML private Label driverSpeedLabel;
    @FXML private Label driverBeesEarnedLabel;

    @FXML private Label driverWarningsLabel;

    @FXML private Button isCommutingButton;
    private boolean isCommuting = false;

    @FXML private AnchorPane driverAnchorPane;

    // Statistics tab

    @FXML private TilePane driverWarningTilePane;
    @FXML private ImageView driverWarningImageView;


    Image okHand = new Image(UXController.class.getResourceAsStream("images/okHand.png"));
    Image warningSign = new Image(UXController.class.getResourceAsStream("images/signs.png"));

    @FXML private Label statisticsDistanceDrivenWeek;
    @FXML private Label statisticsDistanceDrivenMonth;
    @FXML private Label statisticsDistanceDrivenYear;
    @FXML private Label statisticsBeesEarnedWeek;
    @FXML private Label statisticsBeesEarnedMonth;
    @FXML private Label statisticsBeesEarnedTotal;

    @FXML private AnchorPane statisticsAnchorPane;

    // Preferences tab

    @FXML private Label preferencesWarningLabel;

    @FXML private TextField insuranceLimitInput;

    @FXML private AnchorPane preferencesAnchorPane;

    @FXML private Button nightModeButton;
    private boolean switchedOn = false;

    // Warnings

    private final String totalDistanceIsAboveInsuranceLimitWarning = "Distance this year \nis above insurance \ndistance!";
    private final String fuelConsumptionIsAboveAverageWarning = "Fuel consumption \nis above average!";
    private final String numberValidationWarning = "Please only add numbers, you know that ;)";
    private final String validInsuranceLimitNumber = "Please enter a valid insurance limit between 0 and 500.000";

    private Trip trip;
    private DrivingHistory drivingHistory;

    public void initialize()
    {
        drivingHistory = new DrivingHistory();
        msgLabelPreferencesLooksGood();
        insuranceLimitInput.textProperty().addListener(insuranceLimitInputListener);
        newTrip(new Trip());
        trip.start("src/metrics/TestData/data3.json", CarActionsFilter.vehicle_speed, CarActionsFilter.fuel_consumed_since_restart,
                CarActionsFilter.odometer);
        drivingHistory.addTrip(this.trip);
        startUIUpdater();
    }

    public Trip getTrip()
    {
        return this.trip;
    }

    public DrivingHistory getDrivingHistory()
    {
        return this.drivingHistory;
    }

    private void startUIUpdater()
    {
        startThread(() -> {
            updateFuelUsedLabel();
            updateSpeedLabel();
            updateTotalDistanceDrivenLabel();
            updateTripEarnedBeesLabel();
            //updateWeeklyEarnedBeesLabel();
            insuranceLimitWarning();
            updateStatisticsDistanceDrivenYear("");
            updateStatisticsDistanceDrivenMonth("");
            updateStatisticsDistanceDrivenWeek("");
        }, 300);

        startThread(() -> updateWeather(), 1000 * 30);
    }

    private void startThread(Runnable runnable, long millis)
    {
        Thread thread = new Thread(() -> {
            while (true)
            {
                Platform.runLater(runnable);
                try
                {
                    Thread.sleep(millis);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void newTrip(Trip trip)
    {
        this.trip = trip;
        //TODO: Set all labels for current trips = 0
    }

    // ---------------------------------------------Update Methods---------------------------------------------

    public void updateFuelUsedLabel()
    {
        driverFuelConsumedLabel.setText(String.format("%.1f", trip.getFuelBurntPer10Km()));
    }

    public void updateSpeedLabel()
    {
        driverSpeedLabel.setText(String.format("%.1f", trip.getSpeed()));
    }

    public void updateTotalDistanceDrivenLabel()
    {
        driverDistanceDrivenLabel.setText(String.format("%.1f", trip.getTotalDistance()));
    }

    public void updateWarningsLabel(String warning)
    {
        driverWarningsLabel.setText(warning);
    }

    public void updateTripEarnedBeesLabel()
    {
        driverBeesEarnedLabel.setText("TEST"); //TODO: Legg til bier i TripLabel
    }

    public void updateStatisticsEarnedBeesWeekLabel(String text)
    {
        statisticsBeesEarnedWeek.setText(text);
    }

    public void updateStatisticsEarnedBeesMonthLabel(String text)
    {
        statisticsBeesEarnedWeek.setText(text);
    }

    public void updateStatisticsEarnedBeesTotalLabel(String text)
    {
        statisticsBeesEarnedWeek.setText(text);
    }

    public void updateStatisticsDistanceDrivenWeek (String text)
    {
        statisticsDistanceDrivenWeek.setText(Long.toString(drivingHistory.getDistanceThisYear()));
    }

    public void updateStatisticsDistanceDrivenMonth (String text)
    {
        statisticsDistanceDrivenMonth.setText(Long.toString(drivingHistory.getDistanceThisYear()));
    }

    public void updateStatisticsDistanceDrivenYear (String text)
    {
        statisticsDistanceDrivenYear.setText(Long.toString(drivingHistory.getDistanceThisYear()));
    }

    public void setInsuranceLimit()
    {
        drivingHistory.setInsuranceDistance(Long.parseLong(insuranceLimitInput.getText()));
    }

    public void setInsuranceLimit(Long number)
    {
        drivingHistory.setInsuranceDistance(number);
    }

    public void disableDriverTabIfCommuting()
    {
        if (this.trip.getIsCommuting())
        {
            driverAnchorPane.setDisable(true);
        }
    }

    public void fuelConsumptionWarning()
    {
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg())
        {
            driverFuelConsumedLabel.setTextFill(Color.RED);
            driverWarningsLabel.setText(fuelConsumptionIsAboveAverageWarning);
            driverWarningImageView.setImage(okHand);
            driverWarningsLabel.setText(fuelConsumptionIsAboveAverageWarning);
            driverWarningImageView.setImage(warningSign);
            driverWarningTilePane.setStyle("-fx-background-color:#e55934");
        }
    }

    public void insuranceLimitWarning()
    {
        if (drivingHistory.getDistanceThisYear() >= drivingHistory.getInsuranceDistance() && drivingHistory.getInsuranceDistance() != 0L)
        {
            driverDistanceDrivenLabel.setTextFill(Color.RED);
            driverWarningsLabel.setText(totalDistanceIsAboveInsuranceLimitWarning);
        }
        else
        {
            driverDistanceDrivenLabel.setTextFill(Color.BLACK);
            driverWarningTilePane.setStyle("-fx-background-color: #9bc53d");
            driverWarningImageView.setImage(okHand);
        }
    }

    // ---------------------------------------------Listeners---------------------------------------------

    private ChangeListener<? super String> insuranceLimitInputListener = ((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d+") && !newValue.equals(""))
        {
            writeDriverWarning(numberValidationWarning);
        } else if (newValue.equals(""))
        {
            msgLabelPreferencesLooksGood();
            setInsuranceLimit(0L);
        } else if (!isInsuranceLimitValid(insuranceLimitInput.getText()))
        {
            writeDriverWarning(validInsuranceLimitNumber);
        } else
        {
            setInsuranceLimit();
            msgLabelPreferencesLooksGood();
        }
    });

    @FXML
    public void nightModeButtonClicked(ActionEvent event)
    {
        if (switchedOn)
        {
            nightModeButton.setText("OFF");
            nightModeButton.setStyle("-fx-background-color: grey;-fx-text-fill:black;");
            switchedOn = !switchedOn;
            toggleNightModeOff();
        } else
        {
            nightModeButton.setText("ON");
            nightModeButton.setStyle("-fx-background-color: green;-fx-text-fill:white;");
            switchedOn = !switchedOn;
            toggleNightModeOn();
        }
    }

    @FXML
    public void isCommutingButtonClicked(ActionEvent event)
    {
        if (isCommuting)
        {
            isCommutingButton.setText("NO");
            isCommutingButton.setStyle("-fx-background-color: #2B2D42; -fx-text-fill: white;");
            isCommuting = !isCommuting;
            trip.setCommuting(false);
        } else
        {
            isCommutingButton.setText("YES");
            isCommutingButton.setStyle("-fx-background-color: #8D99AE; -fx-text-fill: white;");
            isCommuting = !isCommuting;
            trip.setCommuting(true);
        }
    }

    // ---------------------------------------------Help Methods---------------------------------------------

    private void toggleNightModeOn()
    {
        driverAnchorPane.setStyle("-fx-background-color: grey");
        statisticsAnchorPane.setStyle("-fx-background-color: grey");
        preferencesAnchorPane.setStyle("-fx-background-color: grey");
    }

    private void toggleNightModeOff()
    {
        driverAnchorPane.setStyle("-fx-background-color: #F8F5F3");
        statisticsAnchorPane.setStyle("-fx-background-color: #F8F5F3");
        preferencesAnchorPane.setStyle("-fx-background-color: #F8F5F3");
    }

    private boolean isInsuranceLimitValid(String limit)
    {
        int limitNumber = Integer.parseInt(limit);
        return !(limitNumber < 0 || limitNumber > 500000);
    }


    private void writeDriverWarning(String msg)
    {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:  #ab4642");
        preferencesWarningLabel.setText(msg);
    }

    private void msgLabelPreferencesLooksGood()
    {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #a1b56c");
        preferencesWarningLabel.setText("Preferences looks good.");
    }

    // ---------------------------------------------Weather---------------------------------------------

    private CurrentWeatherController currentWeatherController = new CurrentWeatherController();

    // Driver tab

    @FXML private Label driverWeatherTimeLabel;
    @FXML private Label driverWeatherTemperatureLabel;
    @FXML private Label driverWeatherSummaryLabel;
    @FXML private Label driverWeatherHumidityLabel;
    @FXML private Label driverWeatherPrecipChanceLabel;
    @FXML private ImageView driverWeatherIconImageView;

    private void updateWeather()
    {
        try
        {
            CurrentWeather ntnuTrondheim = currentWeatherController.getCurrentDetails();

            driverWeatherTimeLabel.setText(ntnuTrondheim.getFormattedTime());
            driverWeatherTemperatureLabel.setText(String.format("%.1f", ntnuTrondheim.getTemperature()) + " ℃");
            driverWeatherHumidityLabel.setText(String.format("%.0f", ntnuTrondheim.getHumidity() * 100) + " %");
            driverWeatherPrecipChanceLabel.setText(String.format("%.0f", ntnuTrondheim.getPrecipChance()) + " %");
            driverWeatherSummaryLabel.setText(ntnuTrondheim.getSummary());
            driverWeatherIconImageView.setFitHeight(30);
            driverWeatherIconImageView.setFitWidth(30);
            driverWeatherIconImageView.setImage(ntnuTrondheim.getIconId());
        } catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
    }
}
