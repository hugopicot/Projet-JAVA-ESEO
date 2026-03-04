package org.example.demo2.service;

import org.example.demo2.util.DatabaseConnection;

import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class MessageService {


            public static void envoyerMessage(
                    String contenu,
                    int expediteur,
                    int destinataire){

                String sql = """
        INSERT INTO message
        (contenu,date_envoi,lu,id_expediteur,id_destinataire)
        VALUES (?, ?, 0, ?, ?)
        """;

                try(Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)){

                    ps.setString(1, contenu);
                    ps.setObject(2, LocalDateTime.now());
                    ps.setInt(3, expediteur);
                    ps.setInt(4, destinataire);

                    ps.executeUpdate();

                    System.out.println("Message envoyé ");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }



            public static void afficherMessages(int userId){

                String sql = """
        SELECT * FROM message
        WHERE id_destinataire = ?
        ORDER BY date_envoi DESC
        """;

                try(Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)){

                    ps.setInt(1, userId);

                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){

                        System.out.println("ID: " + rs.getInt("id_message"));
                        System.out.println("Contenu: " + rs.getString("contenu"));
                        System.out.println("Lu: " + rs.getBoolean("lu"));
                        System.out.println("Date: " + rs.getTimestamp("date_envoi"));
                        System.out.println("--------------");
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }



            public static void supprimer(int id){

                String sql =
                        "DELETE FROM message WHERE id_message=?";

                try(Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)){

                    ps.setInt(1, id);
                    ps.executeUpdate();

                    System.out.println("Message supprimé ");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }



            public static void main(String[] args){


                envoyerMessage("Bonjour !", 1, 2);


                afficherMessages(2);


                supprimer(3);
            }
        }



















