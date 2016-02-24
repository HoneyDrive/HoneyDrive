import java.util.*;

public class CarAction
{
    private CarActionsFilter type;
    private String name;
    private Object value;
    private Double timestamp;

    public CarAction(CarActionsFilter type, String name, Object value, Double timestamp)
    {
        setType(type);
        setName(name);
        setValue(value);
        setTimestamp(timestamp);

        triggerCreatedEvent(this);
    }

    private static HashMap<CarActionsFilter, Set<CarActionListener>> listeners = new HashMap<>();

    public static void addCreatedListener(CarActionListener newListener, CarActionsFilter filter)
    {
        Set<CarActionListener> hset = listeners.getOrDefault(filter, new HashSet<>());
        hset.add(newListener);
        listeners.put(filter, hset);
    }

    void triggerCreatedEvent(CarAction action)
    {
        Set<CarActionListener> allSubscribers = listeners.getOrDefault(action.type, new HashSet<>());
        allSubscribers.addAll(listeners.getOrDefault(CarActionsFilter.all, new HashSet<>()));

        for (CarActionListener actionListener : allSubscribers)
        {
            actionListener.newCarAction(action);
        }
    }

    public CarActionsFilter getType()
    {
        return this.type;
    }

    private void setType(CarActionsFilter type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    private void setName(String name)
    {
        this.name = name;
    }

    public Object getValue()
    {
        return value;
    }

    private void setValue(Object value)
    {
        this.value = value;
    }

    public Double getTimestamp()
    {
        return timestamp;
    }

    private void setTimestamp(Double timestamp)
    {
        this.timestamp = timestamp;
    }
}