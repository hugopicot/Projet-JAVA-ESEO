package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Commentaire;
import org.example.demo2.model.Post;
import org.example.demo2.model.Subreddit;
import org.example.demo2.service.AbonnementService;
import org.example.demo2.service.CommentaireService;
import org.example.demo2.service.PostService;
import org.example.demo2.service.SignalementService;
import org.example.demo2.service.SubredditService;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    // ===== Éléments FXML liés au formulaire de création =====
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
    private Label postErrorLabel;

    // ===== Conteneur où les posts s'affichent =====
    @FXML
    private VBox postsContainer;

    // ===== Service métier =====
    private final PostService postService = new PostService();
    private final CommentaireService commentaireService = new CommentaireService();
    private final SubredditService subredditService = new SubredditService();
    private final SignalementService signalementService = new SignalementService();
    private final AbonnementService abonnementService = new AbonnementService();

    // Filtre actuel (null = tous les posts)
    private Subreddit subredditActuel = null;

    // ===== ID de l'utilisateur connecté =====
    // TODO: À remplacer par l'utilisateur réellement connecté (voir avec Siloe)
    private int utilisateurConnecteId = 1;

    // Formateur de date pour l'affichage
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    /**
     * Appelé automatiquement par JavaFX quand la vue est chargée.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerSubreddits();
        chargerPosts();
        chargerTrendingPosts();
        gererHighlightSidebar(allFeedBox);
    }

    private void chargerSubreddits() {
        subredditsContainer.getChildren().clear();
        subredditComboBox.getItems().clear();

        List<Subreddit> allSubs = subredditService.getSubreddits();
        
        if (allSubs.isEmpty()) {
            subredditService.creerSubreddit("General", "La communauté par défaut");
            allSubs = subredditService.getSubreddits();
        }

        subredditComboBox.getItems().addAll(allSubs);
        List<Integer> abonnementsIds = abonnementService.getAbonnementsIds(utilisateurConnecteId);

        // Section "MY COMMUNITIES"
        if (!abonnementsIds.isEmpty()) {
            Label mineLabel = new Label("MY COMMUNITIES ⭐️");
            mineLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 5 0 0 0;");
            subredditsContainer.getChildren().add(mineLabel);
            
            for (Subreddit sub : allSubs) {
                if (abonnementsIds.contains(sub.getId_subreddit())) {
                    subredditsContainer.getChildren().add(creerBoutonSubreddit(sub));
                }
            }
        }

        // Section "DISCOVER MORE"
        Label allLabel = new Label("EXPLORE 🌍");
        allLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        subredditsContainer.getChildren().add(allLabel);

        for (Subreddit sub : allSubs) {
            // Uniquement si pas déjà dans mes abonnements
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
            subredditActuel = sub;
            chargerPosts();
            chargerTrendingPosts();
            gererHighlightSidebar(subBtn);
        });
        return subBtn;
    }

    private void chargerTrendingPosts() {
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
                card.getStyleClass().add("related-post-item"); // On réutilise le style CSS existant
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #eeeeee; -fx-border-radius: 15;");
                
                // Header : User icon + name
                HBox header = new HBox(8);
                header.setAlignment(Pos.CENTER_LEFT);
                try {
                    ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("img/user_svg.png")));
                    userIcon.setFitWidth(20);
                    userIcon.setPreserveRatio(true);
                    header.getChildren().add(userIcon);
                } catch (Exception e) {}
                Label userLabel = new Label("Utilisateur #" + post.getId_utilisateur());
                userLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #555;");
                header.getChildren().add(userLabel);
                
                // Title
                Label titre = new Label(post.getTitre());
                titre.getStyleClass().add("related-post-label");
                titre.setWrapText(true);
                titre.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
                
                // Footer : Like icon + count
                HBox footer = new HBox(8);
                footer.setAlignment(Pos.CENTER_LEFT);
                try {
                    ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream("img/like_svg.png")));
                    likeIcon.setFitWidth(16);
                    likeIcon.setPreserveRatio(true);
                    footer.getChildren().add(likeIcon);
                } catch (Exception e) {}
                Label scoreLabel = new Label(String.valueOf(post.getScore()));
                scoreLabel.getStyleClass().add("related-post-item-likes-amount");
                footer.getChildren().add(scoreLabel);
                
                card.getChildren().addAll(header, titre, footer);
                
                // Rendre la carte cliquable pour voir le flux du subreddit correspondant (optionnel mais sympa)
                card.setStyle(card.getStyle() + " -fx-cursor: hand;");
                
                trendingContainer.getChildren().add(card);
            }
        }
    }

    @FXML
    private void afficherTousLesPosts() {
        subredditActuel = null;
        chargerPosts();
        chargerTrendingPosts();
        gererHighlightSidebar(allFeedBox);
    }

    private void gererHighlightSidebar(Object active) {
        // Reset styles
        allFeedBox.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
        for (javafx.scene.Node node : subredditsContainer.getChildren()) {
            if (node instanceof Button) {
                node.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: black;");
            }
        }

        // Apply style to active
        if (active instanceof HBox) {
            ((HBox) active).setStyle("-fx-cursor: hand; -fx-background-color: #3498db; -fx-background-radius: 5;");
        } else if (active instanceof Button) {
            ((Button) active).setStyle("-fx-background-color: #3498db; -fx-background-radius: 5; -fx-text-fill: white; -fx-cursor: hand;");
        }
    }

    /**
     * Rafraîchit l'affichage complet (posts et subreddits).
     */
    @FXML
    private void handleRefresh() {
        chargerSubreddits();
        chargerPosts();
        cacherErreur();
    }

    /**
     * Crée un nouveau subreddit.
     */
    @FXML
    private void handleCreateSubreddit() {
        // Dialogue de création personnalisé
        Dialog<Subreddit> dialog = new Dialog<>();
        dialog.setTitle("Créer une Communauté");
        dialog.setHeaderText("Entrez les détails du nouveau Subreddit");

        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        VBox grid = new VBox(10);
        TextField nomField = new TextField();
        nomField.setPromptText("Nom (ex: java)");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description...");
        descArea.setPrefRowCount(3);
        
        grid.getChildren().addAll(new Label("Nom :"), nomField, new Label("Description :"), descArea);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    return subredditService.creerSubreddit(nomField.getText(), descArea.getText());
                } catch (Exception e) {
                    afficherErreur(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(sub -> {
            chargerSubreddits(); // Rafraîchir Sidebar et ComboBox
            afficherErreur("Subreddit r/" + sub.getNom() + " créé !");
        });
    }

    /**
     * Appelé quand l'utilisateur clique sur le bouton "Post".
     */
    @FXML
    private void handleCreatePost() {
        String titre = postTitleField.getText();
        String contenu = postContentArea.getText();
        
        Subreddit sub = (subredditActuel != null) ? subredditActuel : subredditComboBox.getValue();

        try {
            if (sub == null) {
                throw new IllegalArgumentException("Veuillez choisir une communauté (Subreddit).");
            }

            postService.creerPost(titre, contenu, utilisateurConnecteId, sub.getId_subreddit());

            // Effacer les champs du formulaire
            postTitleField.clear();
            postContentArea.clear();
            cacherErreur();

            // Rafraîchir la liste des posts
            chargerPosts();

        } catch (IllegalArgumentException e) {
            afficherErreur(e.getMessage());
        }
    }

    /**
     * Charge tous les posts depuis la BDD et les affiche.
     */
    private void chargerPosts() {
        postsContainer.getChildren().clear();
        List<Post> posts;

        if (subredditActuel == null) {
            subredditComboBox.setVisible(true);
            subredditComboBox.setManaged(true);
            postingToLabel.setVisible(false);
            postingToLabel.setManaged(false);
            posts = postService.getTousLesPosts();
        } else {
            subredditComboBox.setVisible(false);
            subredditComboBox.setManaged(false);
            postingToLabel.setText("r/" + subredditActuel.getNom());
            postingToLabel.setVisible(true);
            postingToLabel.setManaged(true);
            
            // Afficher l'en-tête du subreddit avec bouton Rejoindre/Quitter
            HBox header = new HBox(20);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setStyle("-fx-background-color: #f1f2f6; -fx-padding: 15; -fx-background-radius: 10;");
            
            Label title = new Label("r/" + subredditActuel.getNom());
            title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            
            boolean estAb = abonnementService.estAbonne(utilisateurConnecteId, subredditActuel.getId_subreddit());
            Button joinBtn = new Button(estAb ? "Joined ✓" : "Join");
            joinBtn.getStyleClass().add(estAb ? "make-post-button-white" : "make-post-button");
            
            joinBtn.setOnAction(e -> {
                abonnementService.toggleAbonnement(utilisateurConnecteId, subredditActuel.getId_subreddit());
                chargerSubreddits(); // Rafraîchir sidebar
                chargerPosts(); // Rafraîchir le bouton
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

    /**
     * Crée une "carte" visuelle pour un post.
     */
    private VBox creerCartePost(Post post) {
        VBox carte = new VBox(10);
        carte.getStyleClass().add("post-item");
        carte.setPadding(new Insets(20));

        // --- Ligne utilisateur ---
        HBox userRow = new HBox(5);
        userRow.setAlignment(Pos.CENTER_LEFT);
        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("img/user_svg.png")));
            userIcon.setPreserveRatio(true);
            userIcon.setPickOnBounds(true);
            userRow.getChildren().add(userIcon);
        } catch (Exception e) {
            // Si l'image n'est pas trouvée, on continue sans
        }
        Label userLabel = new Label("Utilisateur #" + post.getId_utilisateur());
        userRow.getChildren().add(userLabel);

        // --- Date ---
        Label dateLabel = new Label(post.getDate_creation().format(DATE_FORMAT));
        dateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");

        // --- Titre ---
        Label titreLabel = new Label(post.getTitre());
        titreLabel.getStyleClass().add("post-item-title");

        // --- Contenu ---
        Label contenuLabel = new Label(post.getContenu());
        contenuLabel.getStyleClass().add("post-item-paragraph");
        contenuLabel.setWrapText(true);

        // --- Boutons (like + commentaire) ---
        HBox buttonsRow = new HBox(20);

        // Bouton Like
        boolean dejaLike = postService.aDejaLike(post.getId_post(), utilisateurConnecteId);
        Button likeBtn = new Button(String.valueOf(post.getScore()));
        
        // Appliquer un style différent si déjà liké
        if (dejaLike) {
            likeBtn.getStyleClass().add("make-post-button"); // On garde le même style mais on pourrait en mettre un "active"
            likeBtn.setOpacity(0.7); // Simple feedback visuel
        } else {
            likeBtn.getStyleClass().add("make-post-button-white");
        }

        try {
            String iconPath = dejaLike ? "img/like_white.png" : "img/like_white.png"; // On reste sur white si on n'a pas encore l'autre image
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            likeIcon.setPreserveRatio(true);
            likeIcon.setPickOnBounds(true);
            likeBtn.setGraphic(likeIcon);
        } catch (Exception e) {
            // Pas d'icône
        }
        
        likeBtn.setOnAction(event -> {
            postService.toggleLike(post.getId_post(), utilisateurConnecteId);
            chargerPosts();
            chargerTrendingPosts();
        });

        // Bouton Commentaire
        int nbCom = commentaireService.getNombreCommentaires(post.getId_post());
        Button commentBtn = new Button(String.valueOf(nbCom));
        commentBtn.getStyleClass().add("make-post-button-white");
        try {
            ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("img/comment_svg.png")));
            commentIcon.setPreserveRatio(true);
            commentIcon.setPickOnBounds(true);
            commentBtn.setGraphic(commentIcon);
        } catch (Exception e) {
            // Pas d'icône
        }
        
        // --- Section Commentaires (Masquée par défaut) ---
        VBox commentsSection = new VBox(5);
        commentsSection.setPadding(new Insets(10, 0, 0, 20)); // Indentation pour les replies
        commentsSection.setVisible(false);
        commentsSection.setManaged(false);

        // Champ de réponse rapide
        HBox replyBox = new HBox(10);
        TextField replyField = new TextField();
        replyField.setPromptText("Écrire une réponse...");
        replyField.setPrefWidth(300);
        Button replySubmit = new Button("Répondre");
        replySubmit.getStyleClass().add("make-post-button");
        
        replySubmit.setOnAction(e -> {
            String txt = replyField.getText();
            if (txt != null && !txt.trim().isEmpty()) {
                commentaireService.ajouterCommentaire(txt, utilisateurConnecteId, post.getId_post(), null);
                replyField.clear();
                chargerPosts(); // On recharge pour voir le nouveau commentaire
            }
        });
        
        replyBox.getChildren().addAll(replyField, replySubmit);
        commentsSection.getChildren().add(replyBox);

        // Ajouter les commentaires existants
        List<Commentaire> coms = commentaireService.getCommentairesPost(post.getId_post());
        for (Commentaire com : coms) {
            VBox comBox = new VBox(2);
            HBox comHeader = new HBox(10);
            Label comUser = new Label("Utilisateur #" + com.getId_utilisateur());
            comUser.setStyle("-fx-font-weight: bold; -fx-font-size: 10;");
            
            Button sigComBtn = new Button("Signaler");
            sigComBtn.setStyle("-fx-font-size: 8; -fx-background-color: transparent; -fx-text-fill: #888; -fx-cursor: hand;");
            sigComBtn.setOnAction(e -> {
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

        // Bouton Signaler Post
        Button reportBtn = new Button("Signaler");
        reportBtn.getStyleClass().add("make-post-button-white");
        reportBtn.setStyle("-fx-text-fill: #e74c3c;");
        reportBtn.setOnAction(e -> {
            signalementService.signalerPost(utilisateurConnecteId, post.getId_post());
            reportBtn.setText("Signalé");
            reportBtn.setDisable(true);
        });

        buttonsRow.getChildren().addAll(likeBtn, commentBtn, reportBtn);

        // Bouton Supprimer (visible seulement pour l'auteur)
        if (post.getId_utilisateur() == utilisateurConnecteId) {
            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
            deleteBtn.setOnAction(event -> {
                postService.supprimerPost(post.getId_post(), utilisateurConnecteId);
                chargerPosts();
            });
            buttonsRow.getChildren().add(deleteBtn);
        }

        // --- Assembler la carte ---
        carte.getChildren().addAll(userRow, dateLabel, titreLabel, contenuLabel, buttonsRow, commentsSection);

        return carte;
    }

    // ===== Méthodes utilitaires pour le label d'erreur =====

    private void afficherErreur(String message) {
        postErrorLabel.setText(message);
        postErrorLabel.setVisible(true);
        postErrorLabel.setManaged(true);
        postErrorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    private void cacherErreur() {
        postErrorLabel.setVisible(false);
        postErrorLabel.setManaged(false);
    }
}
