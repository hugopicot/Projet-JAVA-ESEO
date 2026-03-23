package org.example.demo2.dao;

/**
 * Data Access Object pour les posts.
 */

import org.example.demo2.model.Post;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.util.*;


public class PostDao {

    public static List<Post>getAll() throws  SQLException {
        List<Post> post = new ArrayList<Post>();
        String sql = "SELECT * FROM Post";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                post.add(new Post(
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_subreddit")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  post;
    }


    public Post findByid(int id){
        Post post = null;
        String sql = "SELECT * FROM Post WHERE id_post = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                post = new Post(

                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_subreddit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post ;

    }
    public void add(Post post) {
        String sql = "INSERT INTO post (titre,contenu,date_creation,score,id_utilisateur,id_subreddit) VALUES (?, ?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setString(1, post.getTitre());
            ps.setString(2, post.getContenu());
            ps.setTimestamp(3, Timestamp.valueOf(post.getDate_creation()));
            ps.setInt(4, post.getScore());
            ps.setInt(5, post.getId_utilisateur());
            ps.setInt(6, post.getId_subreddit());



            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Post post) throws SQLException {
        String sql = "UPDATE post SET titre = ?,contenu = ? ,date_creation = ?, score = ?,id_utilisateur = ?,id_subreddit = ? where id_post = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){


            ps.setString(1, post.getTitre());
            ps.setString(2, post.getContenu());
            ps.setTimestamp(3, Timestamp.valueOf(post.getDate_creation()));
            ps.setInt(4, post.getScore());
            ps.setInt(5, post.getId_utilisateur());
            ps.setInt(6, post.getId_subreddit());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM post WHERE id_post = ?";
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
