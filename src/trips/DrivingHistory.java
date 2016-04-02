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
	
	public long getDistanceThisYear(){
		return (long) (history.stream().mapToDouble(Trip::getTotalDistance).sum());
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
		return getDistanceThisYear()/history.size();
	}
	public long getCommutingDistanceThisYear() {
		return (long) history.stream().mapToDouble(Trip::getCommutingDistance).sum();
	}
	public long getInsuranceDistance() {
		return this.insuranceDistance;
	}

	public int getBeeCount(){
		return history.stream().mapToInt(a -> a.getBeeCount()).sum();
	}


	public int averageBeePerTrip(){
		return (history.stream().mapToInt(Trip::getBeeCount).sum())/history.size();
	}

	public int averageKmPerTrip(){
		return ((int) history.stream().mapToDouble(Trip::getTotalDistance).sum())/history.size();
	}
	public double averageFuelPerTrip() {
		return (history.stream().mapToDouble(Trip::getFuelBurntPer10Km).sum()) / history.size();
	}
	public void generateMockData(){
		addTrip(new Trip(new Score(5),50,50,5));
		addTrip(new Trip(new Score(4),60,30,4));
	}
}
