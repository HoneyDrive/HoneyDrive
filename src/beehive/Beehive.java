package beehive;

import java.util.List;

import trips.Trip;

import java.util.ArrayList;

public class Beehive {
	private List<Bee> bees;
	
	/*
	 * Beehive consist of a list of bees. Not much else at the moment
	 */
	public Beehive() {
		bees = new ArrayList<Bee>();
		
	}
	
	/*
	 * Adds a new bee to the list
	 * Takes in the trip on which the bee was created, so that date, score etc can be remembered
	 */
	public void addBee(Trip trip){
		bees.add(new Bee(trip));
	}
	
	/*
	 * Updates all the bees
	 */
	public void updateBees(){
		for(Bee b : bees){
			b.update();
		}
		System.out.println("Done updating bees, " + getBeeCount() + " bees updated");
	}
	
	/*
	 * Returns: Number of bees in the list
	 */
	public int getBeeCount(){
		return bees.size();
	}
}
