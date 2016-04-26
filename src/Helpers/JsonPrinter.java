package Helpers;

import metrics.*;

public class JsonPrinter implements IActionListener {

    public static void main(String[] args) {
        JsonPrinter jsonPrinter = new JsonPrinter();
        CarAction.addCreatedListener(jsonPrinter, CarActionsFilter.vehicle_speed);
        IDataReader reader = new ReadFromOpenXCFile("TestData/data3.json");
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
