package org.example.demo2.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.demo2.model.Subreddit;
import org.example.demo2.service.PostService;
import org.example.demo2.service.SubredditService;
import org.example.demo2.service.UtilisateurService;

import java.util.List;
import java.util.function.Consumer;


public class CreatePostController {

    private final PostService postService;
    private final SubredditService subredditService;
    private final UtilisateurService utilisateurService;
    
    private TextField postTitleField;
    private TextArea postContentArea;
    private ComboBox<Subreddit> subredditComboBox;
    private Subreddit selectedSubreddit;
    
    private int utilisateurConnecteId;
    private Runnable onLoginRequired;
    private Runnable onPostCreated;
    private Consumer<Subreddit> onSubredditRequired;
    
    public CreatePostController(PostService postService,
                                 SubredditService subredditService,
                                 UtilisateurService utilisateurService) {
        this.postService = postService;
        this.subredditService = subredditService;
        this.utilisateurService = utilisateurService;
    }
    
    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }
    
    public void setPostTitleField(TextField postTitleField) {
        this.postTitleField = postTitleField;
    }
    
    public void setPostContentArea(TextArea postContentArea) {
        this.postContentArea = postContentArea;
    }
    
    public void setSubredditComboBox(ComboBox<Subreddit> subredditComboBox) {
        this.subredditComboBox = subredditComboBox;
        loadSubredditsIntoComboBox();
    }
    
    public void setSelectedSubreddit(Subreddit subreddit) {
        this.selectedSubreddit = subreddit;
    }
    
    public void setOnLoginRequired(Runnable callback) {
        this.onLoginRequired = callback;
    }
    
    public void setOnPostCreated(Runnable callback) {
        this.onPostCreated = callback;
    }

    public void loadSubredditsIntoComboBox() {
        if (subredditComboBox == null) return;
        
        subredditComboBox.getItems().clear();
        List<Subreddit> allSubs = subredditService.getSubreddits();
        
        if (allSubs.isEmpty()) {
            subredditService.creerSubreddit("General", "La communauté par défaut");
            allSubs = subredditService.getSubreddits();
        }
        
        subredditComboBox.getItems().addAll(allSubs);
    }
    
    public void handleCreatePost() {
        if (!utilisateurService.estAuthentifie()) {
            if (onLoginRequired != null) {
                onLoginRequired.run();
            }
            return;
        }
        
        String titre = postTitleField != null ? postTitleField.getText() : "";
        String contenu = postContentArea != null ? postContentArea.getText() : "";
        
        Subreddit sub = selectedSubreddit != null ? selectedSubreddit : 
                        (subredditComboBox != null ? subredditComboBox.getValue() : null);

        try {
            if (titre == null || titre.trim().isEmpty()) {
                throw new IllegalArgumentException("Le titre est obligatoire.");
            }
            
            if (contenu == null || contenu.trim().isEmpty()) {
                throw new IllegalArgumentException("Le contenu est obligatoire.");
            }
            
            if (sub == null) {
                throw new IllegalArgumentException("Veuillez choisir une communauté (Subreddit).");
            }

            postService.creerPost(titre.trim(), contenu.trim(), utilisateurConnecteId, sub.getId_subreddit());

            clearForm();
            
            if (onPostCreated != null) {
                onPostCreated.run();
            }

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }
    
    public void clearForm() {
        if (postTitleField != null) {
            postTitleField.clear();
        }
        if (postContentArea != null) {
            postContentArea.clear();
        }
        if (subredditComboBox != null) {
            subredditComboBox.getSelectionModel().clearSelection();
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    
    public boolean isFormValid() {
        String titre = postTitleField != null ? postTitleField.getText() : "";
        String contenu = postContentArea != null ? postContentArea.getText() : "";
        Subreddit sub = selectedSubreddit != null ? selectedSubreddit : 
                        (subredditComboBox != null ? subredditComboBox.getValue() : null);
        
        return titre != null && !titre.trim().isEmpty() &&
               contenu != null && !contenu.trim().isEmpty() &&
               sub != null;
    }
}
