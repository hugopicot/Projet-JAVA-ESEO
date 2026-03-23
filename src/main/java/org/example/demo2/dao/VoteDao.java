package org.example.demo2.dao;

import org.example.demo2.model.Vote;
import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoteDao {

    public List<Vote> getAll() {
        List<Vote> vote = new ArrayList<>();
        String sql = "SELECT * FROM vote";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vote.add(mapResultSetToVote(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vote;
    }

    public Vote findById(int id) {
        Vote vote = null;
        String sql = "SELECT * FROM vote WHERE id_vote = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                vote = mapResultSetToVote(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vote;
    }

    /**
     * Vérifie si un utilisateur a déjà voté pour un post donné.
     * @return L'objet Vote s'il existe, null sinon.
     */
    public Vote findByUserAndPost(int idUtilisateur, int idPost) {
        String sql = "SELECT * FROM vote WHERE id_utilisateur = ? AND id_post = ? AND id_commentaire IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idPost);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToVote(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ajoute un vote.
     */
    public void add(Vote vote) {
        String sql = "INSERT INTO vote (type_vote, date_vote, id_utilisateur, id_post, id_commentaire) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, vote.getType_vote());
            ps.setTimestamp(2, Timestamp.valueOf(vote.getDate_vote()));
            ps.setInt(3, vote.getId_utilisateur());
            ps.setInt(4, vote.getId_post());
            if (vote.getId_commentaire() != null) {
                ps.setInt(5, vote.getId_commentaire());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                vote.setId_vote(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Vote vote) {
        String sql = "UPDATE vote SET type_vote = ?, date_vote = ?, id_utilisateur = ?, id_post =?, id_commentaire =? WHERE id_vote = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vote.getType_vote());
            ps.setTimestamp(2, Timestamp.valueOf(vote.getDate_vote()));
            ps.setInt(3, vote.getId_utilisateur());
            ps.setInt(4, vote.getId_post());
            if (vote.getId_commentaire() != null) {
                ps.setInt(5, vote.getId_commentaire());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, vote.getId_vote());

            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Supprime un vote par son ID.
     */
    public void delete(int idVote) {
        String sql = "DELETE FROM vote WHERE id_vote = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVote);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vote mapResultSetToVote(ResultSet rs) throws SQLException {
        Integer idCom = rs.getInt("id_commentaire");
        if (rs.wasNull()) idCom = null;

        return new Vote(
                rs.getInt("id_vote"),
                rs.getInt("type_vote"),
                rs.getTimestamp("date_vote").toLocalDateTime(),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_post"),
                idCom
        );
    }
}
