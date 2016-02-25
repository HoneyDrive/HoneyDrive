import java.util.*;

public class DataStreamSimulator implements IActionListener, IDataStreamer
{
    List<IStreamListener> streamListeners = new ArrayList<>();
    private Queue<CarAction> actions = new ArrayDeque<>();

    public DataStreamSimulator(CarActionsFilter carActionsFilter)
    {
        CarAction.addCreatedListener((IActionListener) this, carActionsFilter);
    }

    @Override
    public void newCarAction(CarAction action)
    {
        actions.add(action);
    }

    @Override
    public void startStreaming()
    {
        long startTime = System.currentTimeMillis();
        CarAction action = actions.poll();
        if (action == null)
        {
            throw new IllegalArgumentException("No actions to stream");
        }
        long drivingStartTime = (long) (action.getTimestamp() * 1000);
        notifyListeners(action);

        while ((action = actions.poll()) != null)
        {
            long timeToWait = ((long) (action.getTimestamp() * 1000) - drivingStartTime) - (System.currentTimeMillis() - startTime);
            while (timeToWait > 0)
            {
                timeToWait = ((long) (action.getTimestamp() * 1000) - drivingStartTime) - (System.currentTimeMillis() - startTime);
            }

            notifyListeners(action);
        }

    }

    private void notifyListeners(CarAction action)
    {
        for (IStreamListener streamListener : streamListeners)
        {
            streamListener.onNewAction(action);
        }
    }

    @Override
    public void addStreamListener(IStreamListener streamListener)
    {
        streamListeners.add(streamListener);
    }

    public static void main(String[] args)
    {
        IDataReader reader = new ReadFromOpenXCFileReader("src/data2.json");
        IDataStreamer streamer = new DataStreamSimulator(CarActionsFilter.vehicle_speed);

        streamer.addStreamListener(a -> System.out.println(a.getName() + "=" + a.getValue()));

        reader.startReading();
        streamer.startStreaming();
    }

}
