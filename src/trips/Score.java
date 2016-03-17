package trips;

import metrics.CarAction;
import metrics.CarActionsFilter;

import java.util.ArrayList;
import java.util.List;

public class Score implements TripListener {


    private List<CarAction> speedList = new ArrayList<>();
    private List<Double> scores = new ArrayList<>();
    private Double timestamp;
    private static final double timeInterval=5;

    private double calculateScore(List<CarAction> carActions){
        Double avg = carActions.stream().mapToDouble(a -> ((Number) a.getValue()).doubleValue()).sum()/carActions.size();
        return  (carActions.stream().mapToDouble(a -> Math.pow(((Number) a.getValue()).doubleValue()-avg , 2)).sum())/(speedList.size()-2);
    }

    public double getScore(){
        return scores.stream().mapToDouble(a -> a).sum()/scores.size();
    }

    private Double firstTimeStamp = -1.0;
    public void newCarAction(CarAction carAction){
        if(firstTimeStamp == -1){
            firstTimeStamp = carAction.getTimestamp();
        }
        if(speedList.isEmpty()){
            timestamp = carAction.getTimestamp();
        }
        if(carAction.getType() == CarActionsFilter.vehicle_speed){
            if(carAction.getTimestamp()-timestamp >= timeInterval){
                System.out.println("Timestamp: " + Double.toString(carAction.getTimestamp()-firstTimeStamp) + " : " + calculateScore(speedList));
                speedList = new ArrayList<>();
            }
            else{
                speedList.add(carAction);
            }
        }
    }


    public static void main(String[] args) {
        Trip trip = new Trip();
        trip.startWithoutDelay("src/metrics/TestData/data3.json",CarActionsFilter.all);
    }
}
