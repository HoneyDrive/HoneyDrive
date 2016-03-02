package metrics;

class CarEventFilterTranslator
{
    public static CarActionsFilter translateStringToFilter(String eventString)
    {
        try
        {
            return CarActionsFilter.valueOf(eventString);
        }
        catch (IllegalArgumentException ex)
        {
            return CarActionsFilter.other;
        }
    }
}