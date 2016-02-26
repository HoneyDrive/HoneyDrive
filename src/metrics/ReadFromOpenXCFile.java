package metrics;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromOpenXCFile implements IDataReader
{
    private BufferedReader reader;
    private JSONParser parser = new JSONParser();

    public ReadFromOpenXCFile(String filePath)
    {
        try
        {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e)
        {
            System.err.println("Could not locate the file");
        }
    }

    @Override
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

        Double timestamp;
        Object timestampobj = jsonObject.get("timestamp");
        if (timestampobj instanceof Long)
        {
            timestamp = ((Long)timestampobj).doubleValue();
        }
        else
        {
            timestamp = (Double) timestampobj;
        }

        // If no one is listening, this object will be removed by GC.
        // If someones listening, this object will be passed to that instance by the CarAction class itself.
        new CarAction(name, value, timestamp);
    }
}
