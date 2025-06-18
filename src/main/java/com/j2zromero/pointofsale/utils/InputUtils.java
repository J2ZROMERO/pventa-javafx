package com.j2zromero.pointofsale.utils;
import javafx.scene.control.DatePicker;
import java.time.ZoneId;
import java.util.Date;

public class InputUtils {



    public static Date convertToDate(DatePicker datePicker) {
        if (datePicker.getValue() != null) {
            return Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }


    public static Double parseDouble(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
