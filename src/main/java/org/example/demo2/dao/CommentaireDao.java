package org.example.demo2.dao;

import org.example.demo2.dao.interfaces.ICommentaireDao;
import org.example.demo2.model.Commentaire;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireDao implements ICommentaireDao {

    public List<Commentaire> findByPost(int idPost) {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire WHERE id_post = ? ORDER BY date_creation ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPost);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                commentaires.add(mapResultSetToCommentaire(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }

    public int countByPost(int idPost) {
        String sql = "SELECT COUNT(*) FROM commentaire WHERE id_post = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPost);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void add(Commentaire com) {
        String sql = "INSERT INTO commentaire (contenu, date_creation, score, id_utilisateur, id_post, id_parent) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, com.getContenu());
            ps.setTimestamp(2, Timestamp.valueOf(com.getDate_creation()));
            ps.setInt(3, com.getScore());
            ps.setInt(4, com.getId_utilisateur());
            ps.setInt(5, com.getId_post());
            if (com.getId_parent() != null && com.getId_parent() > 0) {
                ps.setInt(6, com.getId_parent());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                com.setId_commentaire(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM commentaire WHERE id_commentaire = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Commentaire mapResultSetToCommentaire(ResultSet rs) throws SQLException {
        Integer idParent = rs.getInt("id_parent");
        if (rs.wasNull()) idParent = null;

        return new Commentaire(
                rs.getInt("id_commentaire"),
                rs.getString("contenu"),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                rs.getInt("score"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_post"),
                idParent
        );
    }
}