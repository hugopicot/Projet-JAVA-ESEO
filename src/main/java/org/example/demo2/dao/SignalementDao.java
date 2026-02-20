package org.example.demo2.dao;
import org.example.demo2.model.Post;
import org.example.demo2.model.SignalementCommentaire;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class SignalementDao {
    public List<SignalementCommentaire>getAll() throws  SQLException {
        List<SignalementCommentaire> signalementCommentaires = new ArrayList<SignalementCommentaire>();
        String sql = "SELECT * FROM SignalementCommentaire";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                signalementCommentaires.add(new SignalementCommentaire(
                        rs.getInt("id_signalement_com"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_commentaire")));

            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  signalementCommentaires;
    }
    public SignalementCommentaire findByid(int id){
        SignalementCommentaire signalementCommentaire = null;
        String sql = "SELECT * FROM Post WHERE id_signalement_com = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                signalementCommentaire= new SignalementCommentaire( rs.getInt("id_signalement_com"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_commentaire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signalementCommentaire ;

    }
    public void add(SignalementCommentaire signalementCommentaire) {
        String sql = "INSERT INTO signalementCommentaire (id_signalement_com,id_utilisateur,id_commentaire) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, signalementCommentaire.getId_signalement_com());
            ps.setInt(2, signalementCommentaire.getId_utilisateur());
            ps.setInt(3, signalementCommentaire.getId_commentaire());



            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(SignalementCommentaire signalementCommentaire) throws SQLException {
        String sql = "UPDATE signalementCommentaire  SET id_signalement_com = ?,id_utilisateur = ?,id_commentaire = ?  where id_signalement_com = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, signalementCommentaire.getId_signalement_com());
            ps.setInt(2, signalementCommentaire.getId_utilisateur());
            ps.setInt(3, signalementCommentaire.getId_commentaire());

            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM SignalementCommentaire  WHERE id_signalement_com = ?";
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
