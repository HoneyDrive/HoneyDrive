import metrics.*;

public class Tester
{
    public static void main(String[] args)
    {
        Tester test = new Tester();
        test.startWithDelay();
    }

    public void startWithDelay()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/data2.json", CarActionsFilter.all);
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }

    public void startWithoutDelay()
    {
        CarAction.addCreatedListener(this::newAction, CarActionsFilter.all);
        IDataReader reader = new ReadFromOpenXCFileReader("src/metrics/data1.json");
        reader.startReading();
    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}