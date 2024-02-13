package com.j2zromero.pointofsale.utils;

import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class VerifyField {


    public  static void checkTextFieldNoNEmptyKey(TextField field){
        field.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }
    public  static void checkTextFieldEmpty(TextField field){

        Color alertInput = new Color(1.0, 0.0, 0.0, 0.3); // Red with 0.5 opacity
        field.setBackground(new Background(new BackgroundFill(alertInput, null, null)));
        field.setStyle("-fx-prompt-text-fill: " + Color.BLACK.toString().replace("0x", "#") + ";");
    }
    public static void checkChoiceBoxEmpty(ChoiceBox field){
        Color alertInput = new Color(1.0, 0.0, 0.0, 0.3); // Red with 0.5 opacity
        field.setBackground(new Background(new BackgroundFill(alertInput, null, null)));
        field.setStyle("-fx-prompt-text-fill: " + Color.BLACK.toString().replace("0x", "#") + ";");
    }
    public static void checkChoiceBoxNoNEmptyKey(ChoiceBox field){
        field.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }



    public  static void checkTextFieldChoiceBoxEmpty(Object field[]){

        for(Object element: field){

            if(element instanceof  TextField){

                if(((TextField)element).getText().equalsIgnoreCase("")){
                   checkTextFieldEmpty((TextField) element);
                }else{
                    checkTextFieldNoNEmptyKey((TextField) element);
                }
            }
            else if(element instanceof ChoiceBox<?>){

                if(((ChoiceBox<?>) element).getValue() == null){
                    checkChoiceBoxEmpty((ChoiceBox) element);
                }else{
                    checkChoiceBoxNoNEmptyKey((ChoiceBox) element);
                }

            }
        }
    }

    public  static void checkTextFieldChoiceBoxEmpty(Object field){

            if(field instanceof  TextField){

                if(((TextField)field).getText().equalsIgnoreCase(" ")){
                    checkTextFieldEmpty((TextField) field);
                }else{
                    checkTextFieldNoNEmptyKey((TextField) field);
                }
            }
            else if(field instanceof ChoiceBox<?>){

                if(((ChoiceBox<?>) field).getValue() == null){
                    checkChoiceBoxEmpty((ChoiceBox) field);
                }else{
                    checkChoiceBoxNoNEmptyKey((ChoiceBox) field);
                }


        }
    }
}
