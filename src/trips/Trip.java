package trips;

import metrics.CarAction;
import metrics.CarActionsFilter;
import metrics.DataStreamSimulator;
import metrics.IDataStreamer;

import java.util.EnumSet;
import metrics.*;
public class Trip {
	
	private double totalDistance; 
	private double lastOdometerCount; 
	private double commutingDistance;
	private boolean isCommuting;
<<<<<<< HEAD
	IDataStreamer streamer; 
<<<<<<< HEAD
	private int insuranceDistance;
=======
	private int insuranceDistance
=======
	IDataStreamer streamer;
>>>>>>> 9f65dfbeb79dfd4ad1155abce6029f52c716b59a
>>>>>>> master
	
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
<<<<<<< HEAD
	 public void stop(){
		}
=======

//	 public void stop(){
//		 streamer.stopStreaming();
//	 }
>>>>>>> master
	 
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
