import java.util.Set;

public class Tester implements CarActionListener
{
    public static void main(String[] args)
    {
        Tester test = new Tester();
        test.start();
    }

    public void start()
    {
        CarAction.addCreatedListener(this, CarActionsFilter.all);

        CarAction acc = new CarAction(CarActionsFilter.acceleratorPedalPosition, "test1", "test", 1.2);

        CarAction vspeed = new CarAction(CarActionsFilter.vehicleSpeed, "test2", "test", 1.2);
    }

    public void newCarAction(CarAction action)
    {
        System.out.println(action);
    }

}
