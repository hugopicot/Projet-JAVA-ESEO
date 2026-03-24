package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.demo2.controller.NavBarController;
import org.example.demo2.controller.PostDetailController;
import org.example.demo2.controller.ProfileController;
import org.example.demo2.model.Commentaire;
import org.example.demo2.model.Post;
import org.example.demo2.model.Subreddit;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.AbonnementService;
import org.example.demo2.service.CommentaireService;
import org.example.demo2.service.PostService;
import org.example.demo2.service.SignalementService;
import org.example.demo2.service.SubredditService;
import org.example.demo2.service.UtilisateurService;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable, NavBarController.LoginCallback, ProfileController.ProfileCallback {

    @FXML
    private HBox navbarContainer;

    @FXML
    private HBox mainContentContainer;

    @FXML
    private TextField postTitleField;

    @FXML
    private TextArea postContentArea;

    @FXML
    private ComboBox<Subreddit> subredditComboBox;

    @FXML
    private HBox allFeedBox;

    @FXML
    private VBox subredditsContainer;

    @FXML
    private VBox trendingContainer;

    @FXML
    private Label postingToLabel;

    @FXML
    private VBox postsContainer;

    private final PostService postService = new PostService();
    private final CommentaireService commentaireService = new CommentaireService();
    private final SubredditService subredditService = new SubredditService();
    private final SignalementService signalementService = new SignalementService();
    private final AbonnementService abonnementService = new AbonnementService();
    private final UtilisateurService utilisateurService = new UtilisateurService();

    private final NavBarController navbarController = new NavBarController();

    private Subreddit subredditActuel = null;
    private int utilisateurConnecteId = 1;
    private VBox savedMainContentView = null;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (navbarContainer != null) {
            String logoPath = getClass().getResource("/org/example/demo2/img/nova_logo_new.png").toExternalForm();
            HBox navbar = navbarController.generateNavbar(logoPath, utilisateurService, this);
            navbarContainer.getChildren().add(navbar);
        }
        
        if (allFeedBox != null) {
            allFeedBox.setOnMouseClicked(e -> {
                handleAllFeedClick(null);
            });
        }
        
        chargerSubreddits();
        chargerPosts();
        chargerTrendingPosts();
        if (allFeedBox != null) {
            gererHighlightSidebar(allFeedBox);
        }
        
        // Save the original main view after initialization
        javafx.application.Platform.runLater(() -> {
            if (originalMainContentChildren == null) {
                originalMainContentChildren = new java.util.ArrayList<>(mainContentContainer.getChildren());
            }
        });
    }

    @FXML
    private void handleAllFeedClick(javafx.scene.input.MouseEvent event) {
        // Always restore the original main view when clicking All Feed
        if (originalMainContentChildren != null && !originalMainContentChildren.isEmpty()) {
            mainContentContainer.getChildren().clear();
            mainContentContainer.getChildren().addAll(originalMainContentChildren);
        }
        
        // Clear saved state
        savedMainContentChildren = null;
        savedSubredditActuel = null;
        subredditActuel = null;
        
        chargerSubreddits();
        chargerPosts();
        chargerTrendingPosts();
        gererHighlightSidebar(allFeedBox);
    }

    @Override
    public void onLoginSuccess(Utilisateur user) {
        utilisateurConnecteId = user.getId();
        chargerPosts();
        chargerSubreddits();
        System.out.println("Bienvenue " + user.getPseudo() + " (ID: " + user.getId() + ")");
    }

    @Override
    public void onLogout() {
        utilisateurConnecteId = 1;
        chargerPosts();
        chargerSubreddits();
        System.out.println("Déconnexion réussie");
    }

    @Override
    public void onRegisterSuccess(Utilisateur user) {
        utilisateurConnecteId = user.getId();
        chargerPosts();
        chargerSubreddits();
        System.out.println("Compte cree! Bienvenue " + user.getPseudo() + " (ID: " + user.getId() + ")");
    }

    @Override
    public void onProfileClick() {
        if (!utilisateurService.estAuthentifie()) {
            return;
        }
        
        Utilisateur user = utilisateurService.getUtilisateurConnecte();
        navigateToProfile(user);
    }

    private void navigateToProfile(Utilisateur user) {
        // Save current state before navigating to profile
        if (savedMainContentChildren == null || savedMainContentChildren.isEmpty()) {
            savedMainContentChildren = new java.util.ArrayList<>(mainContentContainer.getChildren());
            savedSubredditActuel = subredditActuel;
        }
        
        ProfileController profileController = new ProfileController(user, this);
        VBox profileView = profileController.generateView();
        
        VBox sidebar = (VBox) savedMainContentChildren.get(0);
        
        mainContentContainer.getChildren().clear();
        mainContentContainer.getChildren().addAll(sidebar, profileView);
        HBox.setHgrow(mainContentContainer.getChildren().get(1), Priority.ALWAYS);
    }

    // ProfileController.ProfileCallback methods
    @Override
    public void onBackToHome() {
        if (savedMainContentChildren != null && !savedMainContentChildren.isEmpty()) {
            mainContentContainer.getChildren().clear();
            mainContentContainer.getChildren().addAll(savedMainContentChildren);
            savedMainContentChildren = null;
            savedSubredditActuel = null;
        }
        
        subredditActuel = null;
        chargerSubreddits();
        chargerPosts();
        chargerTrendingPosts();
        gererHighlightSidebar(allFeedBox);
    }

    @Override
    public void onPostClick(Post post) {
        navigateToPostDetail(post);
    }

    private void gererHighlightSidebar(Object active) {
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

    private void chargerSubreddits() {
        if (subredditsContainer == null || subredditComboBox == null) return;
        
        subredditsContainer.getChildren().clear();
        subredditComboBox.getItems().clear();

        List<Subreddit> allSubs = subredditService.getSubreddits();
        
        if (allSubs.isEmpty()) {
            subredditService.creerSubreddit("General", "La communauté par défaut");
            allSubs = subredditService.getSubreddits();
        }

        subredditComboBox.getItems().addAll(allSubs);
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

    private void chargerPosts() {
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
            
            HBox header = new HBox(20);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setStyle("-fx-background-color: #f1f2f6; -fx-padding: 15; -fx-background-radius: 10;");
            
            Label title = new Label("r/" + subredditActuel.getNom());
            title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            
            boolean estAb = abonnementService.estAbonne(utilisateurConnecteId, subredditActuel.getId_subreddit());
            Button joinBtn = new Button(estAb ? "Joined ✓" : "Join");
            joinBtn.getStyleClass().add(estAb ? "make-post-button-white" : "make-post-button");
            
            joinBtn.setOnAction(e -> {
                if (!utilisateurService.estAuthentifie()) {
                    navbarController.showLoginDialogWithCallback(() -> {
                        abonnementService.toggleAbonnement(utilisateurConnecteId, subredditActuel.getId_subreddit());
                        chargerSubreddits();
                        chargerPosts();
                    });
                } else {
                    abonnementService.toggleAbonnement(utilisateurConnecteId, subredditActuel.getId_subreddit());
                    chargerSubreddits();
                    chargerPosts();
                }
            });
            
            header.getChildren().addAll(title, joinBtn);
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
                VBox carte = creerCartePost(post);
                postsContainer.getChildren().add(carte);
            }
        }
    }

    private void chargerTrendingPosts() {
        if (trendingContainer == null) return;
        
        trendingContainer.getChildren().clear();
        
        Integer currentId = (subredditActuel != null) ? subredditActuel.getId_subreddit() : null;
        List<Post> topPosts = postService.getTopPostsParSubreddit(currentId, 3);

        if (topPosts.isEmpty()) {
            Label vide = new Label("Pas encore de top posts.");
            vide.setStyle("-fx-text-fill: #888; -fx-padding: 10;");
            trendingContainer.getChildren().add(vide);
        } else {
            for (Post post : topPosts) {
                VBox card = new VBox(15);
                card.getStyleClass().add("related-post-item");
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand;");
                
                HBox header = new HBox(8);
                header.setAlignment(Pos.CENTER_LEFT);
                try {
                    ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("img/user_svg.png")));
                    userIcon.setFitWidth(20);
                    userIcon.setPreserveRatio(true);
                    header.getChildren().add(userIcon);
                } catch (Exception e) {}
                Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
                userLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #555;");
                header.getChildren().add(userLabel);
                
                Label titre = new Label(post.getTitre());
                titre.setWrapText(true);
                titre.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
                
                HBox footer = new HBox(8);
                footer.setAlignment(Pos.CENTER_LEFT);
                try {
                    ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream("img/like_svg.png")));
                    likeIcon.setFitWidth(16);
                    likeIcon.setPreserveRatio(true);
                    footer.getChildren().add(likeIcon);
                } catch (Exception e) {}
                Label scoreLabel = new Label(String.valueOf(post.getScore()));
                scoreLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");
                footer.getChildren().add(scoreLabel);
                
                card.getChildren().addAll(header, titre, footer);
                
                card.setOnMouseClicked(e -> navigateToPostDetail(post));
                
                trendingContainer.getChildren().add(card);
            }
        }
    }

    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }

    private Subreddit savedSubredditActuel = null;
    private List<javafx.scene.Node> savedMainContentChildren = null;
    private List<javafx.scene.Node> originalMainContentChildren = null; // Save the original main view

    private void navigateToPostDetail(Post post) {
        savedSubredditActuel = subredditActuel;
        savedMainContentChildren = new java.util.ArrayList<>(mainContentContainer.getChildren());
        
        PostDetailController detailController = new PostDetailController(post, new PostDetailController.PostDetailCallback() {
            @Override
            public void onBackToHome() {
                mainContentContainer.getChildren().clear();
                mainContentContainer.getChildren().addAll(savedMainContentChildren);
                subredditActuel = savedSubredditActuel;
                chargerPosts();
                chargerTrendingPosts();
            }

            @Override
            public void onLoginSuccess(Utilisateur user) {
                utilisateurConnecteId = user.getId();
                chargerSubreddits();
                chargerPosts();
                chargerTrendingPosts();
            }

            @Override
            public void onLogout() {
                utilisateurConnecteId = 1;
                chargerSubreddits();
                chargerPosts();
                chargerTrendingPosts();
            }

            @Override
            public void onRegisterSuccess(Utilisateur user) {
                utilisateurConnecteId = user.getId();
                chargerSubreddits();
                chargerPosts();
                chargerTrendingPosts();
            }
        });
        detailController.setUtilisateurConnecteId(utilisateurConnecteId);
        
        VBox detailView = detailController.generateView();
        HBox detailHBox = (HBox) detailView.getChildren().get(0);
        VBox postDetail = (VBox) detailHBox.getChildren().get(0);
        VBox relatedPosts = (VBox) detailHBox.getChildren().get(1);
        
        VBox sidebar = (VBox) savedMainContentChildren.get(0);
        
        mainContentContainer.getChildren().clear();
        mainContentContainer.getChildren().addAll(sidebar, postDetail, relatedPosts);
        HBox.setHgrow(mainContentContainer.getChildren().get(1), Priority.ALWAYS);
    }

    private String getNomUtilisateur(int idUtilisateur) {
        if (idUtilisateur == utilisateurConnecteId && utilisateurService.estAuthentifie()) {
            return utilisateurService.getUtilisateurConnecte().getPseudo();
        }
        Utilisateur user = utilisateurService.getUtilisateurParId(idUtilisateur);
        if (user != null) {
            return user.getPseudo();
        }
        return "Utilisateur #" + idUtilisateur;
    }

    private Button creerBoutonSubreddit(Subreddit sub) {
        Button subBtn = new Button("r/" + sub.getNom());
        subBtn.getStyleClass().add("sidebar-item-2");
        subBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        subBtn.setPrefWidth(150);
        subBtn.setOnAction(e -> {
            subredditActuel = sub;
            chargerPosts();
            chargerTrendingPosts();
            gererHighlightSidebar(subBtn);
        });
        return subBtn;
    }

    @FXML
    private void handleCreatePost() {
        if (!utilisateurService.estAuthentifie()) {
            navbarController.showLoginDialogWithCallback(() -> {
                handleCreatePost();
            });
            return;
        }
        
        String titre = postTitleField.getText();
        String contenu = postContentArea.getText();
        
        Subreddit sub = (subredditActuel != null) ? subredditActuel : subredditComboBox.getValue();

        try {
            if (sub == null) {
                throw new IllegalArgumentException("Veuillez choisir une communauté (Subreddit).");
            }

            postService.creerPost(titre, contenu, utilisateurConnecteId, sub.getId_subreddit());

            postTitleField.clear();
            postContentArea.clear();

            chargerPosts();

        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    private VBox creerCartePost(Post post) {
        VBox carte = new VBox(10);
        carte.getStyleClass().add("post-item");
        carte.setPadding(new Insets(20));
        carte.setStyle(carte.getStyle() + "-fx-cursor: hand;");
        carte.setOnMouseClicked(e -> navigateToPostDetail(post));

        HBox userRow = new HBox(5);
        userRow.setAlignment(Pos.CENTER_LEFT);
        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("img/user_svg.png")));
            userIcon.setPreserveRatio(true);
            userIcon.setPickOnBounds(true);
            userRow.getChildren().add(userIcon);
        } catch (Exception e) {}
        Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
        userRow.getChildren().add(userLabel);

        Label dateLabel = new Label(post.getDate_creation().format(DATE_FORMAT));
        dateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");

        Label titreLabel = new Label(post.getTitre());
        titreLabel.getStyleClass().add("post-item-title");

        Label contenuLabel = new Label(post.getContenu());
        contenuLabel.getStyleClass().add("post-item-paragraph");
        contenuLabel.setWrapText(true);

        HBox buttonsRow = new HBox(20);

        boolean dejaLike = postService.aDejaLike(post.getId_post(), utilisateurConnecteId);
        Button likeBtn = new Button(String.valueOf(post.getScore()));
        
        if (dejaLike) {
            likeBtn.getStyleClass().add("make-post-button");
            likeBtn.setOpacity(0.7);
        } else {
            likeBtn.getStyleClass().add("make-post-button-white");
        }
        likeBtn.setStyle(likeBtn.getStyle() + "-fx-text-fill: black;");

        try {
            String iconPath = dejaLike ? "img/like_white.png" : "img/like_svg.png";
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            likeIcon.setFitWidth(16);
            likeIcon.setPreserveRatio(true);
            likeIcon.setPickOnBounds(true);
            likeBtn.setGraphic(likeIcon);
        } catch (Exception e) {}
        
        likeBtn.setOnAction(event -> {
            if (!utilisateurService.estAuthentifie()) {
                navbarController.showLoginDialogWithCallback(() -> {
                    postService.toggleLike(post.getId_post(), utilisateurService.getUtilisateurConnecte().getId());
                    chargerPosts();
                    chargerTrendingPosts();
                });
            } else {
                postService.toggleLike(post.getId_post(), utilisateurConnecteId);
                chargerPosts();
                chargerTrendingPosts();
            }
        });

        int nbCom = commentaireService.getNombreCommentaires(post.getId_post());
        Button commentBtn = new Button(String.valueOf(nbCom));
        commentBtn.getStyleClass().add("make-post-button-white");
        commentBtn.setStyle("-fx-text-fill: black;");
        try {
            ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("img/comment_svg.png")));
            commentIcon.setPreserveRatio(true);
            commentIcon.setPickOnBounds(true);
            commentBtn.setGraphic(commentIcon);
        } catch (Exception e) {}
        
        VBox commentsSection = new VBox(5);
        commentsSection.setPadding(new Insets(10, 0, 0, 20));
        commentsSection.setVisible(false);
        commentsSection.setManaged(false);

        HBox replyBox = new HBox(10);
        TextField replyField = new TextField();
        replyField.setPromptText("Écrire une réponse...");
        replyField.setPrefWidth(300);
        Button replySubmit = new Button("Répondre");
        replySubmit.getStyleClass().add("make-post-button");
        replySubmit.setStyle("-fx-text-fill: black;");

        replySubmit.setOnAction(e -> {
            String txt = replyField.getText();
            if (txt == null || txt.trim().isEmpty()) {
                return;
            }
            
            if (!utilisateurService.estAuthentifie()) {
                navbarController.showLoginDialogWithCallback(() -> {
                    replyField.setText(txt);
                    replySubmit.fire();
                });
                return;
            }
            
            commentaireService.ajouterCommentaire(txt, utilisateurConnecteId, post.getId_post(), null);
            replyField.clear();
            
            VBox comBox = new VBox(2);
            HBox comHeader = new HBox(10);
            String userName = utilisateurService.getUtilisateurConnecte().getPseudo();
            Label comUser = new Label(userName);
            comUser.setStyle("-fx-font-weight: bold; -fx-font-size: 10;");

            comHeader.getChildren().add(comUser);
            
            Label comTxt = new Label(txt);
            comTxt.setWrapText(true);
            comBox.getChildren().addAll(comHeader, comTxt);
            comBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 5; -fx-background-radius: 5;");
            
            commentsSection.getChildren().add(comBox);
            int currentCount = Integer.parseInt(commentBtn.getText()) + 1;
            commentBtn.setText(String.valueOf(currentCount));
            chargerTrendingPosts();
        });
        
        replyBox.getChildren().addAll(replyField, replySubmit);
        commentsSection.getChildren().add(replyBox);

        List<Commentaire> coms = commentaireService.getCommentairesPost(post.getId_post());
        for (Commentaire com : coms) {
            VBox comBox = new VBox(2);
            HBox comHeader = new HBox(10);
            Label comUser = new Label(getNomUtilisateur(com.getId_utilisateur()));
            comUser.setStyle("-fx-font-weight: bold; -fx-font-size: 10;");
            
            Button sigComBtn = new Button("Signaler");
            sigComBtn.setStyle("-fx-font-size: 8; -fx-background-color: transparent; -fx-text-fill: #888; -fx-cursor: hand;");
            sigComBtn.setOnAction(ev -> {
                signalementService.signalerCommentaire(utilisateurConnecteId, com.getId_commentaire());
                sigComBtn.setText("Signalé");
                sigComBtn.setDisable(true);
            });

            comHeader.getChildren().addAll(comUser, sigComBtn);
            
            Label comTxt = new Label(com.getContenu());
            comTxt.setWrapText(true);
            comBox.getChildren().addAll(comHeader, comTxt);
            comBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 5; -fx-background-radius: 5;");
            commentsSection.getChildren().add(comBox);
        }

        commentBtn.setOnAction(event -> {
            boolean visible = !commentsSection.isVisible();
            commentsSection.setVisible(visible);
            commentsSection.setManaged(visible);
        });

        Button reportBtn = new Button("Signaler");
        reportBtn.getStyleClass().add("make-post-button-white");
        reportBtn.setStyle("-fx-text-fill: #e74c3c;");
        reportBtn.setOnAction(e -> {
            signalementService.signalerPost(utilisateurConnecteId, post.getId_post());
            reportBtn.setText("Signalé");
            reportBtn.setDisable(true);
        });

        buttonsRow.getChildren().addAll(likeBtn, commentBtn, reportBtn);

        if (utilisateurService.estAuthentifie() && post.getId_utilisateur() == utilisateurConnecteId) {
            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
            deleteBtn.setOnAction(event -> {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirmation");
                confirmDialog.setHeaderText("Voulez-vous vraiment supprimer ce post ?");
                confirmDialog.setContentText("Cette action est irreversible.");

                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        postService.supprimerPost(post.getId_post(), utilisateurConnecteId);
                        chargerPosts();
                    }
                });
            });
            buttonsRow.getChildren().add(deleteBtn);
        }

        carte.getChildren().addAll(userRow, dateLabel, titreLabel, contenuLabel, buttonsRow, commentsSection);

        return carte;
    }
}
