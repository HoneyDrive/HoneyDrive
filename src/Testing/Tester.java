package Testing;

import metrics.CarActionsFilter;
import org.junit.Test;
import trips.DrivingHistory;

import trips.Trip;
import junit.framework.TestCase;


public class Tester extends TestCase {

    @Test
    public void testReader(){
        metrics.Tester tester = new metrics.Tester();
        tester.startAllTests();
    }

    private DrivingHistory drivingHistory = new DrivingHistory();

    @Test
    public void testAddCommutingDistance() {
        Trip newTrip = new Trip(); newTrip.setCommuting(true);
        newTrip.startWithoutDelay("src/metrics/TestData/odometer_data.json", CarActionsFilter.odometer);
        try {
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        assertTrue(newTrip.getCommutingDistance() == 1669);
        assertTrue(newTrip.getTotalDistance() == 1669);
        assertTrue(newTrip.getCommutingDistance() == newTrip.getTotalDistance());
        drivingHistory.addTrip(newTrip);
        newTrip.stop();
    }
    @Test
    public void testAddNotCommutingDistance(){
        Trip newTrip = new Trip(); newTrip.setCommuting(false);
        newTrip.startWithoutDelay("src/metrics/TestData/odometer_data2.json",CarActionsFilter.odometer);
        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        assertTrue(newTrip.getCommutingDistance()==0);
        assertTrue(newTrip.getTotalDistance()==1337);
        drivingHistory.addTrip(newTrip);
        newTrip.stop();
    }
    @Test
    public void testTripMakingAndDrivingHistory(){
        testAddCommutingDistance();
        testAddNotCommutingDistance();
        assertTrue(drivingHistory.getCommutingDistanceThisYear()==1669);
        assertTrue(drivingHistory.getDistanceThisYear()==3006);
        assertTrue(drivingHistory.getAverageDistance() ==1503);
        assertTrue(drivingHistory.getTrip(0).getTotalDistance()+drivingHistory.getTrip(1).getTotalDistance() == 3006);
        assertTrue(drivingHistory.getTrip(0).getCommutingDistance()+drivingHistory.getTrip(1).getCommutingDistance()==1669);
    }
}
