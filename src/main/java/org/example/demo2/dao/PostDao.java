package org.example.demo2.dao;

/**
 * Data Access Object pour les posts.
 */

import org.example.demo2.model.Post;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PostDao {

    /**
     * Récupère TOUS les posts, triés du plus récent au plus ancien.
     */
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post ORDER BY date_creation DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Récupère un post par son ID.
     */
    public Post findById(int id) {
        Post post = null;
        String sql = "SELECT * FROM post WHERE id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                post = mapResultSetToPost(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Récupère tous les posts d'un subreddit donné.
     */
    public List<Post> getBySubreddit(int idSubreddit) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post WHERE id_subreddit = ? ORDER BY date_creation DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSubreddit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Récupère tous les posts d'un utilisateur donné.
     */
    public List<Post> getByUtilisateur(int idUtilisateur) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post WHERE id_utilisateur = ? ORDER BY date_creation DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Ajoute un nouveau post en BDD.
     * On ne passe PAS l'id_post (AUTO_INCREMENT).
     */
    public void add(Post post) {
        String sql = "INSERT INTO post (titre, contenu, date_creation, score, id_utilisateur, id_subreddit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getTitre());
            ps.setString(2, post.getContenu());
            ps.setTimestamp(3, Timestamp.valueOf(post.getDate_creation()));
            ps.setInt(4, post.getScore());
            ps.setInt(5, post.getId_utilisateur());
            ps.setInt(6, post.getId_subreddit());

            ps.executeUpdate();

            // Récupérer l'ID généré par la BDD
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                post.setId_post(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour un post existant.
     */
    public void update(Post post) {
        String sql = "UPDATE post SET titre = ?, contenu = ?, score = ?, id_subreddit = ? WHERE id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getTitre());
            ps.setString(2, post.getContenu());
            ps.setInt(3, post.getScore());
            ps.setInt(4, post.getId_subreddit());
            ps.setInt(5, post.getId_post());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un post par son ID.
     */
    public void delete(int id) {
        String sql = "DELETE FROM post WHERE id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour le score d'un post (upvote / downvote).
     */
    public void updateScore(int idPost, int nouveauScore) {
        String sql = "UPDATE post SET score = ? WHERE id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nouveauScore);
            ps.setInt(2, idPost);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit un ResultSet en objet Post (évite la duplication de code).
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id_post"),
                rs.getString("titre"),
                rs.getString("contenu"),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                rs.getInt("score"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_subreddit")
        );
    }
}
