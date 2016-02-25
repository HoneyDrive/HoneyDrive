package metrics;

public interface IDataListener
{
    void getNewData(String name, Object value, Double timestamp);
}
