package beehive;

import java.util.Date;

import trips.Trip;

public class Bee {
	private Date dateCreated;
	private String name; //TODO: Decide if bees should have names
	
	
	/*
	 * Takes in the trip on which the bee was created, so that date, score etc can be remembered
	 */
	public Bee(Trip trip) {
		this.dateCreated = trip.getDate();
	}
	
	/*
	 * Updates the bee
	 */
	public void update(){
		//TODO: Decide what to do here
		System.out.println("I got updated!");
	}
	
	/*
	 * Gets you buzzed
	 */
	public void buzz(){
		System.out.println("Buzz");
	}
	
	/*
	 * Returns: Name of the bee
	 * TODO: Decide if bees should have names
	 * TODO: NO! THEY SHOULD NOT..
	 */
	public String getName(){
		return name;
	}
	
	/*
	 * Returns: Date of creation
	 */
	public Date getDate(){
		return dateCreated;
	}
}
