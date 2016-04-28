package trips;

import metrics.*;

import java.util.*;

public class Trip implements IActionListener
{
    private static final int MAX_SPEED_DATA = 30;

    private Score score;

    private IDataStreamer streamer;
    private IDataReader reader;

    private Date date;

    private boolean isCommuting;

    private double totalDistance;
    private double lastOdometerCount;
    private double commutingDistance;
    private double insuranceDistance;
    private double fuelUsed;
    private double fuelUsedAtStart;
    private double speed;
    private double timestamp;

    private int speedCounter;

    private List<List<Double>> speedData;

    public Trip()
    {
        date = new Date();
        isCommuting = false;
        totalDistance = 0;
        lastOdometerCount = -1;
        commutingDistance = 0;
        insuranceDistance = 0;
        fuelUsed = 0;
        fuelUsedAtStart = -1;
        speed = 0;
        timestamp = 0;
        speedCounter = 0;
        speedData = new ArrayList<>();
        score = new Score();
    }

    public Trip(Score score, double totalDistance, double commutingDistance, double fuelUsed) {
        this.score = score;
        this.totalDistance = totalDistance;
        this.commutingDistance = commutingDistance;
        this.fuelUsed = fuelUsed;
    }

    public void start(String filepath, CarActionsFilter... filter)
    {
        Set<CarActionsFilter> filters = new HashSet<>(Arrays.asList(filter));
        streamer = new DataStreamSimulator(filepath, filters);
        streamer.addStreamListener(this::newCarAction);
        streamer.startStreaming();
    }

    public void start()
    {
        streamer = new DataStreamSimulator("src/metrics/TestData/data3.json", CarActionsFilter.odometer);
        streamer.addStreamListener(this::newCarAction);
        streamer.startStreaming();
    }

    public void start(String filepath, CarActionsFilter filter)
    {
        streamer = new DataStreamSimulator(filepath, filter);
        streamer.addStreamListener(this::newCarAction);
        streamer.startStreaming();
    }

    public void startWithoutDelay(String filepath, CarActionsFilter filter)
    {
        CarAction.addCreatedListener(this, filter);
        reader = new ReadFromOpenXCFile(filepath);
        reader.startReading();
    }

    private long lastUpdate = System.currentTimeMillis();

    public void newCarAction(CarAction action)
    {
        score.newCarAction(action);

        switch (action.getType())
        {
            case fuel_consumed_since_restart:
                if (fuelUsedAtStart == -1) { // First value
                    fuelUsedAtStart = (double) action.getValue();
                } else {
                    fuelUsed = (double) action.getValue() - fuelUsedAtStart;
                }
                break;
            case vehicle_speed:
                speed = (double) action.getValue();
                timestamp = action.getTimestamp();
                speedData.add(speedCounter, new ArrayList<>(Arrays.asList(speed, timestamp)));
                speedCounter++;
                if (speedCounter == MAX_SPEED_DATA)
                {
                    speedCounter = 0;
                }
                break;
            case odometer:
                if (lastOdometerCount == -1) // First value
                {
                    lastOdometerCount = (double) action.getValue();
                } else
                {
                    double value = (double) action.getValue();

                    totalDistance += value - lastOdometerCount;
                    if (isCommuting)
                    {
                        commutingDistance += value - lastOdometerCount;
                    }
                    lastOdometerCount = value;
                }
                break;
            default:
                // Do nothing
                break;
        }
    }



    public void stop()
    {
        CarAction.removeCreatedListener(this);
    }

    public Date getDate()
    {
        return date;
    }

    public void setCommuting(boolean c)
    {
        isCommuting = c;
    }

    public double getTotalDistance()
    {
        return totalDistance;
    }

    public double getCommutingDistance()
    {
        return commutingDistance;
    }

    public double getFuelUsed()
    {
        return fuelUsed;
    }

    public double getFuelBurntPerKm()
    {
        return getTotalDistance() < 1 ? 0 : getFuelUsed() / getTotalDistance();
    }

    public double getFuelBurntPer10Km()
    {
        return getTotalDistance() < 1 ? 0 : getFuelUsed() / (getTotalDistance() / 10);
    }

    public double getSpeed()
    {
        return speed;
    }

    public boolean getIsCommuting()
    {
        return isCommuting;
    }

    public int getBeeCount(){
        return score.getBeeCount();
    }
    public int getSmileyStatus(){
        return score.getSmileyStatus();
    }

}
