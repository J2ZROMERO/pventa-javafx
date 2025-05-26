package com.j2zromero.pointofsale.utils;
import io.github.cdimascio.dotenv.Dotenv;

public class MariaDB {
    static Dotenv dotenv = Dotenv.load();
    public static final String URL = dotenv.get("DB_URL");
    public static final String user = dotenv.get("DB_USER");
    public static final String password = dotenv.get("DB_PASS");
}