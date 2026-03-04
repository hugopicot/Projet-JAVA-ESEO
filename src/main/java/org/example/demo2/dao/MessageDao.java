package org.example.demo2.dao;

import org.example.demo2.model.Message;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDao {




    public List<Message>getAll() throws  SQLException {
        List<Message> messages = new ArrayList<Message>();
        String sql = "SELECT * FROM Message";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                messages.add(new Message(
                        rs.getInt("id_message"),
                        rs.getInt("contenu"),
                        rs.getBoolean("lu"),
                        rs.getTimestamp("date_envoi").toLocalDateTime(),
                        rs.getInt("id_expediteur"),
                        rs.getInt("id_utilisateur")
                       ));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  messages;
    }
    public Message findByid(int id){
        Message message = null;
        String sql = "SELECT * FROM Message WHERE id_message = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                message = new Message( rs.getInt("id_message"),
                        rs.getInt("contenu"),
                        rs.getBoolean("lu"),
                        rs.getTimestamp("date_envoi").toLocalDateTime(),
                        rs.getInt("id_expediteur"),
                        rs.getInt("id_utilisateur")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;

    }
    public void add(Message message) {
        String sql = "INSERT INTO message(id_message, contenu, date_envoi,lu, id_expediteur, id_utilisateur) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, message.getId_message());
            ps.setInt(2, message.getContenu());
            ps.setTimestamp(3, Timestamp.valueOf(message.getDate_envoi()));
            ps.setBoolean(4, message.getLu());
            ps.setInt(5, message.getId_expediteur());
            ps.setInt(6, message.getId_destinataire());

            ps.executeUpdate();

        } catch (SQLException  e) {
            e.printStackTrace();

        }
    }
    public void update(Message message) throws SQLException {
        String sql = "UPDATE message SET id_message = ?,contenu = ?,date_envoi = ?,lu = ?,id_expediteur = ?,id_utilisateur = ? where id_message = ?";
        try ( Connection conn = DatabaseConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, message.getId_message());
            ps.setInt(2, message.getContenu());
            ps.setTimestamp(3, Timestamp.valueOf(message.getDate_envoi()));
            ps.setBoolean(4, message.getLu());
            ps.setInt(5, message.getId_expediteur());
            ps.setInt(6, message.getId_destinataire());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void delete(int id){
        String sql = "DELETE FROM message WHERE id_message = ?";
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
