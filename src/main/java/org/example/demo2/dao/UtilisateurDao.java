package org.example.demo2.dao;


import org.example.demo2.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private final String url = "jdbc:mysql://localhost:3306/forum_reddit_like";
    private final String user = "root";
    private final String password = "root"; // adapte si besoin

    // Connexion
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // CREATE
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (pseudo, email, mot_de_passe, karma) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getPseudo());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setInt(4, utilisateur.getKarma());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ BY ID
    public Utilisateur getUtilisateurById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        Utilisateur utilisateur = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                utilisateur = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getTimestamp("date_inscription"),
                        rs.getInt("karma")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateur;
    }

    // READ ALL
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getTimestamp("date_inscription"),
                        rs.getInt("karma")
                );
                utilisateurs.add(utilisateur);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateurs;
    }

    // UPDATE
    public void updateUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET pseudo=?, email=?, mot_de_passe=?, karma=? WHERE id_utilisateur=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getPseudo());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setInt(4, utilisateur.getKarma());
            stmt.setInt(5, utilisateur.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LOGIN (bonus utile)
    public Utilisateur login(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE email=? AND mot_de_passe=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getTimestamp("date_inscription"),
                        rs.getInt("karma")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Utilisateur getUtilisateurByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getTimestamp("date_inscription"),
                        rs.getInt("karma")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

