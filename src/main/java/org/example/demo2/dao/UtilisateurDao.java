package org.example.demo2.dao;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.util.*;
/**
 * Data Access Object pour les utilisateurs.
 */

public class UtilisateurDao {
    public List<Utilisateur>getAll() throws  SQLException {
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
                     rs.getTimestamp("date_inscription").toLocalDateTime(),
                rs.getInt("karma")));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return utulisateur ;
    }
    public Utilisateur findByid(int id){
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM Utulisateur WHERE id_utilisateur = ?";
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
                        rs.getTimestamp("date_inscription").toLocalDateTime(),
                        rs.getInt("karma"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur ;

    }
    public void add(Utilisateur utulisateur) {
        String sql = "INSERT INTO utulisateur (id_utilisateur,pseudo,email,mot_de_passe,date_inscription,karma) VALUES (?, ?, ?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, utulisateur.getId_utilisateur());
            ps.setString(2, utulisateur.getPseudo());
            ps.setString(3, utulisateur.getEmail());
            ps.setString(3, utulisateur.getMot_de_passe());
            ps.setTimestamp(4, Timestamp.valueOf(utulisateur.getDate_inscription()));
            ps.setInt(3, utulisateur.getKarma());





            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Utilisateur utulisateur) throws SQLException {
        String sql = "UPDATE utulisateur  SET id_utilisateur = ?,pseudo = ?,email = ?,mot_de_passe = ?,date_inscription = ?,karma = ?  where id_utilisateur = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, utulisateur.getId_utilisateur());
            ps.setString(2, utulisateur.getPseudo());
            ps.setString(3, utulisateur.getEmail());
            ps.setString(3, utulisateur.getMot_de_passe());
            ps.setTimestamp(4, Timestamp.valueOf(utulisateur.getDate_inscription()));
            ps.setInt(3, utulisateur.getKarma());

            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM Utulisateur  WHERE id_utilisateur = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
