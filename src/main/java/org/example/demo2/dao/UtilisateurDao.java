package org.example.demo2.dao;

import org.example.demo2.dao.interfaces.IUtilisateurDao;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class UtilisateurDao implements IUtilisateurDao {
    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utulisateur = new ArrayList<Utilisateur>();
        String sql = "SELECT * FROM Utilisateur";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                utulisateur.add(new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                       rs.getString("mot_de_passe"),
                rs.getInt("karma")));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return utulisateur ;
    }
    @Override
    public Utilisateur findById(int id){
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                utilisateur= new Utilisateur( rs.getInt("id_utilisateur"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getTimestamp("date_inscription"),
                        rs.getInt("karma"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur ;

    }
    @Override
    public void add(Utilisateur utulisateur) {
        String sql = "INSERT INTO utilisateur (pseudo, email, mot_de_passe, date_inscription, karma) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utulisateur.getPseudo());
            ps.setString(2, utulisateur.getEmail());
            ps.setString(3, utulisateur.getMotDePasse());
            ps.setInt(4, utulisateur.getKarma());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                utulisateur.setId(rs.getInt(1));
            }

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utilisateur utulisateur) {
        String sql = "UPDATE utilisateur SET pseudo = ?, email = ?, mot_de_passe = ?, karma = ? WHERE id_utilisateur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utulisateur.getPseudo());
            ps.setString(2, utulisateur.getEmail());
            ps.setString(3, utulisateur.getMotDePasse());
            ps.setInt(4, utulisateur.getKarma());
            ps.setInt(5, utulisateur.getId());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(int id){
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        return utilisateurs;
    }

    // UPDATE
    @Override
    public void updateUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET pseudo=?, email=?, mot_de_passe=?, karma=? WHERE id_utilisateur=?";

        try (Connection conn = DatabaseConnection.getConnection();
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
    @Override
    public void deleteUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur=?";

        try (Connection conn = DatabaseConnection.getConnection();
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

        try (Connection conn = DatabaseConnection.getConnection();
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


    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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

