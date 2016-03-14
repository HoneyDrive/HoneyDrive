package metrics;

public interface IDataStreamer
{
    void addStreamListener(IStreamListener streamListener);
    void startStreaming();
    void stopStreaming();
}
