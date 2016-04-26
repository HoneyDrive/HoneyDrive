package UX;

import UX.weather.CurrentWeather;
import UX.weather.CurrentWeatherController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @FXML private Label howAmIDrivingResponsLabel;
    @FXML private Label driverWarningsLabel;

    @FXML private Button isCommutingButton;
    private boolean isCommuting = false;

    @FXML private AnchorPane driverAnchorPane;

    @FXML private TilePane howAmIDrivingTilePane;

    @FXML private ImageView howAmIDrivingImageView;

    // Statistics tab

    @FXML private TilePane driverWarningTilePane;
    @FXML private TilePane driverCommutingTilePane;
    @FXML private ImageView driverWarningImageView;


    Image okHand = new Image(UXController.class.getResourceAsStream("images/okHand.png"));
    Image warningSign = new Image(UXController.class.getResourceAsStream("images/signs.png"));

    @FXML private Label statisticsDistanceDrivenWeek;
    @FXML private Label statisticsDistanceDrivenMonth;
    @FXML private Label statisticsDistanceDrivenYear;
    @FXML private Label statisticsBeesEarnedWeek;
    @FXML private Label statisticsBeesEarnedMonth;
    @FXML private Label statisticsBeesEarnedTotal;
    @FXML private Label statisticsCommutingDistanceWeek;
    @FXML private Label statisticsCommutingDistanceMonth;
    @FXML private Label statisticsCommutingDistanceYear;
    @FXML private Label statisticsAverageKm;
    @FXML private Label statisticsAverageBees;
    @FXML private Label statisticsAverageFuel;


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
        drivingHistory.generateMockData();
        msgLabelPreferencesLooksGood();
        insuranceLimitInput.textProperty().addListener(insuranceLimitInputListener);
        newTrip(new Trip());
        trip.start("TestData/highway-speeding.json", CarActionsFilter.vehicle_speed, CarActionsFilter.fuel_consumed_since_restart,
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
            updateSmiley();
        }, 300);

        startThread(() -> {
            updateWeather();
            updateStatisticsAverages();
            insuranceLimitWarning();
            updateStatisticsDistanceDrivenYear("");
            updateStatisticsDistanceDrivenMonth("");
            updateStatisticsDistanceDrivenWeek("");
            updateStatisticsCommutingDrivenMonth();
            updateStatisticsCommutingDrivenWeek();
            updateStatisticsCommutingDrivenYear();
            updateStatisticsEarnedBeesMonthLabel("" + (drivingHistory.getBeeCount() + 10));
            updateStatisticsEarnedBeesTotalLabel("" + (drivingHistory.getBeeCount() + 20));
            updateStatisticsEarnedBeesWeekLabel("" + drivingHistory.getBeeCount());
        }, 45_000);
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

    public static boolean almostEqual(double a, double b, double eps)
    {
        return Math.abs(a - b) < eps;
    }

    private double lastFuel;

    public void updateFuelUsedLabel()
    {
        if (!almostEqual(lastFuel, trip.getFuelBurntPer10Km(), 0.05))
        {
            driverFuelConsumedLabel.setText(String.format("%.1f", trip.getFuelBurntPer10Km()));
            lastFuel = trip.getFuelBurntPer10Km();
        }
    }

    private double lastDistance;

    public void updateTotalDistanceDrivenLabel()
    {
        if (!almostEqual(lastDistance, trip.getTotalDistance(), 0.05))
        {
            driverDistanceDrivenLabel.setText(String.format("%.1f", trip.getTotalDistance()));
            lastDistance = trip.getTotalDistance();
        }
    }

    private double lastSpeed;

    public void updateSpeedLabel()
    {
        if (lastSpeed != trip.getSpeed())
        {
            driverSpeedLabel.setText(String.format("%.0f", trip.getSpeed()));
            lastSpeed = trip.getSpeed();
        }
    }

    private int lastBeeCount;

    public void updateTripEarnedBeesLabel()
    {
        if (lastBeeCount != trip.getBeeCount())
        {
            driverBeesEarnedLabel.setText("" + trip.getBeeCount());
            lastBeeCount = trip.getBeeCount();
        }
    }

    public void updateWarningsLabel(String warning)
    {
        driverWarningsLabel.setText(warning);
    }

    public void updateStatisticsEarnedBeesWeekLabel(String text)
    {
        statisticsBeesEarnedWeek.setText(text);
    }

    public void updateStatisticsEarnedBeesMonthLabel(String text)
    {
        statisticsBeesEarnedMonth.setText(text);
    }

    public void updateStatisticsEarnedBeesTotalLabel(String text)
    {
        statisticsBeesEarnedTotal.setText(text);
    }

    public void updateStatisticsDistanceDrivenWeek(String text)
    {
        statisticsDistanceDrivenWeek.setText(Long.toString(drivingHistory.getDistanceThisYear()));
    }

    public void updateStatisticsDistanceDrivenMonth(String text)
    {
        statisticsDistanceDrivenMonth.setText(Long.toString(drivingHistory.getDistanceThisYear() + 11));
    }

    public void updateStatisticsDistanceDrivenYear(String text)
    {
        statisticsDistanceDrivenYear.setText(Long.toString(drivingHistory.getDistanceThisYear() + 50));
    }

    public void updateStatisticsCommutingDrivenWeek()
    {
        statisticsCommutingDistanceWeek.setText(Long.toString(drivingHistory.getCommutingDistanceThisYear()));
    }

    public void updateStatisticsCommutingDrivenMonth()
    {
        statisticsCommutingDistanceMonth.setText(Long.toString(drivingHistory.getCommutingDistanceThisYear() + 10));
    }

    public void updateStatisticsCommutingDrivenYear()
    {
        statisticsCommutingDistanceYear.setText(Long.toString(drivingHistory.getCommutingDistanceThisYear() + 20));
    }

    public void updateStatisticsAverages()
    {
        statisticsAverageBees.setText("" + drivingHistory.averageBeePerTrip());
        statisticsAverageFuel.setText(String.format("%.1f", drivingHistory.averageFuelPerTrip()));
        statisticsAverageKm.setText("" + drivingHistory.averageKmPerTrip());

    }

    public void setInsuranceLimit()
    {
        drivingHistory.setInsuranceDistance(Long.parseLong(insuranceLimitInput.getText()));
    }

    public void setInsuranceLimit(Long number)
    {
        drivingHistory.setInsuranceDistance(number);
    }

    private int lastSmileyStatus;

    public void updateSmiley()
    {
        if (lastSmileyStatus != trip.getSmileyStatus())
        {
            lastSmileyStatus = trip.getSmileyStatus();

            if (trip.getSmileyStatus() == -1)
            {
                howAmIDrivingTilePane.setStyle("-fx-background-color: #9BC53D");
                howAmIDrivingResponsLabel.setText("Efficient!");
                howAmIDrivingImageView.setImage(new Image(UXController.class.getResourceAsStream("images/smiley.png")));
            } else if (trip.getSmileyStatus() == 0)
            {
                howAmIDrivingTilePane.setStyle("-fx-background-color: #FDE74C");
                howAmIDrivingResponsLabel.setText("Not so efficient!");
                howAmIDrivingImageView.setImage(new Image(UXController.class.getResourceAsStream("images/medium.png")));
            } else
            {
                howAmIDrivingTilePane.setStyle("-fx-background-color: #E55934");
                howAmIDrivingResponsLabel.setText("Aggressive!");
                howAmIDrivingImageView.setImage(new Image(UXController.class.getResourceAsStream("images/sad.png")));
            }
        }
    }

    public void fuelConsumptionWarning()
    {
        if (this.trip.getFuelBurntPerKm() > drivingHistory.getFuelConsumptionAvg())
        {
            driverFuelConsumedLabel.setTextFill(Color.RED);

            driverWarningsLabel.setText(fuelConsumptionIsAboveAverageWarning);
            driverWarningImageView.setImage(warningSign);
            driverWarningTilePane.setStyle("-fx-background-color:#e55934");
        } else
        {
            driverFuelConsumedLabel.setTextFill(Color.BLACK);

            driverWarningsLabel.setText("No warnings");
            driverWarningImageView.setImage(okHand);
            driverWarningTilePane.setStyle("-fx-background-color: #9bc53d");
        }
    }

    public void insuranceLimitWarning()
    {
        if (drivingHistory.getDistanceThisYear() >= drivingHistory.getInsuranceDistance() && drivingHistory.getInsuranceDistance() != 0L)
        {
            driverDistanceDrivenLabel.setTextFill(Color.RED);

            driverWarningsLabel.setText(totalDistanceIsAboveInsuranceLimitWarning);
            driverWarningImageView.setImage(warningSign);
            driverWarningTilePane.setStyle("-fx-background-color:#e55934");
        } else
        {
            driverDistanceDrivenLabel.setTextFill(Color.BLACK);

            driverWarningsLabel.setText("No warnings");
            driverWarningImageView.setImage(okHand);
            driverWarningTilePane.setStyle("-fx-background-color: #9bc53d");
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
            driverCommutingTilePane.setStyle("-fx-background-color:  #8D99AE");
            isCommutingButton.setText("NO");
            isCommutingButton.setStyle("-fx-background-color:  #8D99AE; -fx-text-fill: white;");
            isCommuting = !isCommuting;
            trip.setCommuting(false);
        } else
        {
            driverCommutingTilePane.setStyle("-fx-background-color:  #2B2D42");
            isCommutingButton.setText("YES");
            isCommutingButton.setStyle("-fx-background-color: #2B2D42; -fx-text-fill: white;");
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
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:  #E55934");
        insuranceLimitInput.setStyle("-fx-text-fill: #E55934");
        preferencesWarningLabel.setText(msg);
    }

    private void msgLabelPreferencesLooksGood()
    {
        preferencesWarningLabel.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #a1b56c");
        insuranceLimitInput.setStyle("-fx-text-fill: black");
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
