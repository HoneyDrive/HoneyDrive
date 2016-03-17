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

public class GraphController extends Controller implements Initializable, IActionListener  {




    public void swap(){
        changeToScene("graph2.fxml");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        CarAction.addCreatedListener(this, CarActionsFilter.vehicle_speed);
        chart.getXAxis().setLabel("Seconds");
        chart.getYAxis().setLabel("Speed");
        chart.getData().add(speedSeries);
        chart.getData().add(scoreSeries);
        chart.setCreateSymbols(false);

    }


    public void updateSeries() {
        chart.getData().clear();
        chart.getData().add(speedSeries);
        chart.getData().add(scoreSeries);
    }


    @FXML
    LineChart chart;
    @FXML
    NumberAxis xAxis;
    @FXML NumberAxis yAxis;



}
