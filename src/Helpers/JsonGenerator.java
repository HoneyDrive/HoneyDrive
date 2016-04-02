package Helpers;

import metrics.CarActionsFilter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class JsonGenerator {

    private List<String> printList = new ArrayList<>();
//{"name": "accelerator_pedal_position","value": 0,"timestamp": 1364323939.012000}

    public void addElement(CarActionsFilter type, Double timestamp, Double value){
        String ll = "{\"name\":\"" + type.name() + "\",\"value\":" + value.toString() + ", \"timestamp\": " + timestamp.toString()+"}";
        printList.add(ll);
    }


    public void print(String filename){
        try {
            PrintWriter printWriter = new PrintWriter("src/metrics/TestData/generated/" + filename + ".json","UTF-8");
            printList.stream().forEach(a -> printWriter.write(a + "\n"));
            printWriter.close();
        }catch (FileNotFoundException e){
            System.out.println(e.getStackTrace());
        }catch (UnsupportedEncodingException e){
            System.out.println(e.getStackTrace());
        }
    }

    public void generateSmoothRide(){
        double timeInterval = 0.1;
        double speedChange = 0.1;
        double speed = 0;
        double time = 0;
        CarActionsFilter type = CarActionsFilter.vehicle_speed;
        for (int i = 0;i<500;i++){
            addElement(type,time,speed);
            time+= timeInterval;
            speed += speedChange;
        }
        for(int i=0;i<500;i++){
            addElement(type,time,speed);
            time+= timeInterval;
        }
        for(int i=0;i<500;i++){
            addElement(type,time,speed);
            time+=timeInterval;
            speed -= speedChange;
        }
        print("passive");
    }

    public void generateAgressiveRide(){
        printList.clear();
        double timeInterval = 0.1;
        double speedChange = 0.2;
        double speed = 0;
        double time = 0;
        CarActionsFilter carActionsFilter = CarActionsFilter.vehicle_speed;
        for(int i = 0;i<500;i++){
            for(int y = 0;y<30;y++){
                addElement(carActionsFilter,time,speed);
                time += timeInterval;
                speed+= speedChange;
            }
            for(int y=0;y<30;y++){
                addElement(carActionsFilter,time,speed);
                time += timeInterval;
                speed -= speedChange;
            }
        }
        print("aggressive");
    }

    public static void main(String[] args) {
        JsonGenerator jsonGenerator = new JsonGenerator();
        jsonGenerator.generateSmoothRide();
        jsonGenerator.generateAgressiveRide();
    }

}
