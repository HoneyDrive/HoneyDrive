package trips;

import java.util.Date;

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


	public Trip(){
		totalDistance=0;
		isCommuting = false;
		commutingDistance=0;
		lastOdometerCount=0;
		fuelUsed = 0 ;
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
        IDataReader reader = new ReadFromOpenXCFile(filepath);
        reader.startReading();
    }



	public void newAction(CarAction action){
		if(action.getName().equals("fuel_consumed_since_restart")){
			fuelUsed += (long) action.getValue();
		}
		else{
        if(totalDistance==0 && lastOdometerCount== 0) { //TODO: Dette gir bug. Hvis odometer=0 i første vil den ikke telles med. Også bug når den ikke er 0. Fix og sjekk at stemmer med test.
            lastOdometerCount = (long) action.getValue();
        }else{
            long value = (long) action.getValue();
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
	 public double getTotalDistance(){
		 return totalDistance;
	 }
	 public double getCommutingDistance(){
		 return commutingDistance; 
	 }
	public long getFuelUsed() {return fuelUsed;}

}
