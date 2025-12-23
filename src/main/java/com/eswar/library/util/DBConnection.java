package com.eswar.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties properties = new Properties();

    static {
        System.out.println("DBConnection: Loading db.properties...");
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find db.properties");
            } else {
                properties.load(input);
                Class.forName(properties.getProperty("db.driver"));
                System.out.println("DBConnection: Driver loaded.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        // Fallback to properties if Env Vars are missing (for local dev)
        if (url == null) url = properties.getProperty("db.url");
        if (user == null) user = properties.getProperty("db.username");
        if (password == null) password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
