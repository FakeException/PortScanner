package com.euphoric.scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
        primaryStage.setTitle("Port Scanner");
        primaryStage.setScene(new Scene(root, 949, 565));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
