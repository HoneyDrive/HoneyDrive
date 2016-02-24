import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFromOpenXCFile implements ReadCarData
{
    private List<NewDataListener> listeners = new ArrayList<>();
    private BufferedReader reader;

    public ReadFromOpenXCFile(String filePath)
    {
        try
        {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e)
        {
            System.out.println("Could not locate the file.");
        }
    }

    public void startReading()
    {
        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                createData(line);
            }
        } catch (IOException e)
        {
            System.out.println("Could not read from the file");
        }
    }

    private void createData(String line)
    {

    }

    public void addNewDataListener(NewDataListener newDataListener)
    {
        listeners.add(newDataListener);
    }


    public static void main(String[] args)
    {
        ReadFromOpenXCFile reader = new ReadFromOpenXCFile("./files/downtown-crosstown.json");
        reader.startReading();
    }

    // Not used
    private void createDataAlternative(String line)
    {
        int startIndex = line.indexOf(':');
        int endIndex = line.indexOf(',', startIndex);
        String name = line.substring(startIndex + 2, endIndex - 1);

        startIndex = line.indexOf(':', endIndex);
        endIndex = line.indexOf(',', startIndex);
        String value = line.substring(startIndex + 1, endIndex);

        startIndex = line.indexOf(':', endIndex);
        endIndex = line.length() - 2;
        long timestamp = (long) (Double.parseDouble(line.substring(startIndex + 1, endIndex)) * 1000); // milliseconds

        System.out.println("name=" + name + ", value=" + value + ", timestamp=" + timestamp);
    }

}
