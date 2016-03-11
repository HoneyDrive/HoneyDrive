package trips;

import java.util.ArrayList;
import java.util.List;

public class DrivingHistory {
	List<Trip> history = new ArrayList<Trip>();
	private long distanceThisYear;
	private long commutingDistanceThisYear;
	private long insuranceDistance;
	List<Long> previousCommutingDistances = new ArrayList<>();
	List<Long> previousTotalDistances = new ArrayList<>();
	
	

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
	public double getAverageDistance(){
		return distanceThisYear/history.size();
	}
	public double getCommutingDistanceThisYear() {
		return commutingDistanceThisYear;
	}
}
