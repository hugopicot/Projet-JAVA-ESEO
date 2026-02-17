package org.example.demo2.dao;

import org.example.demo2.dao.CommentaireDao;
import org.example.demo2.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CommentaireDao {
    public CommentaireDao(String contenu, int id_commentaire, LocalDateTime date_creation,int score,int id_utilisateur,int id_post, int  id_parent) {
    }

    public List<CommentaireDao>getAll() throws SQLException {
        List<CommentaireDao> commentaire = new ArrayList<CommentaireDao>();
        String sql = "SELECT * FROM commentaire";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                commentaire.add(new CommentaireDao(rs.getString("contenu"),

                        rs.getInt("id_commentaire"),
                        rs.getString("nom"),
                        rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_post"),
                        rs.getInt("id_parent");
            }
        }
    }
}

