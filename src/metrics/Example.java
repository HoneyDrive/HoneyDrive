package metrics;

import java.util.EnumSet;

public class Example
{
    public static void main(String[] args)
    {
        Example test = new Example();
        test.startWithDelay();
    }

    public void startWithDelay()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data2.json", CarActionsFilter.all);
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }

    public void startWithoutDelay()
    {
        CarAction.addCreatedListener(this::newAction, CarActionsFilter.all);
        IDataReader reader = new ReadFromOpenXCFile("src/metrics/TestData/data2.json");
        reader.startReading();
    }

    public void startWithDelayMultipleFilters()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data3.json", EnumSet.of(CarActionsFilter.latitude,CarActionsFilter.longitude));
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }

    public void startWithoutDelayMultipleFilters()
    {
        CarAction.addCreatedListener(this::newAction, EnumSet.of(CarActionsFilter.latitude,CarActionsFilter.longitude));
        IDataReader reader = new ReadFromOpenXCFile("src/metrics/TestData/data3.json");
        reader.startReading();
    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}