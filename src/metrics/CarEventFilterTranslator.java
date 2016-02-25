package metrics;

import java.util.HashMap;
import java.util.Map;

class CarEventFilterTranslator
{
    private static Map<String, CarActionsFilter> translationDict;
    static
    {
        translationDict = new HashMap<>();
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
        CarActionsFilter filter = translationDict.get(eventString);

        if (filter == null)
        {
            return CarActionsFilter.other;
        }
        else
        {
            return filter;
        }
    }
}