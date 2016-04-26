package Helpers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import metrics.IDataReader;
import metrics.ReadFromOpenXCFile;

public class GraphMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph2.fxml"));
        Parent parent = loader.load();
        ((Controller)loader.getController()).setStage(primaryStage);
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        IDataReader reader;

        primaryStage.show();
        reader = new ReadFromOpenXCFile("TestData/highway-speeding.json");
        reader.startReading();
    }
}
