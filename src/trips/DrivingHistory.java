package trips;

import java.util.ArrayList;
import java.util.List;

public class DrivingHistory {
	List<Trip> history = new ArrayList<Trip>();
	private long distanceThisYear;
	private long commutingDistanceThisYear;
	private long insuranceDistance;
	List<Long> previousCommutingDistances = new ArrayList<Long>();
	List<Long> previousTotalDistances = new ArrayList<Long>();
	
	

	public DrivingHistory(){
		
	}
	
	public void addTrip(Trip trip){
		history.add(trip);
		distanceThisYear+= trip.getTotalDistance();
		commutingDistanceThisYear+=trip.getCommutingDistance();
		if(distanceThisYear>insuranceDistance){
			sendWarning();
		}
	}
	
	public double getDistanceThisYear(){
		return distanceThisYear;
	}


   public double getFuelConsumptionAvg() {
       double avg = 0;
       for (Trip trip:history){
           avg += trip.getFuelBurntPerKm();
       }
       avg /= history.size();
       return avg;

   }


	
	public void sendWarning(){
		
	}
	public Trip getTrip(int index){
		return history.get(index);
	}
	
	public void newYear(){
		//Ta vare på disse i en variabel, beste måten å lagre de på? 
		previousCommutingDistances.add(commutingDistanceThisYear);
		previousTotalDistances.add(distanceThisYear);
		distanceThisYear = 0 ;
		commutingDistanceThisYear=0;
	}
	public void setInsuranceDistance(long insuranceDistance){
		this.insuranceDistance=insuranceDistance;
	}
	public double getAverageDistance(){
		return distanceThisYear/history.size();
	}
	public double getCommutingDistanceThisYear() {
		return commutingDistanceThisYear;
	}
	public long getInsuranceDistance() {
		return this.insuranceDistance;
	}
}
