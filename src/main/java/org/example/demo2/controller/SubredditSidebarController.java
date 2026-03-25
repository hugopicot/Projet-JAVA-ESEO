package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Subreddit;
import org.example.demo2.service.AbonnementService;
import org.example.demo2.service.SubredditService;

import java.util.List;
import java.util.function.Consumer;
import java.util.Optional;


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

        Button createSubBtn = new Button("+ Create Community");
        createSubBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 12;");
        createSubBtn.setOnAction(e -> showCreateSubredditDialog());
        subredditsContainer.getChildren().add(createSubBtn);

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

    private void showCreateSubredditDialog() {
        Dialog<Subreddit> dialog = new Dialog<>();
        dialog.setTitle("Create Community");
        dialog.setHeaderText("Create a new community");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Community name (r/)");
        TextField descField = new TextField();
        descField.setPromptText("Description");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                String name = nameField.getText();
                String desc = descField.getText();
                if (name != null && !name.trim().isEmpty()) {
                    try {
                        return subredditService.creerSubreddit(name.trim(), desc != null ? desc.trim() : "");
                    } catch (Exception ex) {
                        return null;
                    }
                }
            }
            return null;
        });

        Optional<Subreddit> result = dialog.showAndWait();
        result.ifPresent(subreddit -> {
            chargerSubreddits();
            if (onSubredditSelected != null) {
                onSubredditSelected.accept(subreddit);
            }
        });
    }
}
