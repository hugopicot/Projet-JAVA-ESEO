package org.example.demo2.dao;

import org.example.demo2.dao.interfaces.IModerationDao;
import org.example.demo2.model.Moderation;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ModerationDao implements IModerationDao {

    @Override
    public List<Moderation> getAll() {
        List<Moderation> moderationList = new ArrayList<>();
        String sql = "SELECT * FROM moderation";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                moderationList.add(mapResultSetToModeration(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moderationList;
    }

    @Override
    public Moderation findById(int id) {
        Moderation moderation = null;
        String sql = "SELECT * FROM moderation WHERE id_moderation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                moderation = mapResultSetToModeration(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moderation;
    }

    @Override
    public void add(Moderation moderation) {
        String sql = "INSERT INTO moderation (id_utilisateur, id_subreddit) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moderation.getId_utulisateur());
            ps.setInt(2, moderation.getId_subreddit());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Moderation moderation) {
        String sql = "UPDATE moderation SET id_utilisateur = ?, id_subreddit = ? WHERE id_moderation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moderation.getId_utulisateur());
            ps.setInt(2, moderation.getId_subreddit());
            ps.setInt(3, moderation.getId_moderation());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM moderation WHERE id_moderation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Moderation mapResultSetToModeration(ResultSet rs) throws SQLException {
        return new Moderation(
                rs.getInt("id_moderation"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_subreddit")
        );
    }
}
