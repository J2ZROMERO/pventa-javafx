package com.j2zromero.pointofsale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException{
    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/login/login.fxml"));
        Scene scene =  new Scene(loader.load());
        primaryStage.setTitle("Bienvenido");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}


