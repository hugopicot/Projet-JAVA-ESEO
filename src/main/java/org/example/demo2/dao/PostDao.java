package org.example.demo2.dao;

/**
 * Data Access Object pour les posts.
 */



import org.example.demo2.model.Post;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
public class PostDao {
    public List<Post>getAll() throws  SQLException {
        List<Post> post = new ArrayList<Post>();
        String sql = "SELECT * FROM Post";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                post.add(new Post(
                        rs.getInt("id_post"),
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
                post = new Post( rs.getInt("id_post"),
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
        String sql = "INSERT INTO post (id_post,titre,contenu,date_creation,score,id_utilisateur,id_subreddit) VALUES (?, ?, ?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, post.getId_post());
            ps.setString(2, post.getTitre());
            ps.setString(3, post.getContenu());
            ps.setTimestamp(4, Timestamp.valueOf(post.getDate_creation()));
            ps.setInt(5, post.getScore());
            ps.setInt(6, post.getId_utilisateur());
            ps.setInt(7, post.getId_subreddit());



            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Post post) throws SQLException {
        String sql = "UPDATE post SET id_post = ?,titre = ?,contenu = ? ,date_creation = ?, score = ?,id_utilisateur = ?,id_subreddit = ? where id_moderation = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, post.getId_post());
            ps.setString(2, post.getTitre());
            ps.setString(3, post.getContenu());
            ps.setTimestamp(4, Timestamp.valueOf(post.getDate_creation()));
            ps.setInt(5, post.getScore());
            ps.setInt(6, post.getId_utilisateur());
            ps.setInt(7, post.getId_subreddit());
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
