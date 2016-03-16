package UX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import UX.UXController;
import sun.applet.Main;

import java.io.IOException;

public class UXMain extends Application {

    public Scene mainScene;
    public Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(false);

        try {
            Parent page = FXMLLoader.load(getClass().getResource("view.fxml"));
            mainScene = new Scene(page);
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
