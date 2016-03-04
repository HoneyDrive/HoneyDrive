package trips;

import metrics.CarAction;
import metrics.CarActionsFilter;
import metrics.DataStreamSimulator;
import metrics.IDataStreamer;

import java.util.EnumSet;
import metrics.*;
public class Trip {
	
	private long totalDistance;
	private long lastOdometerCount;
	private long commutingDistance;
	private boolean isCommuting;
	IDataStreamer streamer; 
	private long insuranceDistance;

	
	public Trip(){
		start();
		totalDistance=0;
		isCommuting = false; 
		commutingDistance=0;
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

    public void newAction(CarAction action){
        System.out.println(action.getValue());
        if(totalDistance==0 && lastOdometerCount== 0) {
            lastOdometerCount = (long) action.getValue();
        }else{
            long value = (long) action.getValue();
            totalDistance+= value-lastOdometerCount;
            if(isCommuting){
                commutingDistance+=value-lastOdometerCount;
            }
            lastOdometerCount=value;
        }
    }
	 public void stop(){
		}
	 
	 public void setCommuting(boolean c){
		 isCommuting = c; 
	 }
	 public double getTotalDistance(){
		 return totalDistance;
	 }
	 public double getCommutingDistance(){
		 return commutingDistance; 
	 }
}
