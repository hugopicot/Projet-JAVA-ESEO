package org.example.demo2.dao;

import org.example.demo2.model.Commentaire;

import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class CommentaireDao {



    public List<Commentaire>getAll() throws SQLException {
        List<Commentaire> commentaire = new ArrayList<Commentaire>();
        String sql = "SELECT * FROM Commentaire";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                commentaire.add(new Commentaire(
                        rs.getString("contenu"),
                        rs.getInt("id_commentaire"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_post"),
                        rs.getInt("id_parent")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  commentaire;
    }
    public Commentaire findByid(int id){
        Commentaire commentaire = null;
        String sql = "SELECT * FROM commentaire WHERE id_commentaire = ?";
        try (
            Connection conn = DatabaseConnection.getConnection();
           PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                commentaire = new Commentaire(rs.getString("contenu"),rs.getInt("id_commentaire"),rs.getTimestamp("date_creation").toLocalDateTime(), rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_post"),
                        rs.getInt("id_parent"));
            }
        } catch (SQLException e) {
         e.printStackTrace();
        }
        return commentaire;

    }
    public void add(Commentaire commentaire) {
        String sql = "INSERT INTO commentaire(contenu, id_commentaire, date_creation, score, id_utilisateur, id_post, id_parent) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, commentaire.getContenu());
            ps.setInt(2, commentaire.getId_commentaire());
            ps.setTimestamp(3, Timestamp.valueOf(commentaire.getDate_creation()));
            ps.setInt(4, commentaire.getScore());
            ps.setInt(5, commentaire.getId_utilisateur());
            ps.setInt(6, commentaire.getId_post());
            ps.setInt(7, commentaire.getId_parent());

            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Commentaire commentaire) throws SQLException {
 String sql = "UPDATE commentaire SET contenu = ?,id_commentaire = ?,date_creation = ?,score = ?,id_utilisateur = ?,id_post = ?,id_parent = ?  where id_commentaire = ?";
 try ( Connection conn = DatabaseConnection.getConnection();
       PreparedStatement ps = conn.prepareStatement(sql)){
     ps.setString(1, commentaire.getContenu());
     ps.setInt(2, commentaire.getId_commentaire());
     ps.setTimestamp(3, Timestamp.valueOf(commentaire.getDate_creation()));
     ps.setInt(4, commentaire.getScore());
     ps.setInt(5, commentaire.getId_utilisateur());
     ps.setInt(6, commentaire.getId_post());
     ps.setInt(7, commentaire.getId_parent());
     ps.executeUpdate();
 }
 catch (SQLException e){
 e.printStackTrace();
}
}
  public void delete(int id){
String sql = "DELETE FROM commentaire WHERE id_commentaire = ?";
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