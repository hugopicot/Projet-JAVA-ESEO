package org.example.demo2.dao;

import org.example.demo2.model.Vote;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.util.*;
public class VoteDao {


    public List<Vote> getAll() throws SQLException {
        List<Vote> vote = new ArrayList<Vote>();
        String sql = "SELECT * FROM Vote";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vote.add(new Vote(
                        rs.getInt("id_vote"),
                        rs.getInt("type_vote"),
                        rs.getTimestamp("date_vote").toLocalDateTime(),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_post"),
                        rs.getInt("id_commentaire")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vote;
    }
    public Vote findByid(int id){
        Vote vote = null;
        String sql = "SELECT * FROM Vote WHERE id_vote = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                      vote = new Vote( rs.getInt("id_vote"),
                              rs.getInt("type_vote"),
                              rs.getTimestamp("date_vote").toLocalDateTime(),
                              rs.getInt("id_utilisateur"),
                              rs.getInt("id_post"),
                              rs.getInt("id_commentaire"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vote ;

    }

    public void add(Vote vote) {
        String sql = "INSERT INTO vote (id_vote,type_vote,date_vote,id_utilisateur,id_post,id_commentaire) VALUES (?, ?, ?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vote.getId_vote());
            ps.setInt(2, vote.getType_vote());
            ps.setTimestamp(4, Timestamp.valueOf(vote.getDate_vote()));
            ps.setInt(3, vote.getId_post());
            ps.setInt(3, vote.getId_utilisateur());
            ps.setInt(3, vote.getId_commentaire());





            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Vote vote) throws SQLException {
        String sql = "UPDATE vote SET id_vote = ?,type_vote = ?,date_vote = ?,id_utilisateur = ?,id_post =?,id_commentaire =? where id_vote = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, vote.getId_vote());
            ps.setInt(2, vote.getType_vote());
            ps.setTimestamp(4, Timestamp.valueOf(vote.getDate_vote()));
            ps.setInt(3, vote.getId_post());
            ps.setInt(3, vote.getId_utilisateur());
            ps.setInt(3, vote.getId_commentaire());

            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM Vote  WHERE  id_vote = ?";
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
