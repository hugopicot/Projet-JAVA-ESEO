package org.example.demo2.util;

/**
 * Gestionnaire de connexion à la base de données.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TO DELETE TO AVOID MERGE CONFLICTS. JUST TESTING
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bdd_forum";
    private static final String USER ="root";
    private static final String PASSWORD ="";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
