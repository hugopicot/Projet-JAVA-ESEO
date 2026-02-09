package org.example.demo2.dao;

/**
 * Data Access Object pour les posts.
 */

import org.example.demo2.model.Post;
import org.example.demo2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PostDao {
//    Voy a hacer un mini dao solamente para testear el UI, luego lo borro para evitar merge conflicts
// TO DELETE TO AVOID MERGE CONFLICTS. JUST TESTING
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post";

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                posts.add(new Post(
                        rs.getInt("id_post"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getInt("score"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_subreddit")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return posts;
    }

}
