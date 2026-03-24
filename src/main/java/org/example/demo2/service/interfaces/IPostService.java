package org.example.demo2.service.interfaces;

import org.example.demo2.model.Post;

import java.util.List;

public interface IPostService {
    void createPost(Post post);
    List<Post> getAllPosts();
    void updatePost(Post post);
    void deletePost(int id);
    void creerPost(String titre, String contenu, int idUtilisateur, int idSubreddit);
    List<Post> getTousLesPosts();
    List<Post> getPostsBySubreddit(int idSubreddit);
    List<Post> getPostsByUtilisateur(int idUtilisateur);
}
