package Helpers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import metrics.CarAction;
import metrics.CarActionsFilter;
import metrics.IActionListener;
import trips.Score;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphController implements Initializable, IActionListener {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CarAction.addCreatedListener(this, CarActionsFilter.vehicle_speed);
        score = new Score();
        speedSeries = new XYChart.Series();
        scoreSeries = new XYChart.Series();
        chart.getXAxis().setLabel("Seconds");
        chart.getYAxis().setLabel("Speed");
        chart.getData().add(speedSeries);
        chart.getData().add(scoreSeries);
        chart.setCreateSymbols(false);
        /*
        scoreSeries.getData().add(new XYChart.Data<>(0.2,5));
        scoreSeries.getData().add(new XYChart.Data<>(0.3,7));
        speedSeries.getData().add(new XYChart.Data<>(1,23));
*/
        speedSeries.getData().addAll(new XYChart.Data(0.1,10));
    }


    @FXML
    LineChart chart;
    @FXML
    NumberAxis xAxis;
    @FXML NumberAxis yAxis;
    private XYChart.Series speedSeries;
    private XYChart.Series scoreSeries;

    private Score score;
    @FXML
    public void addScore(){
        scoreSeries.getData().clear();
        Map<Double,Double> map = score.getScoresMap();
        Collection<Double> keys = map.keySet();
        for(Double key:keys){
            scoreSeries.getData().add(new XYChart.Data<>(key,map.get(key)));
        }
    }
    @FXML
    public void addVehicleSpeed(){
        System.out.println("hei!");
    }
    private Double firstTimeStamp = -1.0;
    private Double yMax = 0.0;
    private Double xMax = 0.0;


    @Override
    public void newCarAction(CarAction action) {
        if(firstTimeStamp == -1){
            firstTimeStamp = action.getTimestamp();
            return;
        }
        score.newCarAction(action);
        if (action.getType() == CarActionsFilter.vehicle_speed){
            if(((Number)action.getValue()).doubleValue() > yMax){
                yMax = ((Number)action.getValue()).doubleValue();
            }
            if(action.getTimestamp() > xMax){
                xMax = action.getTimestamp();
            }
            speedSeries.getData().add(new XYChart.Data<>(action.getTimestamp()-firstTimeStamp,action.getValue()));
        }
    }

}
