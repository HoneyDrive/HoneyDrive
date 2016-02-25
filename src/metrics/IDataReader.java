package metrics;

public interface IDataReader
{
    void addNewDataListener(IDataListener newDataListener);
    void startReading();
}
