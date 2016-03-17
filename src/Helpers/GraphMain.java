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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        IDataReader reader;

        primaryStage.show();
        reader = new ReadFromOpenXCFile("src/metrics/TestData/data3.json");
        reader.startReading();

    }
}
