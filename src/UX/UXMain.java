package UX;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class UXMain extends Application
{

    public Scene mainScene;
    public Stage primaryStage;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(false);
        primaryStage.setTitle("HoneyDrive");

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent page = loader.load();
            mainScene = new Scene(page);
            primaryStage.setScene(mainScene);
            primaryStage.show();
            UXController controller = loader.getController();
            primaryStage.setOnCloseRequest(we -> writeDistanceDrivenToFile(controller));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private void writeDistanceDrivenToFile(UXController controller)
    {
        double distance = controller.getTrip().getTotalDistance() + controller.getDrivingHistory().getDistanceThisYear();
        try(PrintWriter writer = new PrintWriter("insuranceData.txt", "UTF-8"))
        {
            writer.println("Distance driven this year:");
            writer.println(distance);
        }
        catch (FileNotFoundException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

    }
}
