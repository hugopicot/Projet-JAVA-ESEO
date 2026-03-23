package org.example.demo2.dao;

import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDao {

    public boolean estAbonne(int idUtilisateur, int idSubreddit) {
        String sql = "SELECT 1 FROM abonnement WHERE id_utilisateur = ? AND id_subreddit = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idSubreddit);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void ajouter(int idUtilisateur, int idSubreddit) {
        String sql = "INSERT INTO abonnement (id_utilisateur, id_subreddit) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idSubreddit);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int idUtilisateur, int idSubreddit) {
        String sql = "DELETE FROM abonnement WHERE id_utilisateur = ? AND id_subreddit = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idSubreddit);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAbonnementsUtilisateur(int idUtilisateur) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_subreddit FROM abonnement WHERE id_utilisateur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id_subreddit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
}
