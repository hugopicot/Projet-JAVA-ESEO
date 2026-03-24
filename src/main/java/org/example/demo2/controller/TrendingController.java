package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Post;
import org.example.demo2.service.PostService;
import org.example.demo2.service.UtilisateurService;

import java.util.List;
import java.util.function.Consumer;


public class TrendingController {

    private static final int MAX_TRENDING_POSTS = 3;
    
    private final PostService postService;
    private final UtilisateurService utilisateurService;
    
    private VBox trendingContainer;
    private Integer currentSubredditId = null;
    private int utilisateurConnecteId;
    private Consumer<Post> onPostClick;
    
    public TrendingController(PostService postService, UtilisateurService utilisateurService) {
        this.postService = postService;
        this.utilisateurService = utilisateurService;
    }
    
    public void setTrendingContainer(VBox trendingContainer) {
        this.trendingContainer = trendingContainer;
    }
    
    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }
    
    public void setOnPostClick(Consumer<Post> callback) {
        this.onPostClick = callback;
    }
    
    public void setCurrentSubredditId(Integer subredditId) {
        this.currentSubredditId = subredditId;
    }
    
    public void clearCurrentSubreddit() {
        this.currentSubredditId = null;
    }

    public void chargerTrendingPosts() {
        if (trendingContainer == null) return;
        
        trendingContainer.getChildren().clear();
        
        List<Post> topPosts = postService.getTopPostsParSubreddit(currentSubredditId, MAX_TRENDING_POSTS);

        if (topPosts.isEmpty()) {
            Label vide = new Label("Pas encore de top posts.");
            vide.setStyle("-fx-text-fill: #888; -fx-padding: 10;");
            trendingContainer.getChildren().add(vide);
        } else {
            for (Post post : topPosts) {
                VBox card = createTrendingCard(post);
                trendingContainer.getChildren().add(card);
            }
        }
    }
    
    private VBox createTrendingCard(Post post) {
        VBox card = new VBox(15);
        card.getStyleClass().add("related-post-item");
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand;");
        
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        
        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            userIcon.setFitWidth(20);
            userIcon.setPreserveRatio(true);
            header.getChildren().add(userIcon);
        } catch (Exception e) {
        }
        
        Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
        userLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #555;");
        header.getChildren().add(userLabel);
        
        Label titre = new Label(post.getTitre());
        titre.setWrapText(true);
        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
        
        HBox footer = createScoreFooter(post);
        
        card.getChildren().addAll(header, titre, footer);
        
        if (onPostClick != null) {
            card.setOnMouseClicked(e -> onPostClick.accept(post));
        }
        
        return card;
    }
    
    private HBox createScoreFooter(Post post) {
        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER_LEFT);
        
        try {
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/like_svg.png")));
            likeIcon.setFitWidth(16);
            likeIcon.setPreserveRatio(true);
            footer.getChildren().add(likeIcon);
        } catch (Exception e) {
        }
        
        Label scoreLabel = new Label(String.valueOf(post.getScore()));
        scoreLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");
        footer.getChildren().add(scoreLabel);
        
        return footer;
    }
    
    private String getNomUtilisateur(int idUtilisateur) {
        if (idUtilisateur == utilisateurConnecteId && utilisateurService.estAuthentifie()) {
            return utilisateurService.getUtilisateurConnecte().getPseudo();
        }
        var user = utilisateurService.getUtilisateurParId(idUtilisateur);
        if (user != null) {
            return user.getPseudo();
        }
        return "Utilisateur #" + idUtilisateur;
    }
}
