package org.example.demo2.dao;
import org.example.demo2.model.Message;
import org.example.demo2.model.Moderation;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class ModerationDao {

    public List<Moderation>getAll() throws  SQLException {
        List<Moderation> moderation = new ArrayList<Moderation>();
        String sql = "SELECT * FROM Moderation";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                moderation.add(new Moderation(
                        rs.getInt("id_moderation"),
                        rs.getInt(" id_utilisateur"),
                        rs.getInt("id_subreddit")

                ));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  moderation;
    }
    public Moderation findByid(int id){
        Moderation moderation = null;
        String sql = "SELECT * FROM Moderation WHERE id_message = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                moderation = new Moderation( rs.getInt("id_moderation"),
                        rs.getInt(" id_utilisateur"),
                        rs.getInt("id_subreddit")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moderation ;

    }
    public void add(Moderation moderation) {
        String sql = "INSERT INTO moderation (id_moderation, id_utilisateur,id_subreddit) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, moderation.getId_moderation());
            ps.setInt(2, moderation.getId_utulisateur());
            ps.setInt(4, Moderation.getId_subreddit());


            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Moderation moderation) throws SQLException {
        String sql = "UPDATE moderation SET id_moderation = ?,id_utilisateur = ?,id_subreddit = ? where id_moderation = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, moderation.getId_moderation());
            ps.setInt(2, moderation.getId_utulisateur());
            ps.setInt(4, moderation.getId_subreddit());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM moderation WHERE id_moderation = ?";
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
