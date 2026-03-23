package org.example.demo2.dao;

import org.example.demo2.model.SignalementPost;
import org.example.demo2.model.SignalementCommentaire;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;

public class SignalementDao {

    public void addSignalementPost(SignalementPost sig) {
        String sql = "INSERT INTO signalement_post (id_utilisateur, id_post) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sig.getId_utilisateur());
            ps.setInt(2, sig.getId_post());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sig.setId_signalement(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSignalementCommentaire(SignalementCommentaire sig) {
        String sql = "INSERT INTO signalement_commentaire (id_utilisateur, id_commentaire) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sig.getId_utilisateur());
            ps.setInt(2, sig.getId_commentaire());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sig.setId_signalement_com(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existeSignalementPost(int idUtilisateur, int idPost) {
        String sql = "SELECT 1 FROM signalement_post WHERE id_utilisateur = ? AND id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idPost);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
