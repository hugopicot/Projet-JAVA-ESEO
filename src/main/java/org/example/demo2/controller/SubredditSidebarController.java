package org.example.demo2.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Subreddit;
import org.example.demo2.service.AbonnementService;
import org.example.demo2.service.SubredditService;

import java.util.List;
import java.util.function.Consumer;


public class SubredditSidebarController {

    private final SubredditService subredditService;
    private final AbonnementService abonnementService;
    
    private int utilisateurConnecteId;
    private HBox allFeedBox;
    private VBox subredditsContainer;
    private Consumer<Subreddit> onSubredditSelected;
    private Runnable onAllFeedSelected;
    
    public SubredditSidebarController(SubredditService subredditService, 
                                       AbonnementService abonnementService) {
        this.subredditService = subredditService;
        this.abonnementService = abonnementService;
    }
    
    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }
    
    public void setAllFeedBox(HBox allFeedBox) {
        this.allFeedBox = allFeedBox;
        if (allFeedBox != null) {
            allFeedBox.setOnMouseClicked(e -> {
                if (onAllFeedSelected != null) {
                    onAllFeedSelected.run();
                }
            });
        }
    }
    
    public void setSubredditsContainer(VBox subredditsContainer) {
        this.subredditsContainer = subredditsContainer;
    }
    
    public void setOnSubredditSelected(Consumer<Subreddit> callback) {
        this.onSubredditSelected = callback;
    }
    
    public void setOnAllFeedSelected(Runnable callback) {
        this.onAllFeedSelected = callback;
    }

    public void chargerSubreddits() {
        if (subredditsContainer == null) return;
        
        subredditsContainer.getChildren().clear();

        List<Subreddit> allSubs = subredditService.getSubreddits();
        
        if (allSubs.isEmpty()) {
            subredditService.creerSubreddit("General", "La communauté par défaut");
            allSubs = subredditService.getSubreddits();
        }

        List<Integer> abonnementsIds = abonnementService.getAbonnementsIds(utilisateurConnecteId);

        if (!abonnementsIds.isEmpty()) {
            Label mineLabel = new Label("MY COMMUNITIES");
            mineLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 5 0 0 0;");
            subredditsContainer.getChildren().add(mineLabel);
            
            for (Subreddit sub : allSubs) {
                if (abonnementsIds.contains(sub.getId_subreddit())) {
                    subredditsContainer.getChildren().add(creerBoutonSubreddit(sub));
                }
            }
        }

        Label allLabel = new Label("EXPLORE");
        allLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        subredditsContainer.getChildren().add(allLabel);

        for (Subreddit sub : allSubs) {
            if (!abonnementsIds.contains(sub.getId_subreddit())) {
                subredditsContainer.getChildren().add(creerBoutonSubreddit(sub));
            }
        }
    }
    
    private Button creerBoutonSubreddit(Subreddit sub) {
        Button subBtn = new Button("r/" + sub.getNom());
        subBtn.getStyleClass().add("sidebar-item-2");
        subBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        subBtn.setPrefWidth(150);
        
        subBtn.setOnAction(e -> {
            highlightSidebarItem(subBtn);
            if (onSubredditSelected != null) {
                onSubredditSelected.accept(sub);
            }
        });
        
        return subBtn;
    }
    
    public void highlightAllFeed() {
        highlightSidebarItem(allFeedBox);
    }
    
    public void highlightSidebarItem(Object active) {
        if (allFeedBox != null) {
            allFeedBox.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        }
        
        if (subredditsContainer != null) {
            for (javafx.scene.Node node : subredditsContainer.getChildren()) {
                if (node instanceof Button) {
                    node.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: black;");
                }
            }
        }

        if (active instanceof HBox) {
            ((HBox) active).setStyle("-fx-cursor: hand; -fx-background-color: #3498db; -fx-background-radius: 5;");
        } else if (active instanceof Button) {
            ((Button) active).setStyle("-fx-background-color: #3498db; -fx-background-radius: 5; -fx-text-fill: white; -fx-cursor: hand;");
        }
    }
}
