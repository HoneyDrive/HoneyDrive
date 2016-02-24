import java.util.HashMap;
import java.util.Map;

public enum CarActionsFilter
{
    all,
    acceleratorPedalPosition,
    engineSpeed,
    vehicleSpeed,
    // TODO: legg til resten
    other
}

class CarEventFilterTranslator
{
    private static Map<String, CarActionsFilter> translationDict;
    static
    {
        translationDict = new HashMap<>();
        translationDict.put("", CarActionsFilter.acceleratorPedalPosition);
        // TODO
    }

    public static CarActionsFilter translateStringToFilter(String eventString)
    {
        // TODO
    }
}