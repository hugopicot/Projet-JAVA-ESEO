package org.example.demo2.controller;

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
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.*;
import org.example.demo2.util.SessionManager;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostDetailController {

    public interface PostDetailCallback {
        void onBackToHome();
        void onLoginSuccess(Utilisateur user);
        void onLogout();
        void onRegisterSuccess(Utilisateur user);
    }

    private PostService postService;
    private CommentaireService commentaireService;
    private SubredditService subredditService;
    private SignalementService signalementService;
    private UtilisateurService utilisateurService;
    private AbonnementService abonnementService;
    private NavBarController navbarController;

    private Post currentPost;
    private int utilisateurConnecteId = 1;
    private PostDetailCallback callback;
    private DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    private VBox rootContainer;
    private VBox commentsContainer;
    private Button commentBtn;
    private VBox commentsSection;

    public PostDetailController(Post post, PostDetailCallback callback) {
        this.currentPost = post;
        this.callback = callback;
        this.postService = new PostService();
        this.commentaireService = new CommentaireService();
        this.subredditService = new SubredditService();
        this.signalementService = new SignalementService();
        this.utilisateurService = new UtilisateurService();
        this.abonnementService = new AbonnementService();
        this.navbarController = new NavBarController();
    }

    public VBox generateView() {
        rootContainer = new VBox();
        rootContainer.setSpacing(0);

        HBox mainContent = new HBox();
        mainContent.setSpacing(0);

        VBox postDetail = createPostDetail();
        VBox relatedPosts = createRelatedPosts();

        mainContent.getChildren().addAll(postDetail, relatedPosts);
        HBox.setHgrow(postDetail, javafx.scene.layout.Priority.ALWAYS);

        rootContainer.getChildren().add(mainContent);
        VBox.setVgrow(mainContent, javafx.scene.layout.Priority.ALWAYS);

        return rootContainer;
    }

    private VBox createPostDetail() {
        VBox container = new VBox();
        container.setSpacing(20);
        container.setAlignment(Pos.TOP_RIGHT);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white;");

        VBox postCard = createPostCard(currentPost);
        container.getChildren().add(postCard);

        VBox commentForm = createCommentForm();
        container.getChildren().add(commentForm);

        Label commentsTitle = new Label("All Comments");
        commentsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        container.getChildren().add(commentsTitle);

        commentsContainer = new VBox();
        commentsContainer.setSpacing(15);
        commentsContainer.setPadding(new Insets(0, 0, 20, 0));
        container.getChildren().add(commentsContainer);

        chargerCommentaires();

        return container;
    }

    private VBox createPostCard(Post post) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-background-radius: 10;");

        HBox userRow = new HBox(5);
        userRow.setAlignment(Pos.CENTER_LEFT);
        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            userIcon.setPreserveRatio(true);
            userIcon.setPickOnBounds(true);
            userRow.getChildren().add(userIcon);
        } catch (Exception e) {}

        Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
        userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db;");
        userRow.getChildren().add(userLabel);

        Subreddit subreddit = subredditService.getSubredditById(post.getId_subreddit());
        if (subreddit != null) {
            Label subLabel = new Label(" in r/" + subreddit.getNom());
            subLabel.setStyle("-fx-text-fill: #888;");
            userRow.getChildren().add(subLabel);
        }

        Label dateLabel = new Label(post.getDate_creation().format(DATE_FORMAT));
        dateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");

        Label titleLabel = new Label(post.getTitre());
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Label contentLabel = new Label(post.getContenu());
        contentLabel.setStyle("-fx-font-size: 14;");
        contentLabel.setWrapText(true);

        HBox buttonsRow = new HBox(20);

        boolean dejaLike = postService.aDejaLike(post.getId_post(), utilisateurConnecteId);
        Button likeBtn = new Button(String.valueOf(post.getScore()));
        if (dejaLike) {
            likeBtn.getStyleClass().add("make-post-button");
            likeBtn.setOpacity(0.7);
        } else {
            likeBtn.getStyleClass().add("make-post-button-white");
        }
        likeBtn.setStyle("-fx-text-fill: black;");
        try {
            String iconPath = dejaLike ? "/org/example/demo2/img/like_white.png" : "/org/example/demo2/img/like_svg.png";
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            likeIcon.setFitWidth(16);
            likeIcon.setPreserveRatio(true);
            likeBtn.setGraphic(likeIcon);
        } catch (Exception e) {}

        likeBtn.setOnAction(e -> {
            if (!SessionManager.getInstance().estAuthentifie()) {
                navbarController.showLoginDialogWithCallback(() -> {
                    postService.toggleLike(post.getId_post(), SessionManager.getInstance().getUtilisateurConnecte().getId());
                    refreshPostCard();
                });
            } else {
                postService.toggleLike(post.getId_post(), utilisateurConnecteId);
                refreshPostCard();
            }
        });

        int nbCom = commentaireService.getNombreCommentaires(post.getId_post());
        commentBtn = new Button(String.valueOf(nbCom));
        commentBtn.getStyleClass().add("make-post-button-white");
        commentBtn.setStyle("-fx-text-fill: black;");
        try {
            ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/comment_svg.png")));
            commentIcon.setPreserveRatio(true);
            commentBtn.setGraphic(commentIcon);
        } catch (Exception e) {}

        commentBtn.setOnAction(e -> {
            boolean visible = !commentsSection.isVisible();
            commentsSection.setVisible(visible);
            commentsSection.setManaged(visible);
        });

        buttonsRow.getChildren().addAll(likeBtn, commentBtn);

        if (SessionManager.getInstance().estAuthentifie() && post.getId_utilisateur() == utilisateurConnecteId) {
            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
            deleteBtn.setOnAction(e -> {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirmation");
                confirmDialog.setHeaderText("Voulez-vous vraiment supprimer ce post ?");
                confirmDialog.setContentText("Cette action est irreversible.");

                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        postService.supprimerPost(post.getId_post(), utilisateurConnecteId);
                        callback.onBackToHome();
                    }
                });
            });
            buttonsRow.getChildren().add(deleteBtn);
        }

        card.getChildren().addAll(userRow, dateLabel, titleLabel, contentLabel, buttonsRow);

        return card;
    }

    private void refreshPostCard() {
        Post updatedPost = postService.getPostById(currentPost.getId_post());
        if (updatedPost != null) {
            currentPost = updatedPost;
            
            for (int i = 0; i < rootContainer.getChildren().size(); i++) {
                Object child = rootContainer.getChildren().get(i);
                if (child instanceof HBox && ((HBox) child).getChildren().size() > 1) {
                    HBox mainContent = (HBox) child;
                    if (mainContent.getChildren().size() >= 2) {
                        VBox postDetail = (VBox) mainContent.getChildren().get(0);
                        postDetail.getChildren().set(0, createPostCard(currentPost));
                    }
                }
            }
        }
    }

    private VBox createCommentForm() {
        VBox form = new VBox(10);
        form.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-background-radius: 10;");

        TextField commentField = new TextField();
        commentField.setPromptText("Write a comment...");
        commentField.setPrefWidth(400);
        commentField.setStyle("-fx-background-radius: 5; -fx-padding: 10;");

        Button submitBtn = new Button("Comment");
        submitBtn.getStyleClass().add("make-post-button");
        submitBtn.setStyle("-fx-text-fill: white; -fx-background-radius: 5;");
        submitBtn.setOnAction(e -> {
            String txt = commentField.getText();
            if (txt == null || txt.trim().isEmpty()) {
                return;
            }

            if (!SessionManager.getInstance().estAuthentifie()) {
                navbarController.showLoginDialogWithCallback(() -> {
                    commentField.setText(txt);
                    submitBtn.fire();
                });
                return;
            }

            int userId = SessionManager.getInstance().getUtilisateurConnecteId();
            commentaireService.ajouterCommentaire(txt, userId, currentPost.getId_post(), null);
            commentField.clear();
            chargerCommentaires();
            refreshCommentButton();
        });

        form.getChildren().addAll(commentField, submitBtn);

        return form;
    }

    private void refreshCommentButton() {
        if (commentBtn != null) {
            int nbCom = commentaireService.getNombreCommentaires(currentPost.getId_post());
            commentBtn.setText(String.valueOf(nbCom));
        }
    }

    private void chargerCommentaires() {
        commentsContainer.getChildren().clear();

        List<Commentaire> coms = commentaireService.getCommentairesPost(currentPost.getId_post());
        for (Commentaire com : coms) {
            VBox comBox = createCommentCard(com);
            commentsContainer.getChildren().add(comBox);
        }
    }

    private VBox createCommentCard(Commentaire com) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-background-radius: 5;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            userIcon.setFitWidth(16);
            userIcon.setPreserveRatio(true);
            header.getChildren().add(userIcon);
        } catch (Exception e) {}

        Label userLabel = new Label(getNomUtilisateur(com.getId_utilisateur()));
        userLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #3498db;");
        header.getChildren().add(userLabel);

        Label dateLabel = new Label(com.getDate_creation().format(DATE_FORMAT));
        dateLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #888;");
        header.getChildren().add(dateLabel);

        if (SessionManager.getInstance().estAuthentifie()) {
            Button reportBtn = new Button("Signaler");
            reportBtn.setStyle("-fx-font-size: 10; -fx-background-color: transparent; -fx-text-fill: #888; -fx-cursor: hand;");
            reportBtn.setOnAction(ev -> {
                signalementService.signalerCommentaire(utilisateurConnecteId, com.getId_commentaire());
                reportBtn.setText("Signalé");
                reportBtn.setDisable(true);
            });
            header.getChildren().add(reportBtn);
        }

        Label contentLabel = new Label(com.getContenu());
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 13;");

        card.getChildren().addAll(header, contentLabel);

        return card;
    }

    private VBox createRelatedPosts() {
        VBox container = new VBox();
        container.setSpacing(15);
        container.setPadding(new Insets(20));
        container.setMinWidth(250);
        container.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Related Posts");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        container.getChildren().add(title);

        List<Post> relatedPosts = postService.getTopPostsParSubreddit(currentPost.getId_subreddit(), 5);
        for (Post post : relatedPosts) {
            if (post.getId_post() != currentPost.getId_post()) {
                VBox postCard = createRelatedPostCard(post);
                container.getChildren().add(postCard);
            }
        }

        return container;
    }

    private VBox createRelatedPostCard(Post post) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand;");

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            userIcon.setFitWidth(16);
            userIcon.setPreserveRatio(true);
            header.getChildren().add(userIcon);
        } catch (Exception e) {}

        Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
        userLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #555;");
        header.getChildren().add(userLabel);

        Label titleLabel = new Label(post.getTitre());
        titleLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        HBox footer = new HBox(5);
        footer.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/like_svg.png")));
            likeIcon.setFitWidth(12);
            likeIcon.setPreserveRatio(true);
            footer.getChildren().add(likeIcon);
        } catch (Exception e) {}

        Label scoreLabel = new Label(String.valueOf(post.getScore()));
        scoreLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");

        footer.getChildren().add(scoreLabel);

        card.getChildren().addAll(header, titleLabel, footer);

        return card;
    }

    private String getNomUtilisateur(int idUtilisateur) {
        if (SessionManager.getInstance().estAuthentifie() && idUtilisateur == SessionManager.getInstance().getUtilisateurConnecteId()) {
            return SessionManager.getInstance().getUtilisateurConnecte().getPseudo();
        }
        Utilisateur user = utilisateurService.getUtilisateurParId(idUtilisateur);
        if (user != null) {
            return user.getPseudo();
        }
        return "Utilisateur #" + idUtilisateur;
    }

    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }
}