package com.j2zromero.pointofsale.utils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {
    public static void setNullable(CallableStatement stmt, int parameterIndex, Object value, int sqlType) throws SQLException {
        if (value != null) {
            stmt.setObject(parameterIndex, value);
        } else {
            stmt.setNull(parameterIndex, sqlType);
        }
    }

    // Generic method to safely retrieve a nullable value from ResultSet
    public static <T> T getNullable(ResultSet rs, String columnLabel, Class<T> type) {
        try {
            return rs.getObject(columnLabel, type);
        } catch (SQLException e) {
            // Log and return null if value is not parseable or any other error occurs
            System.err.println("Error retrieving column '" + columnLabel + "': " + e.getMessage());
            return null;
        }
    }
}
