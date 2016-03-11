package Testing;

import metrics.CarActionsFilter;
import metrics.Tester;
import trips.DrivingHistory;
import trips.Trip;

import junit.framework.*;

public class Test extends TestCase {

    public void testReader(){
        Tester tester = new Tester();
        tester.startAllTests();
    }

    private DrivingHistory drivingHistory = new DrivingHistory();

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
    }
    public void testAddNotCommutingDistance(){
        Trip newTrip = new Trip(); newTrip.setCommuting(false);
        newTrip.startWithoutDelay("src/metrics/TestData/odometer_data2.json",CarActionsFilter.odometer);
        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        assertTrue(newTrip.getCommutingDistance()==0);
        assertTrue(newTrip.getTotalDistance()==1337);
        drivingHistory.addTrip(newTrip);
    }

    public void testTripMakingAndDrivingHistory(){
        testAddCommutingDistance();
        assertTrue(drivingHistory.getCommutingDistanceThisYear() == drivingHistory.getTrip(0).getCommutingDistance());
        System.out.println(drivingHistory.getTrip(0).getCommutingDistance());
        System.out.println(drivingHistory.getCommutingDistanceThisYear());
        assertTrue(drivingHistory.getDistanceThisYear() == drivingHistory.getTrip(0).getTotalDistance());
        assertTrue(drivingHistory.getAverageDistance() == drivingHistory.getTrip(0).getTotalDistance());
        testAddNotCommutingDistance();
        System.out.println(drivingHistory.getCommutingDistanceThisYear());
        System.out.println(drivingHistory.getTrip(0).getCommutingDistance());
        System.out.println(drivingHistory.getTrip(1).getCommutingDistance());
        assertTrue(drivingHistory.getCommutingDistanceThisYear() == drivingHistory.getTrip(0).getCommutingDistance());
        assertTrue(drivingHistory.getDistanceThisYear()==drivingHistory.getTrip(0).getTotalDistance()+drivingHistory.getTrip(1).getTotalDistance());
        assertTrue(drivingHistory.getAverageDistance()==(drivingHistory.getTrip(0).getTotalDistance()+drivingHistory.getTrip(1).getTotalDistance())/2);
    }
    public void runAllTests(){
        testTripMakingAndDrivingHistory();
        testReader();

    }

    public static void main(String[] args){
        Test test = new Test();
        test.testTripMakingAndDrivingHistory();
    }

}
