package metrics;

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

    public CarAction(String name, Object value, Double timestamp)
    {
        setType(CarEventFilterTranslator.translateStringToFilter(name));
        setName(name);
        setValue(value);
        setTimestamp(timestamp);

        triggerCreatedEvent(this);
    }

    private static HashMap<CarActionsFilter, Set<IActionListener>> listeners = new HashMap<>();

    public static void addCreatedListener(IActionListener newListener, CarActionsFilter filter)
    {
        Set<IActionListener> hset = listeners.getOrDefault(filter, new HashSet<>());
        hset.add(newListener);
        listeners.put(filter, hset);
    }

    public static void addCreatedListener(IActionListener newListener, Set<CarActionsFilter> filters)
    {
        for (CarActionsFilter filter : filters)
        {
            Set<IActionListener> hset = listeners.getOrDefault(filter, new HashSet<>());
            hset.add(newListener);
            listeners.put(filter, hset);
        }
    }

    private void triggerCreatedEvent(CarAction action)
    {
        Set<IActionListener> allSubscribers = listeners.getOrDefault(action.type, new HashSet<>());
        allSubscribers.addAll(listeners.getOrDefault(CarActionsFilter.all, new HashSet<>()));

        for (IActionListener actionListener : allSubscribers)
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