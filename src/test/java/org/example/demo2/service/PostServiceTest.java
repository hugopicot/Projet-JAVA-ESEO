package org.example.demo2.service;

import org.example.demo2.dao.*;
import org.example.demo2.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    private PostService postService;
    private UtilisateurService utilisateurService;
    private SubredditService subredditService;
    private PostDao postDao;
    private UtilisateurDao utilisateurDao;
    private SubredditDao subredditDao;
    private VoteDao voteDao;

    private List<Integer> postIds = new ArrayList<>();
    private List<Integer> userIds = new ArrayList<>();
    private List<Integer> subredditIds = new ArrayList<>();
    private List<Integer> voteIds = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        postService = new PostService();
        utilisateurService = new UtilisateurService();
        subredditService = new SubredditService();
        postDao = new PostDao();
        utilisateurDao = new UtilisateurDao();
        subredditDao = new SubredditDao();
        voteDao = new VoteDao();
    }

    @AfterEach
    public void tearDown() {
        for (Integer id : voteIds) {
            try { voteDao.delete(id); } catch (Exception e) { }
        }
        for (Integer id : postIds) {
            try { postDao.delete(id); } catch (Exception e) { }
        }
        for (Integer id : subredditIds) {
            try { subredditDao.delete(id); } catch (Exception e) { }
        }
        for (Integer id : userIds) {
            try { utilisateurDao.delete(id); } catch (Exception e) { }
        }
        postIds.clear();
        userIds.clear();
        subredditIds.clear();
        voteIds.clear();
    }

    @Test
    public void testCreerPost() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "post" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("postuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("testsubreddit" + uniqueId, "Description test").getId_subreddit();
        
        postService.creerPost("Titre test", "Contenu test", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getTousLesPosts();
        Post createdPost = posts.stream()
            .filter(p -> p.getTitre().equals("Titre test") && p.getId_utilisateur() == idUtilisateur)
            .findFirst().orElse(null);
        assertNotNull(createdPost, "Le post doit être créé");
        
        postIds.add(createdPost.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testCreerPostTitreVide() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "vide" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("videuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("videsub" + uniqueId, "Desc").getId_subreddit();
        
        assertThrows(IllegalArgumentException.class, () -> {
            postService.creerPost("", "Contenu", idUtilisateur, idSubreddit);
        });
        
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testCreerPostContenuVide() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "cont" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("contuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("contsub" + uniqueId, "Desc").getId_subreddit();
        
        assertThrows(IllegalArgumentException.class, () -> {
            postService.creerPost("Titre", "", idUtilisateur, idSubreddit);
        });
        
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testGetPostsParSubreddit() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "sub" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("subuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("subtest" + uniqueId, "Description").getId_subreddit();
        
        postService.creerPost("Post 1", "Contenu 1", idUtilisateur, idSubreddit);
        postService.creerPost("Post 2", "Contenu 2", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParSubreddit(idSubreddit);
        for (Post p : posts) {
            if (p.getTitre().equals("Post 1") || p.getTitre().equals("Post 2")) {
                postIds.add(p.getId_post());
            }
        }
        
        assertTrue(posts.stream().anyMatch(p -> p.getTitre().equals("Post 1")), "Post 1 doit exister");
        assertTrue(posts.stream().anyMatch(p -> p.getTitre().equals("Post 2")), "Post 2 doit exister");
        
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testGetPostsParUtilisateur() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "user" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("userpost" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("usersub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Mon Post", "Mon Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post createdPost = posts.stream().filter(p -> p.getTitre().equals("Mon Post")).findFirst().orElse(null);
        assertNotNull(createdPost, "Le post doit exister");
        
        postIds.add(createdPost.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testToggleLike() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "like" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("likeuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("likesub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Like", "Contenu Like", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Like")).findFirst().orElse(null);
        assertNotNull(post, "Le post doit exister");
        
        int initialScore = post.getScore();
        postService.toggleLike(post.getId_post(), idUtilisateur);
        
        Post updatedPost = postService.getPostById(post.getId_post());
        assertEquals(initialScore + 1, updatedPost.getScore(), "Le score doit augmenter");
        
        postService.toggleLike(post.getId_post(), idUtilisateur);
        Post finalPost = postService.getPostById(post.getId_post());
        assertEquals(initialScore, finalPost.getScore(), "Le score doit revenir à l'état initial");
        
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testADejALike() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "dejalike" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("dejalikeuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("dejalikesub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post DejaLike", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post DejaLike")).findFirst().orElse(null);
        assertNotNull(post);
        
        assertFalse(postService.aDejaLike(post.getId_post(), idUtilisateur), "Ne doit pas avoir de like au début");
        
        postService.toggleLike(post.getId_post(), idUtilisateur);
        
        assertTrue(postService.aDejaLike(post.getId_post(), idUtilisateur), "Doit avoir un like après toggle");
        
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testSupprimerPost() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "supp" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("suppuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("suppsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Supprimer", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Supprimer")).findFirst().orElse(null);
        assertNotNull(post);
        
        postService.supprimerPost(post.getId_post(), idUtilisateur);
        
        Post deletedPost = postService.getPostById(post.getId_post());
        assertNull(deletedPost, "Le post doit être supprimé");
        
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testSupprimerPostNonProprietaire() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String email1 = "prop" + uniqueId + "@test.com";
        String email2 = "autre" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("proprietaire" + uniqueId, email1, "password");
        utilisateurService.login(email1, "password");
        int idProp = utilisateurService.getUtilisateurConnecte().getId();
        
        utilisateurService.inscrire("autreuser" + uniqueId, email2, "password");
        
        int idSubreddit = subredditService.creerSubreddit("proptest" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Proprietaire", "Contenu", idProp, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idProp);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Proprietaire")).findFirst().orElse(null);
        assertNotNull(post);
        
        postService.supprimerPost(post.getId_post(), idProp + 999);
        
        Post stillExists = postService.getPostById(post.getId_post());
        assertNotNull(stillExists, "Le post ne doit pas être supprimé par un autre utilisateur");
        
        postIds.add(post.getId_post());
        userIds.add(idProp);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testGetTopPostsParSubreddit() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "top" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("topuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("topsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Score Bas", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post postBas = posts.stream().filter(p -> p.getTitre().equals("Post Score Bas")).findFirst().orElse(null);
        assertNotNull(postBas);
        
        postService.toggleLike(postBas.getId_post(), idUtilisateur);
        
        postService.creerPost("Post Score Haut", "Contenu 2", idUtilisateur, idSubreddit);
        
        posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post postHaut = posts.stream().filter(p -> p.getTitre().equals("Post Score Haut")).findFirst().orElse(null);
        assertNotNull(postHaut);
        
        List<Post> topPosts = postService.getTopPostsParSubreddit(idSubreddit, 1);
        assertTrue(topPosts.size() <= 1, "Devrait retourner au maximum 1 post");
        
        postIds.add(postBas.getId_post());
        postIds.add(postHaut.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }
}
