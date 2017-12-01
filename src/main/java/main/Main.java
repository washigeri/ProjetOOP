package main;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.TabHandler;
import views.ViewHandler;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("../sample.fxml").openStream());
        ViewHandler viewHandler = (ViewHandler) fxmlLoader.getController();
        TabHandler.viewHandler = viewHandler;
        TabHandler.TabChangeHandler();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }
}