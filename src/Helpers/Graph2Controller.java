package Helpers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import metrics.CarAction;
import metrics.CarActionsFilter;
import metrics.IActionListener;

import java.net.URL;
import java.util.*;

public class Graph2Controller extends Controller implements Initializable, IActionListener {




    public void swap(){
        changeToScene("graph.fxml");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        CarAction.addCreatedListener(this, CarActionsFilter.vehicle_speed);
        speedChart.getXAxis().setLabel("Seconds");
        speedChart.getYAxis().setLabel("Speed");
        speedChart.getData().add(speedSeries);
        scoreChart.getData().add(scoreSeries);
        scoreChart.setCreateSymbols(false);
        speedChart.setCreateSymbols(false);
    }

    @Override
    public void updateSeries(){
        scoreChart.getData().clear();
        speedChart.getData().clear();
        speedChart.getData().add(speedSeries);
        scoreChart.getData().add(scoreSeries);

    }



    @FXML
    LineChart speedChart;
    @FXML
    LineChart scoreChart;

    @FXML NumberAxis scoreXAxis;
    @FXML NumberAxis scoreYAxis;
    @FXML NumberAxis speedXAxis;
    @FXML NumberAxis speedYAxis;





}
