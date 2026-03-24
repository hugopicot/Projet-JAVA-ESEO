package org.example.demo2.dao;

import org.example.demo2.dao.interfaces.IMessageDao;
import org.example.demo2.model.Message;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class MessageDao implements IMessageDao {

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getByExpediteur(int idExpediteur) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE id_expediteur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idExpediteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getByDestinataire(int idDestinataire) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE id_utilisateur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDestinataire);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void updateLu(int id) {
        String sql = "UPDATE message SET lu = TRUE WHERE id_message = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Message message) {
        String sql = "INSERT INTO message (contenu, date_envoi, lu, id_expediteur, id_utilisateur) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getContenu());
            ps.setTimestamp(2, Timestamp.valueOf(message.getDate_envoi()));
            ps.setBoolean(3, message.getLu());
            ps.setInt(4, message.getId_expediteur());
            ps.setInt(5, message.getId_destinataire());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM message WHERE id_message = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("id_message"),
                rs.getInt("contenu"),
                rs.getBoolean("lu"),
                rs.getTimestamp("date_envoi").toLocalDateTime(),
                rs.getInt("id_expediteur"),
                rs.getInt("id_utilisateur")
        );
    }
}
