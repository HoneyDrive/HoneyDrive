package metrics;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFromOpenXCFile implements IDataReader
{
    private BufferedReader reader;
    private JSONParser parser = new JSONParser();

    public ReadFromOpenXCFile(String filePath)
    {
        reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));
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
        } catch (ParseException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createData(String line) throws ParseException
    {
        JSONObject jsonObject = (JSONObject) parser.parse(line);

        String name = (String) jsonObject.get("name");
        Object value = jsonObject.get("value");
        if (value instanceof Number)
        {
            value = ((Number) value).doubleValue();
        }
        double timestamp = ((Number) jsonObject.get("timestamp")).doubleValue();

        // If no one is listening, this object will be removed by GC.
        // If someones listening, this object will be passed to that instance by the CarAction class itself.
        new CarAction(name, value, timestamp);
    }

}
