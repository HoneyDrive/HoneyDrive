public class Tester implements IActionListener
{
    public static void main(String[] args)
    {
        Tester test = new Tester();
        test.start();

        /*
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\mmkar\\IdeaProjects\\HoneyDrive-Metrics-Sprint1\\src\\main\\java\\data1.json"));
            JSONParser parser = new JSONParser();
            String line;

            while ((line = reader.readLine()) != null)
            {
                JSONObject jsonObject = (JSONObject) parser.parse(line);

                String name = (String) jsonObject.get("name");
                Object value = jsonObject.get("value");
                Double timestamp = (Double) jsonObject.get("timestamp");

                System.out.println(name);
                System.out.println(value);
                System.out.println(timestamp);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        */
    }

    public void start()
    {
        CarAction.addCreatedListener(this, CarActionsFilter.all);

        //CarAction acc = new CarAction(CarActionsFilter.acceleratorPedalPosition, "test1", "test", 1.2);

        //CarAction vspeed = new CarAction(CarActionsFilter.vehicleSpeed, "test2", "test", 1.2);

        ProcessCarData proc = new ProcessCarData(new ReadFromOpenXCFileReader("src/main/java/data1.json"));
    }

    public void newCarAction(CarAction action)
    {
        System.out.println(action);
    }
}
