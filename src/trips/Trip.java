package trips;

import metrics.CarAction;
import metrics.CarActionsFilter;
import metrics.DataStreamSimulator;
import metrics.IDataStreamer;

import java.util.EnumSet;

public class Trip {
	
	private double totalDistance; 
	private double lastOdometerCount; 
	private double commutingDistance;
	private boolean isCommuting;
	IDataStreamer streamer;
	
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
	 
	 public void newAction(CarAction action) {
	        if(totalDistance==0){
	        	lastOdometerCount= (double) action.getValue();
	        }
	        else{
	        	double value = (double) action.getValue();
	        	totalDistance+= value-lastOdometerCount;
	        	if(isCommuting){
	        		commutingDistance+=value-lastOdometerCount; 
	        	}
	        	lastOdometerCount=value;
	        }
	    }

//	 public void stop(){
//		 streamer.stopStreaming();
//	 }
	 
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
