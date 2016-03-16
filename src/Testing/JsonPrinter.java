package Testing;

import metrics.*;

public class JsonPrinter implements IActionListener {

    public static void main(String[] args) {
        JsonPrinter jsonPrinter = new JsonPrinter();
        CarAction.addCreatedListener(jsonPrinter, CarActionsFilter.fuel_consumed_since_restart);
        IDataReader reader = new ReadFromOpenXCFile("src/metrics/TestData/data3.json");
        reader.startReading();
        try{
            Thread.sleep(100000);
        }catch (InterruptedException e){
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void newCarAction(CarAction action) {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}
