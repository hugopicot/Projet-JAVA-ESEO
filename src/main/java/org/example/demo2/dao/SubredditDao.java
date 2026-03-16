package org.example.demo2.dao;

import org.example.demo2.model.Subreddit;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class SubredditDao {
    public List<Subreddit>getAll() throws  SQLException {
        List<Subreddit> subreddits = new ArrayList<Subreddit>();
        String sql = "SELECT * FROM Subreddit";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                subreddits.add(new Subreddit(
                        rs.getInt("id_subreddit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                         rs.getTimestamp("date_creation").toLocalDateTime())
                );
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  subreddits;
    }
    public Subreddit findByid(int id){
        Subreddit subreddit = null;
        String sql = "SELECT * FROM Subreddit WHERE id_subreddit = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                subreddit= new Subreddit( rs.getInt("id_subreddit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getTimestamp("date_creation").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subreddit ;

    }
    public void add(Subreddit subreddit) {
        String sql = "INSERT INTO subreddit (id_subreddit,nom,description,date_creation) VALUES (?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subreddit.getId_subreddit());
            ps.setString(2, subreddit.getNom());
            ps.setString(3, subreddit.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(subreddit.getDate_creation()));



            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Subreddit subreddit) throws SQLException {
        String sql = "UPDATE subreddit  SET id_subreddit = ?,nom = ?,description = ?,date_creation = ?  where id_subreddit = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, subreddit.getId_subreddit());
            ps.setString(2, subreddit.getNom());
            ps.setString(3, subreddit.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(subreddit.getDate_creation()));

            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM Subreddit  WHERE id_subreddit = ?";
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
