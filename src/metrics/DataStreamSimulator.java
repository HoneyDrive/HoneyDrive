package metrics;

import java.util.*;

public class DataStreamSimulator implements IActionListener, IDataStreamer
{
    private IDataReader reader;
    private List<IStreamListener> streamListeners = new ArrayList<>();
    private boolean streaming;

    private Queue<CarAction> actions = new ArrayDeque<>();

    public DataStreamSimulator(String filePath, CarActionsFilter carActionsFilter)
    {
        reader = new ReadFromOpenXCFile(filePath);
        CarAction.addCreatedListener(this, carActionsFilter);
        readData();
    }

    public DataStreamSimulator(IDataReader reader, CarActionsFilter carActionsFilter)
    {
        this.reader = reader;
        CarAction.addCreatedListener(this, carActionsFilter);
        readData();
    }

    public DataStreamSimulator(String filePath, Set<CarActionsFilter> carActionsFilters)
    {
        reader = new ReadFromOpenXCFile(filePath);
        CarAction.addCreatedListener(this, carActionsFilters);
        readData();
    }

    public DataStreamSimulator(IDataReader reader, Set<CarActionsFilter> carActionsFilters)
    {
        this.reader = reader;
        CarAction.addCreatedListener(this, carActionsFilters);
        readData();
    }

    private void readData()
    {
        reader.startReading();
    }

    @Override
    public void newCarAction(CarAction action)
    {
        actions.add(action);
    }

    @Override
    public void startStreaming()
    {
        streaming = true;
        Thread thread = new Thread(this::startStreamingLogic);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stopStreaming()
    {
        streaming = false;
    }

    private void startStreamingLogic()
    {
        long startTime = System.currentTimeMillis();
        CarAction action = actions.poll();
        if (action == null)
        {
            throw new IllegalStateException("No actions to stream");
        }
        long drivingStartTime = Math.round(action.getTimestamp() * 1000); // in ms
        notifyListeners(action);

        while ((action = actions.poll()) != null && streaming)
        {
            long systemTimePassedSinceStart = System.currentTimeMillis() - startTime;
            long carTimePassedSinceStart = Math.round(action.getTimestamp() * 1000) - drivingStartTime;
            long timeToWait = carTimePassedSinceStart - systemTimePassedSinceStart;

            if (timeToWait > 0)
            {
                try
                {
                    Thread.sleep(timeToWait);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
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

}
