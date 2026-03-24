package org.example.demo2.service;

import org.example.demo2.dao.*;
import org.example.demo2.model.Commentaire;
import org.example.demo2.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommentaireServiceTest {

    private CommentaireService commentaireService;
    private PostService postService;
    private UtilisateurService utilisateurService;
    private SubredditService subredditService;
    private CommentaireDao commentaireDao;
    private PostDao postDao;
    private UtilisateurDao utilisateurDao;
    private SubredditDao subredditDao;
    private VoteDao voteDao;

    private List<Integer> commentaireIds = new ArrayList<>();
    private List<Integer> postIds = new ArrayList<>();
    private List<Integer> userIds = new ArrayList<>();
    private List<Integer> subredditIds = new ArrayList<>();
    private List<Integer> voteIds = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        commentaireService = new CommentaireService();
        postService = new PostService();
        utilisateurService = new UtilisateurService();
        subredditService = new SubredditService();
        commentaireDao = new CommentaireDao();
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
        for (Integer id : commentaireIds) {
            try { commentaireDao.delete(id); } catch (Exception e) { }
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
        commentaireIds.clear();
        postIds.clear();
        userIds.clear();
        subredditIds.clear();
        voteIds.clear();
    }

    @Test
    public void testAjouterCommentaire() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "com" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("comuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("comsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Commentaire", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Commentaire")).findFirst().orElse(null);
        assertNotNull(post);
        
        Commentaire commentaire = commentaireService.ajouterCommentaire("Mon commentaire", idUtilisateur, post.getId_post(), null);
        
        assertNotNull(commentaire, "Le commentaire doit être créé");
        assertEquals("Mon commentaire", commentaire.getContenu(), "Le contenu doit correspondre");
        
        commentaireIds.add(commentaire.getId_commentaire());
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testAjouterCommentaireVide() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "videcom" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("videcomuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("videcomsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Vide", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Vide")).findFirst().orElse(null);
        assertNotNull(post);
        
        assertThrows(IllegalArgumentException.class, () -> {
            commentaireService.ajouterCommentaire("", idUtilisateur, post.getId_post(), null);
        });
        
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testAjouterCommentaireNull() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "nullcom" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("nullcomuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("nullcomsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Null", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Null")).findFirst().orElse(null);
        assertNotNull(post);
        
        assertThrows(IllegalArgumentException.class, () -> {
            commentaireService.ajouterCommentaire(null, idUtilisateur, post.getId_post(), null);
        });
        
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testAjouterReponseCommentaire() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "rep" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("repuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("repsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Reponse", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Reponse")).findFirst().orElse(null);
        assertNotNull(post);
        
        Commentaire commentaireParent = commentaireService.ajouterCommentaire("Commentaire parent", idUtilisateur, post.getId_post(), null);
        
        Commentaire reponse = commentaireService.ajouterCommentaire("Réponse au commentaire", idUtilisateur, post.getId_post(), commentaireParent.getId_commentaire());
        
        assertNotNull(reponse, "La réponse doit être créée");
        assertEquals(commentaireParent.getId_commentaire(), reponse.getId_parent(), "Le parent doit correspondre");
        
        commentaireIds.add(commentaireParent.getId_commentaire());
        commentaireIds.add(reponse.getId_commentaire());
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testGetCommentairesPost() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "getcom" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("getcomuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("getcomsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Get Com", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Get Com")).findFirst().orElse(null);
        assertNotNull(post);
        
        Commentaire com1 = commentaireService.ajouterCommentaire("Commentaire 1", idUtilisateur, post.getId_post(), null);
        Commentaire com2 = commentaireService.ajouterCommentaire("Commentaire 2", idUtilisateur, post.getId_post(), null);
        
        List<Commentaire> commentaires = commentaireService.getCommentairesPost(post.getId_post());
        assertTrue(commentaires.size() >= 2, "Le post doit avoir au moins 2 commentaires");
        
        commentaireIds.add(com1.getId_commentaire());
        commentaireIds.add(com2.getId_commentaire());
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testGetNombreCommentaires() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "nbcom" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("nbcomuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("nbcomsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Nb Com", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Nb Com")).findFirst().orElse(null);
        assertNotNull(post);
        
        int initialCount = commentaireService.getNombreCommentaires(post.getId_post());
        
        Commentaire com1 = commentaireService.ajouterCommentaire("Commentaire A", idUtilisateur, post.getId_post(), null);
        Commentaire com2 = commentaireService.ajouterCommentaire("Commentaire B", idUtilisateur, post.getId_post(), null);
        
        int finalCount = commentaireService.getNombreCommentaires(post.getId_post());
        assertEquals(initialCount + 2, finalCount, "Le nombre de commentaires doit avoir augmenté de 2");
        
        commentaireIds.add(com1.getId_commentaire());
        commentaireIds.add(com2.getId_commentaire());
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }

    @Test
    public void testSupprimerCommentaire() {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "suppcom" + uniqueId + "@test.com";
        
        utilisateurService.inscrire("suppcomuser" + uniqueId, uniqueEmail, "password");
        utilisateurService.login(uniqueEmail, "password");
        
        int idUtilisateur = utilisateurService.getUtilisateurConnecte().getId();
        int idSubreddit = subredditService.creerSubreddit("suppcomsub" + uniqueId, "Desc").getId_subreddit();
        
        postService.creerPost("Post Supp Com", "Contenu", idUtilisateur, idSubreddit);
        
        List<Post> posts = postService.getPostsParUtilisateur(idUtilisateur);
        Post post = posts.stream().filter(p -> p.getTitre().equals("Post Supp Com")).findFirst().orElse(null);
        assertNotNull(post);
        
        Commentaire commentaire = commentaireService.ajouterCommentaire("Commentaire à supprimer", idUtilisateur, post.getId_post(), null);
        int idCommentaire = commentaire.getId_commentaire();
        
        commentaireService.supprimerCommentaire(idCommentaire);
        
        List<Commentaire> commentaires = commentaireService.getCommentairesPost(post.getId_post());
        boolean found = commentaires.stream().anyMatch(c -> c.getId_commentaire() == idCommentaire);
        assertFalse(found, "Le commentaire doit être supprimé");
        
        postIds.add(post.getId_post());
        userIds.add(idUtilisateur);
        subredditIds.add(idSubreddit);
    }
}
