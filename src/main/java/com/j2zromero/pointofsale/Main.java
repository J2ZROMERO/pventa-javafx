package com.j2zromero.pointofsale;

import com.j2zromero.pointofsale.utils.DbTest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

///  https://chatgpt.com/share/68602cb1-181c-800c-8961-e4f60de05c8b
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException{
//    FXMLLoader MainLoader = new FXMLLoader(Main.class.getResource("/views/menu/menu.fxml"));
        FXMLLoader MainLoader = new FXMLLoader(Main.class.getResource("/views/login/login.fxml"));
    FXMLLoader  DbLoader = new FXMLLoader(Main.class.getResource("/views/global_warnings/test_db_connection.fxml"));
        // Cargar CSS global


        Scene MainScene =  new Scene(MainLoader.load());
        Scene DbScene =  new Scene(DbLoader.load());
        primaryStage.setTitle("Bienvenido");
        try {
            DbTest.Test();
            primaryStage.setScene(MainScene);
            primaryStage.setResizable(false);
        }catch (Exception e ){
            primaryStage.setScene(DbScene);
        }
        // Center the window on the screen
        primaryStage.centerOnScreen();
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}


