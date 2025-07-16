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


    public static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}
