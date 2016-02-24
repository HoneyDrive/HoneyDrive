import java.util.HashMap;
import java.util.Map;

public enum CarActionsFilter
{
    all,
    steering_wheel_angle,
    torque_at_transmission,
    engine_speed,
    vehicle_speed,
    accelerator_pedal_position,
    parking_brake_status,
    brake_pedal_status,
    transmission_gear_position,
    gear_lever_position,
    odometer,
    ignition_status,
    fuel_level,
    fuel_consumed_since_restart,
    door_status,
    headlamp_status,
    high_beam_status,
    windshield_wiper_status,
    latitude,
    longitude,
    other
}

class CarEventFilterTranslator
{
    private static Map<String, CarActionsFilter> translationDict;
    static
    {
        translationDict = new HashMap<String, CarActionsFilter>();
        translationDict.put("steering_wheel_angle,", CarActionsFilter.steering_wheel_angle);
        translationDict.put("torque_at_transmission", CarActionsFilter.torque_at_transmission);
        translationDict.put("engine_speed", CarActionsFilter.engine_speed);
        translationDict.put("vehicle_speed", CarActionsFilter.vehicle_speed);
        translationDict.put("accelerator_pedal_position", CarActionsFilter.accelerator_pedal_position);
        translationDict.put("parking_brake_status", CarActionsFilter.parking_brake_status);
        translationDict.put("brake_pedal_status", CarActionsFilter.brake_pedal_status);
        translationDict.put("transmission_gear_position", CarActionsFilter.transmission_gear_position);
        translationDict.put("gear_lever_position", CarActionsFilter.gear_lever_position);
        translationDict.put("odometer", CarActionsFilter.odometer);
        translationDict.put("ignition_status", CarActionsFilter.ignition_status);
        translationDict.put("fuel_level", CarActionsFilter.fuel_level);
        translationDict.put("fuel_consumed_since_restart", CarActionsFilter.fuel_consumed_since_restart);
        translationDict.put("door_status", CarActionsFilter.door_status);
        translationDict.put("headlamp_status", CarActionsFilter.headlamp_status);
        translationDict.put("high_beam_status", CarActionsFilter.high_beam_status);
        translationDict.put("windshield_wiper_status", CarActionsFilter.windshield_wiper_status);
        translationDict.put("latitude", CarActionsFilter.latitude);
        translationDict.put("longitude", CarActionsFilter.longitude);
    }

    public static CarActionsFilter translateStringToFilter(String eventString)
    {
        return translationDict.get(eventString);
    }
}