import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFromOpenXCFileReader implements IDataReader
{
    private List<IDataListener> listeners = new ArrayList<IDataListener>();
    private BufferedReader reader;
    private JSONParser parser = new JSONParser();

    public ReadFromOpenXCFileReader(String filePath)
    {
        try
        {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e)
        {
            System.err.println("Could not locate the file");
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
        }
        catch (IOException e)
        {
            System.err.println("Could not read from the file.");
        }
        catch (ParseException e)
        {
            System.err.println("Something was wrong in the format of the file.");
        }
    }

    private void createData(String line) throws ParseException
    {
        JSONObject jsonObject = (JSONObject) parser.parse(line);

        String name = (String) jsonObject.get("name");
        Object value = jsonObject.get("value");
        Double timestamp = (Double) jsonObject.get("timestamp");

        new CarAction(name, value, timestamp);
        
        for (IDataListener listener : listeners)
        {
            listener.getNewData(name, value, timestamp);
        }
    }

    public void addNewDataListener(IDataListener listener)
    {
        listeners.add(listener);
    }


    public static void main(String[] args)
    {
        ReadFromOpenXCFileReader reader = new ReadFromOpenXCFileReader("src/data2.json");
        reader.addNewDataListener((s, o, d) -> System.out.println(s + "=" + o));
        reader.startReading();
    }

    /**
     * Do not use this method.
     * Use createData(String) instead.
     */
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
        double timestamp = Double.parseDouble(line.substring(startIndex + 1, endIndex));
    }

}
