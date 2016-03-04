package Testing;

import metrics.CarActionsFilter;
import metrics.Tester;
import trips.DrivingHistory;
import trips.Trip;

import junit.framework.*;
import java.util.ArrayList;

public class Test extends TestCase {

    public void testReader(){
        Tester tester = new Tester();
        tester.startAllTests();
    }

    private DrivingHistory drivingHistory = new DrivingHistory();

    public void testAddCommutingDistance() {
        Trip newTrip = new Trip(); newTrip.setCommuting(true);
        newTrip.start("src/metrics/TestData/odometer_data.json", CarActionsFilter.odometer);
        drivingHistory.addTrip(newTrip);
        //assertTrue(newTrip.getCommutingDistance()==1669);
        //assertTrue(newTrip.getTotalDistance()==1669);
        //assertTrue(newTrip.getCommutingDistance()==newTrip.getCommutingDistance());

    }

    public static void main(String[] args){
        Test test = new Test();
        test.testAddCommutingDistance();
    }



}
