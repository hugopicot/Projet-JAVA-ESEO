package org.example.demo2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire de connexion à la base de données.
 */
public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bdd_forum";
        String user = "root";
        String password = "";

        // Retourne la connexion
        return DriverManager.getConnection(url, user, password);

    }

}
