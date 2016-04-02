package trips;

import metrics.CarAction;
import metrics.CarActionsFilter;

import java.util.*;

public class Score implements TripListener {


    private List<CarAction> speedList = new ArrayList<>();
    private List<Double> scores = new ArrayList<>();
    private Double timestamp;
    private static final double timeInterval=1;
    private int beeCount;
    private int smileyStatus; // -1 = Happy, 0 = litt happy, 1 = Sint

    public int getSmileyStatus() {
        return smileyStatus;
    }


    public Score(int beeCount){
        this.beeCount = beeCount;
    }
    public Score(){

    }
    private double calculateScore(List<CarAction> carActions){
        Double avg = carActions.stream().mapToDouble(a -> ((Number) a.getValue()).doubleValue()).sum()/carActions.size();
        return  (carActions.stream().mapToDouble(a -> Math.pow(((Number) a.getValue()).doubleValue()-avg , 2)).sum())/(speedList.size()-2);
    }

    public double getScore(){
        return scores.stream().mapToDouble(a -> a).sum()/scores.size();
    }

    private Map<Double,Double> scoresMap = new HashMap<>(); // timestamp,score
    private Double firstTimeStamp = -1.0;


    public void newCarAction(CarAction carAction){
        if(firstTimeStamp == -1){
            firstTimeStamp = carAction.getTimestamp();
        }
        if(speedList.isEmpty()){
            timestamp = carAction.getTimestamp();
        }
        if(carAction.getType() == CarActionsFilter.vehicle_speed){
            speedList.add(carAction);
            if(carAction.getTimestamp()-timestamp >= timeInterval){
                addScore(carAction);
            }
        }
    }
    private Double lastScore = -1.0;
    private Double stopTimeStamp;
    private static final Double idlingTimeInterval = 30.0;
    private void addScore(CarAction carAction){
        Double calcScore;
        if(getSumOfSpeedList() < 1){
            if(lastScore == -1.0){
                stopTimeStamp = carAction.getTimestamp();
                lastScore = 1.0;
            }
            if(carAction.getTimestamp()-stopTimeStamp  > idlingTimeInterval ){
                lastScore += 0.1;
                calcScore = lastScore;
                addScore(calcScore,carAction);

            }
        }else{
            calcScore = calculateScore(speedList);
            lastScore = -1.0;
            addScore(calcScore,carAction);
        }
        speedList = new ArrayList<>();
    }

    private void addScore(Double calcScore,CarAction carAction){
        calcScore *= 1 + (getSumOfSpeedList()/200);
        scoresMap.put(carAction.getTimestamp()-firstTimeStamp,calcScore);
        scores.add(calcScore);
        checkBeeUpdate(carAction,calcScore);
        setSmileyStatus(calcScore);
    }

    private void setSmileyStatus(Double score){
        if(score > scoreThreshold){
            smileyStatus = 1;
        }
        else if(score > 5){
            smileyStatus = 0;
        }
        else{
            smileyStatus = -1;
        }
    }

    private Double beeStartTime;
    private static final Double scoreThreshold = 10.0;
    private static final Double beeInterval = 30.0;
    private void checkBeeUpdate(CarAction carAction,Double score){
        if(beeStartTime == null || beeStartTime == -1 || score >scoreThreshold){
            beeStartTime = carAction.getTimestamp();
        }
        if(score <= scoreThreshold &&carAction.getTimestamp() - beeStartTime > beeInterval ){
            beeCount++;
            beeStartTime = -1.0;
        }
    }
    private Double getSumOfSpeedList(){
        return speedList.stream().mapToDouble(a-> (Double) a.getValue()).sum();
    }

    public Map<Double,Double> getScoresMap(){
        return scoresMap;
    }

    public static void main(String[] args) {
        Trip trip = new Trip();
        trip.startWithoutDelay("src/metrics/TestData/data3.json",CarActionsFilter.all);
    }

    public int getBeeCount() {
        return beeCount;
    }

}
