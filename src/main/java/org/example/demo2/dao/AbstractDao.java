package org.example.demo2.dao;

import org.example.demo2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {

    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void closeResources(Connection conn, PreparedStatement ps) {
        closeResources(conn, ps, null);
    }
}
