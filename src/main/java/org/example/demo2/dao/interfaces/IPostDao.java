package org.example.demo2.dao.interfaces;

import org.example.demo2.model.Post;

import java.util.List;

public interface IPostDao {
    void add(Post post);
    List<Post> getAll();
    Post findById(int id);
    void update(Post post);
    void delete(int id);
    List<Post> getBySubreddit(int idSubreddit);
    List<Post> getByUtilisateur(int idUtilisateur);
    void updateScore(int id, int score);
}
