package org.example.demo2.service;

import org.example.demo2.dao.PostDao;
import org.example.demo2.dao.VoteDao;
import org.example.demo2.model.Post;
import org.example.demo2.model.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostDao postDao;
    private final VoteDao voteDao;

    public PostService() {
        this.postDao = new PostDao();
        this.voteDao = new VoteDao();
    }

    public void createPost(Post post) {
        postDao.add(post);
    }
    
    public List<Post> getAllPosts() {
        return postDao.getAll();
    }

    public void updatePost(Post post) {
        postDao.update(post);
    }

    public void deletePost(int id_post) {
        postDao.delete(id_post);
    }

    public void creerPost(String titre, String contenu, int idUtilisateur, int idSubreddit) {
        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu ne peut pas être vide.");
        }
        
        Post post = new Post(titre, contenu, idUtilisateur, idSubreddit);
        postDao.add(post);
    }

    public List<Post> getTousLesPosts() {
        return postDao.getAll();
    }

    public void toggleLike(int idPost, int idUtilisateur) {
        Vote voteExistant = voteDao.findByUserAndPost(idUtilisateur, idPost);
        Post post = postDao.findById(idPost);

        if (post == null) return;

        if (voteExistant != null) {
            voteDao.delete(voteExistant.getId_vote());
            postDao.updateScore(idPost, post.getScore() - 1);
        } else {
            // type_vote = 1 pour LIKE
            Vote nouveauVote = new Vote(1, idUtilisateur, idPost, null);
            voteDao.add(nouveauVote);
            postDao.updateScore(idPost, post.getScore() + 1);
        }
    }

    public boolean aDejaLike(int idPost, int idUtilisateur) {
        return voteDao.findByUserAndPost(idUtilisateur, idPost) != null;
    }

    public void supprimerPost(int idPost, int idUtilisateur) {
        Post post = postDao.findById(idPost);
        if (post != null && post.getId_utilisateur() == idUtilisateur) {
            postDao.delete(idPost);
        }
    }

    public List<Post> getPostsParSubreddit(int idSubreddit) {
        return postDao.getBySubreddit(idSubreddit);
    }

    public List<Post> getTopPostsParSubreddit(Integer idSubreddit, int limit) {
        List<Post> allPosts;
        if (idSubreddit == null) {
            allPosts = postDao.getAll();
        } else {
            allPosts = postDao.getBySubreddit(idSubreddit);
        }

        return allPosts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Post> getPostsParUtilisateur(int idUtilisateur) {
        return postDao.getByUtilisateur(idUtilisateur);
    }

    public Post getPostById(int idPost) {
        return postDao.findById(idPost);
    }
}
