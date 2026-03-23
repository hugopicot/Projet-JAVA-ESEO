package org.example.demo2.dao;

import org.example.demo2.model.Subreddit;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubredditDao {

    public List<Subreddit> getAll() {
        List<Subreddit> subreddits = new ArrayList<>();
        String sql = "SELECT * FROM subreddit ORDER BY nom ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                subreddits.add(mapResultSetToSubreddit(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subreddits;
    }

    public Subreddit findById(int id) {
        String sql = "SELECT * FROM subreddit WHERE id_subreddit = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToSubreddit(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Subreddit sub) {
        String sql = "INSERT INTO subreddit (nom, description, date_creation) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sub.getNom());
            ps.setString(2, sub.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(sub.getDate_creation()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sub.setId_subreddit(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Subreddit mapResultSetToSubreddit(ResultSet rs) throws SQLException {
        return new Subreddit(
                rs.getInt("id_subreddit"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getTimestamp("date_creation").toLocalDateTime()
        );
    }
}
