public class ProcessCarData implements NewDataListener
{
    private ReadCarData reader;
    private SaveCarData logger;

    public void getNewData(String name, Object value, Double timestamp)
    {
        logger.addEntry(name, value, timestamp);

        CarActionsFilter type = CarEventFilterTranslator.translateStringToFilter(name);

        // If no one is listening, this object will be removed by GC.
        // If someones listening, this object will be passed to that instance by the CarAction class itself.
        CarAction action = new CarAction(type, name, value, timestamp);
    }
}
