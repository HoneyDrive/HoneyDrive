package Testing;

import metrics.*;
import org.junit.Ignore;
import org.junit.Test;
import trips.DrivingHistory;
import static org.junit.Assert.assertTrue;
import trips.Trip;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Tester {


    private DrivingHistory drivingHistory = new DrivingHistory();
    private Trip trip1;
    private Trip trip2;

    public Tester(){
        trip1= new Trip(); trip1.setCommuting(true);
        trip1.startWithoutDelay("TestData/testData1.json", CarActionsFilter.all);
        try {
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        trip1.stop();
        trip2 = new Trip(); trip2.setCommuting(false);
        trip2.startWithoutDelay("TestData/testData2.json",CarActionsFilter.all);
        try {
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        trip2.stop();
        drivingHistory.addTrip(trip1);
        drivingHistory.addTrip(trip2);

    }

    @Test
    public void checkTrip1TotalDistance(){
        assertTrue(trip1.getTotalDistance() == 1669);
    }
    @Test
    public void checkTrip1CommutingDistance(){
        assertTrue(trip1.getCommutingDistance() == 1669);
    }
    @Test
    public void checkTrip1DistanceCorrect(){
        assertTrue(trip1.getCommutingDistance() == trip1.getTotalDistance());
    }

    @Test
    public void checkTrip2TotalDistance(){
        assertTrue(trip2.getTotalDistance()==1337);
    }
    @Test
    public void checkTrip2CommutingDistance(){
        assertTrue(trip2.getCommutingDistance()==0);
    }
    @Test
    public void checkSameNumbersTrip1AndDrivingHistory(){
        assertTrue(drivingHistory.getTrip(0).getTotalDistance()+drivingHistory.getTrip(1).getTotalDistance() == 3006);

    }
    @Test
    public void checkSameNumbersTrip2AndDrivingHistory(){
        assertTrue(drivingHistory.getTrip(0).getCommutingDistance()+drivingHistory.getTrip(1).getCommutingDistance()==1669);

    }

    @Test
    public void testCommutingDistanceDrivingHistory(){
        assertTrue(drivingHistory.getCommutingDistanceThisYear()==1669);
    }
    @Test
    public void testDistanceThisYearDrivingHistory(){
        assertTrue(drivingHistory.getDistanceThisYear()==3006);
    }
    @Test
    public void testAverageDistanceDrivingHistory(){
        assertTrue(drivingHistory.getAverageDistance() ==1503);
    }


    @Test
    public void testFuelConsumptionForTrip1(){
        assertTrue(drivingHistory.getTrip(0).getFuelUsed()==100);
        assertTrue(trip1.getFuelUsed()==100);
    }
    @Test
    public void testFuelConsumptionForTrip2(){
        assertTrue("Expected 60 but was: " + drivingHistory.getTrip(1).getFuelUsed(),drivingHistory.getTrip(1).getFuelUsed()==60);
        assertTrue(trip2.getFuelUsed()==60);
    }
    @Test
    public void testFuelBurntPerKmForTrip1(){
        assertTrue(trip1.getFuelBurntPerKm()==100.0/1669.0);
    }
    @Test
    public void testFuelBurntPerKmForTrip2(){
        assertTrue(trip2.getFuelBurntPerKm()==60.0/1337.0);
    }
    @Test
    public void testFuelBurntPer10KmForTrip1(){
        assertTrue("Expected: " + 100.0/(1669.0/10.0) + "but was: " + trip1.getFuelBurntPer10Km(),trip1.getFuelBurntPer10Km()==100.0/(1669.0/10.0));
    }
    @Test
    public void testFuelBurntPer10KmForTrip2(){
        assertTrue(trip2.getFuelBurntPer10Km()==60.0/(1337.0/10.0));
    }

    @Test
    public void trip1HasZeroBees(){
        assertTrue(drivingHistory.getTrip(0).getBeeCount() == 0);
    }

    @Test
    public void trip2HasZeroBees(){
        assertTrue(drivingHistory.getTrip(1).getBeeCount() == 0);
    }

    @Test
    public void checkDrivingHistoryAverageBeeCount(){
        assertTrue(drivingHistory.averageBeePerTrip()==0);
    }
    @Test
    public void checkDrivingHistoryAverageKmCount(){
        assertTrue(drivingHistory.averageKmPerTrip()==(1669.0+1337.0)/2.0);
    }
    @Test
    public void checkDrivingHistoryAverageFuelPerTrip(){
        assertTrue("expected: " +((100.0/(1669.0/10.0))+60.0/(1337.0/10.0))/2.0 + "actual:" + drivingHistory.averageFuelPerTrip(), drivingHistory.averageFuelPerTrip()==((100.0/(1669.0/10.0))+60.0/(1337.0/10.0))/2.0);
    }


    @Test
    public void startSingleFilterWithDelayTesting()
    {
        System.out.println("\n\n---------Single Filter With Delay---------");

        Map<String, Set<String>> typeOfData = new HashMap<>();

        IDataStreamer streamer = new DataStreamSimulator("TestData/data2.json", CarActionsFilter.engine_speed);
        streamer.addStreamListener(action ->
        {
            Set<String> types = typeOfData.getOrDefault(action.getType().name(), new HashSet<>());
            types.add(action.getValue().getClass().toString());
            typeOfData.put(action.getType().name(), types);

            System.out.println("Single filter, delayed: " + action.getName());

            if (action.getType().equals(CarActionsFilter.other))
            {
                System.err.println(action.getName() + " was unexpected!");
            }
        });
        streamer.startStreaming();
    }
    @Test
    public void startMultipleFilterWithDelayTesting()
    {
        System.out.println("\n\n---------Multiple Filter With Delay---------");

        Map<String, Set<String>> typeOfData = new HashMap<>();

        IDataStreamer streamer = new DataStreamSimulator("TestData/data2.json", EnumSet.of(CarActionsFilter.latitude, CarActionsFilter.longitude));
        streamer.addStreamListener(action ->
        {
            Set<String> types = typeOfData.getOrDefault(action.getType().name(), new HashSet<>());
            types.add(action.getValue().getClass().toString());
            typeOfData.put(action.getType().name(), types);

            System.out.println("Multiple filters, delayed: " + action.getName());

            if (action.getType().equals(CarActionsFilter.other))
            {
                System.err.println(action.getName() + " was unexpected!");
            }
        });
        streamer.startStreaming();
    }


    private void getFilePaths(List<String> openXCFileList){
        try
        {
            Files.walk(Paths.get("src/metrics/TestData")).forEach(filePath ->
            {
                if (Files.isRegularFile(filePath))
                {
                    int index = filePath.toString().indexOf("TestData");
                    String relativePath = filePath.toString().substring(index);
                    openXCFileList.add(relativePath);
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public void startMultipleFilterWithoutDelayTesting()
    {
        System.out.println("\n\n---------Multiple Filter Without Delay---------");

        List<String> openXCFileList = new ArrayList<>();
        Map<String, Set<String>> typeOfData = new HashMap<>();
        getFilePaths(openXCFileList);

        for (String filepath : openXCFileList)
        {
            CarActionsFilter previousFilter = CarActionsFilter.other;
            for (CarActionsFilter filter : CarActionsFilter.values())
            {
                CarAction.addCreatedListener(action ->
                {
                    Set<String> types = typeOfData.getOrDefault(action.getType().name(), new HashSet<>());
                    types.add(action.getValue().getClass().toString());
                    typeOfData.put(action.getType().name(), types);

                    if (action.getType().equals(CarActionsFilter.other))
                    {
                        System.err.println(action.getName() + " was unexpected!");
                    }
                }, EnumSet.of(previousFilter, filter));

                previousFilter = filter;
            }
            IDataReader reader = new ReadFromOpenXCFile(filepath);
            reader.startReading();

            System.out.println(filepath + " done");
        }
        System.out.println("\n\n");
        printTypesMap(typeOfData);
        System.out.println("\n\n");
    }
    @Test
    public void startSingleFilterWithoutDelayTesting()
    {
        System.out.println("\n\n---------Single Filter Without Delay---------");

        List<String> openXCFileList = new ArrayList<>();
        Map<String, Set<String>> typeOfData = new HashMap<>();
        getFilePaths(openXCFileList);

        for (String filepath : openXCFileList)
        {
            for (CarActionsFilter filter : CarActionsFilter.values())
            {
                CarAction.addCreatedListener(action ->
                {
                    Set<String> types = typeOfData.getOrDefault(action.getType().name(), new HashSet<>());
                    types.add(action.getValue().getClass().toString());
                    typeOfData.put(action.getType().name(), types);

                    if (action.getType().equals(CarActionsFilter.other))
                    {
                        System.err.println(action.getName() + " was unexpected! File = " + filepath);
                    }
                }, filter);
            }
            IDataReader reader = new ReadFromOpenXCFile(filepath);
            reader.startReading();

            System.out.println(filepath + " done");
        }
        System.out.println("\n\n");
        printTypesMap(typeOfData);
        System.out.println("\n\n");
    }

    private void printTypesMap(Map<String, Set<String>> typeOfData)
    {
        for (Map.Entry<String, Set<String>> entry : typeOfData.entrySet())
        {
            System.out.println(entry.getKey() + ":");
            System.out.println("\t" + entry.getValue());
        }
    }
}
