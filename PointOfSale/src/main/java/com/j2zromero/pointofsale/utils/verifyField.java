package com.j2zromero.pointofsale.utils;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class verifyField {


    public static void checkTextField(TextField field){
        Color alertInput = new Color(1.0, 0.0, 0.0, 0.3); // Red with 0.5 opacity
        field.setBackground(new Background(new BackgroundFill(alertInput, null, null)));
        field.setStyle("-fx-prompt-text-fill: " + Color.BLACK.toString().replace("0x", "#") + ";");
    }
    public static void checkChoiceBox(ChoiceBox field){
        Color alertInput = new Color(1.0, 0.0, 0.0, 0.3); // Red with 0.5 opacity
        field.setBackground(new Background(new BackgroundFill(alertInput, null, null)));
        field.setStyle("-fx-prompt-text-fill: " + Color.BLACK.toString().replace("0x", "#") + ";");
    }

}
