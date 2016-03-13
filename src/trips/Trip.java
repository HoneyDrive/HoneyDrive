package trips;

import java.util.*;

import metrics.*;

public class Trip {
	
    private long totalDistance;
    private long lastOdometerCount;
    private long commutingDistance;
    private boolean isCommuting;
    private IDataStreamer streamer;
    private long insuranceDistance;
    private Date date;
    private long fuelUsed ;
    IDataReader reader;
    private double speed ;
    private double timestamp ;
    private List<List<Double>> speedData = new ArrayList<>();
    private int speedCounter ;
    private int maxSpeedData=30;


    public Trip(){
    totalDistance=0;
    isCommuting = false;
    commutingDistance=0;
    lastOdometerCount=-1;
    fuelUsed = 0 ;
    date = new Date();
    }

    public void start()
    {
        streamer = new DataStreamSimulator("src/metrics/TestData/data3.json",  CarActionsFilter.odometer);
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }
    public void start(String filepath,CarActionsFilter filter){
        streamer = new DataStreamSimulator(filepath,filter);
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }
    public void startWithoutDelay(String filepath,CarActionsFilter filter){
        CarAction.addCreatedListener(this::newAction, filter);
        reader = new ReadFromOpenXCFile(filepath);
        reader.startReading();
    }



    public void newAction(CarAction action){
        if(action.getName().equals("fuel_consumed_since_restart")){
            fuelUsed += (Long) action.getValue();
        }
        else if (action.getName().equals("vehicle_speed")){
            speed = (double) action.getValue();
            timestamp = action.getTimestamp();
            speedData.add(speedCounter, new ArrayList<>(Arrays.asList(speed, timestamp)));
            speedCounter++;
            if (speedCounter==maxSpeedData){
                speedCounter=0;
            }
        }
        else{
            if(lastOdometerCount==0){
                lastOdometerCount = (Long) action.getValue();
                totalDistance+= lastOdometerCount;
                if(isCommuting){
                    commutingDistance+= lastOdometerCount;
                }
            }

            else if(totalDistance==0 && lastOdometerCount==-1) { 
                lastOdometerCount = (Long) action.getValue();

            }else{
                long value = (Long) action.getValue();
                totalDistance+= value-lastOdometerCount;
                if(isCommuting){
                    commutingDistance+=value-lastOdometerCount;
                }
                lastOdometerCount=value;
            }
        }}
    public void stop(){
    }

    public Date getDate(){
     return date;
    }
    public void setCommuting(boolean c){
     isCommuting = c;
    }
    public long getTotalDistance(){
     return totalDistance;
    }
    public long getCommutingDistance(){
     return commutingDistance;
    }
    public long getFuelUsed() {return fuelUsed;}
    public long getFuelBurntPerKm(){
        return getTotalDistance()/getFuelUsed();
    }
    public double    getSpeed(){return speed; }


}
