package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Post;
import org.example.demo2.model.Subreddit;
import org.example.demo2.service.AbonnementService;
import org.example.demo2.service.PostService;
import org.example.demo2.service.UtilisateurService;

import java.util.List;
import java.util.function.Consumer;


public class FeedController {

    private final PostService postService;
    private final AbonnementService abonnementService;
    private final UtilisateurService utilisateurService;
    private final PostCardController postCardController;
    
    private VBox postsContainer;
    private ComboBox<Subreddit> subredditComboBox;
    private Label postingToLabel;
    private Subreddit subredditActuel = null;
    
    private int utilisateurConnecteId;
    private Runnable onLoginRequired;
    private Runnable onRefreshNeeded;
    private Consumer<Post> onPostClick;
    private Runnable onSubredditChanged;
    
    public FeedController(PostService postService,
                          AbonnementService abonnementService,
                          UtilisateurService utilisateurService,
                          PostCardController postCardController) {
        this.postService = postService;
        this.abonnementService = abonnementService;
        this.utilisateurService = utilisateurService;
        this.postCardController = postCardController;
    }
    
    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
        postCardController.setUtilisateurConnecteId(id);
    }
    
    public void setPostsContainer(VBox postsContainer) {
        this.postsContainer = postsContainer;
    }
    
    public void setSubredditComboBox(ComboBox<Subreddit> subredditComboBox) {
        this.subredditComboBox = subredditComboBox;
    }
    
    public void setPostingToLabel(Label postingToLabel) {
        this.postingToLabel = postingToLabel;
    }
    
    public void setOnLoginRequired(Runnable callback) {
        this.onLoginRequired = callback;
        postCardController.setOnLoginRequired(callback);
    }
    
    public void setOnRefreshNeeded(Runnable callback) {
        this.onRefreshNeeded = callback;
    }
    
    public void setOnPostClick(Consumer<Post> callback) {
        this.onPostClick = callback;
        postCardController.setOnPostClick(callback);
    }
    
    public void setOnSubredditChanged(Runnable callback) {
        this.onSubredditChanged = callback;
    }
    
    public Subreddit getSubredditActuel() {
        return subredditActuel;
    }
    
    public void setSubredditActuel(Subreddit subreddit) {
        this.subredditActuel = subreddit;
    }
    
    public void clearSubredditActuel() {
        this.subredditActuel = null;
    }

    public void chargerPosts() {
        if (postsContainer == null) return;
        
        postsContainer.getChildren().clear();
        List<Post> posts;

        if (subredditActuel == null) {
            if (subredditComboBox != null) {
                subredditComboBox.setVisible(true);
                subredditComboBox.setManaged(true);
            }
            if (postingToLabel != null) {
                postingToLabel.setVisible(false);
                postingToLabel.setManaged(false);
            }
            posts = postService.getTousLesPosts();
        } else {
            if (subredditComboBox != null) {
                subredditComboBox.setVisible(false);
                subredditComboBox.setManaged(false);
            }
            if (postingToLabel != null) {
                postingToLabel.setText("r/" + subredditActuel.getNom());
                postingToLabel.setVisible(true);
                postingToLabel.setManaged(true);
            }
            
            HBox header = createSubredditHeader();
            postsContainer.getChildren().add(header);

            posts = postService.getPostsParSubreddit(subredditActuel.getId_subreddit());
        }

        if (posts.isEmpty()) {
            String msg = subredditActuel == null ? "Aucun post pour le moment." : "Aucun post dans r/" + subredditActuel.getNom();
            Label vide = new Label(msg);
            vide.setStyle("-fx-text-fill: #888; -fx-font-style: italic; -fx-padding: 20;");
            postsContainer.getChildren().add(vide);
        } else {
            for (Post post : posts) {
                VBox carte = postCardController.createPostCard(post);
                postsContainer.getChildren().add(carte);
            }
        }
    }
    
    private HBox createSubredditHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #f1f2f6; -fx-padding: 15; -fx-background-radius: 10;");
        
        Label title = new Label("r/" + subredditActuel.getNom());
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        
        boolean estAbonne = abonnementService.estAbonne(utilisateurConnecteId, subredditActuel.getId_subreddit());
        Button joinBtn = new Button(estAbonne ? "Joined" : "Join");
        joinBtn.getStyleClass().add(estAbonne ? "make-post-button-white" : "make-post-button");
        
        joinBtn.setOnAction(e -> handleJoinToggle());
        
        header.getChildren().addAll(title, joinBtn);
        return header;
    }
    
    private void handleJoinToggle() {
        if (!utilisateurService.estAuthentifie()) {
            if (onLoginRequired != null) {
                onLoginRequired.run();
            }
            return;
        }
        
        abonnementService.toggleAbonnement(utilisateurConnecteId, subredditActuel.getId_subreddit());
        
        if (onSubredditChanged != null) {
            onSubredditChanged.run();
        }
        
        chargerPosts();
    }
}
