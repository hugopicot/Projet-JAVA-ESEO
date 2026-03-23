package org.example.demo2.service;
import org.example.demo2.dao.PostDao;
import org.example.demo2.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des posts (CRUD, likes).
 */





import java.sql.SQLException;
import java.util.List;

public class PostService {

    private PostDao postDao = new PostDao();

    // Créer un nouveau post
    public void createPost(Post post) {
        postDao.add(post); // utilise la méthode add() du DAO pour la DB
    }

    // Récupérer tous les posts depuis la base
    public List<Post> getAllPosts() {
        try {
            return PostDao.getAll(); // méthode statique dans ton DAO
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Optionnel : update
    public void updatePost(Post post) {
        try {
            postDao.update(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Optionnel : delete
    public void deletePost(int id_post) {
        postDao.delete(id_post);
    }
}