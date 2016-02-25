import metrics.*;

public class Tester implements IActionListener
{
    public static void main(String[] args)
    {
        Tester test = new Tester();
        test.start();
    }

    public void start()
    {
        CarAction.addCreatedListener(this, CarActionsFilter.all);
        ProcessCarData proc = new ProcessCarData(new ReadFromOpenXCFileReader("src/metrics/data1.json"));
    }

    public void newCarAction(CarAction action)
    {
        System.out.println(action);
    }
}
