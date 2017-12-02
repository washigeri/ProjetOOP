package main;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.TabHandler;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("../sample.fxml").openStream());
        TabHandler.viewHandler = fxmlLoader.getController();
        TabHandler.TabChangeHandler();
        primaryStage.setTitle("Projet OOP");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        TabHandler.Start();
    }
}