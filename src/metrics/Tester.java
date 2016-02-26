package metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
-------------------------------
REMEMBER TO download all the test data from the OpenXC web page to get the most reliable results.
-------------------------------
 */


public class Tester
{
    public static void main(String[] args)
    {
        Tester test = new Tester();
        test.startAllTests();
    }

    public void startAllTests()
    {
        startSingleFilterWithoutDelayTesting();
        startMultipleFilterWithoutDelayTesting();
        startSingleFilterWithDelayTesting();
        startMultipleFilterWithDelayTesting();
    }

    public void startSingleFilterWithDelayTesting()
    {
        System.out.println("\n\n---------Single Filter With Delay---------");

        Map<String, Set<String>> typeOfData = new HashMap<>();

        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data2.json", CarActionsFilter.engine_speed);
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

    public void startMultipleFilterWithDelayTesting()
    {
        System.out.println("\n\n---------Multiple Filter With Delay---------");

        Map<String, Set<String>> typeOfData = new HashMap<>();

        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data2.json", EnumSet.of(CarActionsFilter.latitude, CarActionsFilter.longitude));
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


    public void startMultipleFilterWithoutDelayTesting()
    {
        System.out.println("\n\n---------Multiple Filter Without Delay---------");

        List<String> openXCFileList = new ArrayList<>();
        Map<String, Set<String>> typeOfData = new HashMap<>();

        try
        {
            Files.walk(Paths.get("src/metrics/TestData")).forEach(filePath ->
            {
                if (Files.isRegularFile(filePath))
                {
                    openXCFileList.add(filePath.toString());
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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

    public void startSingleFilterWithoutDelayTesting()
    {
        System.out.println("\n\n---------Single Filter Without Delay---------");

        List<String> openXCFileList = new ArrayList<>();
        Map<String, Set<String>> typeOfData = new HashMap<>();

        try
        {
            Files.walk(Paths.get("src/metrics/TestData")).forEach(filePath ->
            {
                if (Files.isRegularFile(filePath))
                {
                    openXCFileList.add(filePath.toString());
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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
