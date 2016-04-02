package Helpers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import metrics.CarAction;
import metrics.CarActionsFilter;
import trips.Score;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class Controller implements Initializable {


    protected XYChart.Series scoreSeries;
    protected XYChart.Series speedSeries;
    protected Score score;
    protected List<CarAction> carActionList = new ArrayList<>();
    protected Stage stage;

    public void initialize(URL location, ResourceBundle resources) {
        speedSeries = new XYChart.Series();
        scoreSeries = new XYChart.Series();
        score = new Score();

    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<CarAction> getCarActionList() {
        return carActionList;
    }

    public void setCarActionList(List<CarAction> carActionList) {
        this.carActionList = carActionList;
    }

    public void setSpeedSeries(XYChart.Series speedSeries) {
        this.speedSeries = speedSeries;
        updateSeries();
    }

    public void setScoreSeries(XYChart.Series scoreSeries) {
        this.scoreSeries = scoreSeries;
        updateSeries();
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public abstract void updateSeries();


    private Double firstTimeStamp = -1.0;

    public void newCarAction(CarAction action) {
        if(firstTimeStamp == -1){
            firstTimeStamp = action.getTimestamp();
            return;
        }
        score.newCarAction(action);
        if (action.getType() == CarActionsFilter.vehicle_speed){
            getCarActionList().add(action);
            speedSeries.getData().add(new XYChart.Data<>(action.getTimestamp()-firstTimeStamp,action.getValue()));
        }
    }

    public void changeToScene(String path){
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            Scene scene = new Scene((loader.load()));
            Controller controller = loader.getController();
            controller.setStage(newStage);

            controller.setSpeedSeries(speedSeries);
            controller.setScore(score);
            newStage.setScene(scene);
            stage.close();
            newStage.show();
        }catch (IOException e){

        }
    }

    @FXML
    public void addScore(){
        scoreSeries.getData().clear();
        Map<Double,Double> map = score.getScoresMap();
        Collection<Double> keys = map.keySet();
        for(Double key:keys){
            scoreSeries.getData().add(new XYChart.Data<>(key,map.get(key)));
        }
        System.out.println(score.getScore());
    }
}
