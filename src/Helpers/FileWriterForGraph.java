package Helpers;

import metrics.*;
import trips.Trip;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileWriterForGraph implements IActionListener {
    private IDataReader reader;

    public void fileWriter(){
        try{
            PrintWriter printWriter = new PrintWriter("k.json","UTF-8");
            for (CarAction carAction : actions){
                System.out.println(carAction.getName());
                printWriter.write(  Double.toString(carAction.getTimestamp()-firstTimeStamp) + ", " + Double.toString(((Number)carAction.getValue()).doubleValue())  + "\n" );
            }
            System.out.println("haha");
            printWriter.close();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    List<CarAction> actions = new ArrayList<>();
    private double firstTimeStamp = -1;
    @Override
    public void newCarAction(CarAction action) {
        if (firstTimeStamp == -1){
            firstTimeStamp = action.getTimestamp();
        }
        actions.add(action);
    }
    public void startWithoutDelay(String filepath, CarActionsFilter filter) {
        CarAction.addCreatedListener(this, filter);
        reader = new ReadFromOpenXCFile(filepath);
        reader.startReading();
    }

    public void stop() {
        CarAction.removeCreatedListener(this);
    }


    public static void main(String[] args) {
        FileWriterForGraph trip = new FileWriterForGraph();
        trip.startWithoutDelay("TestData/data3.json", CarActionsFilter.vehicle_speed);
        try{
            Thread.sleep(1000);
        }catch (Exception e){

        }
        trip.stop();
        trip.fileWriter();


    }
}
