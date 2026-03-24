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
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.CommentaireService;
import org.example.demo2.service.PostService;
import org.example.demo2.service.SignalementService;
import org.example.demo2.service.UtilisateurService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;


public class PostCardController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    
    private final PostService postService;
    private final CommentaireService commentaireService;
    private final SignalementService signalementService;
    private final UtilisateurService utilisateurService;
    
    private int utilisateurConnecteId;
    private Consumer<Post> onPostClick;
    private Runnable onLoginRequired;
    private Consumer<Utilisateur> onLoginSuccess;
    
    public PostCardController(PostService postService, 
                              CommentaireService commentaireService,
                              SignalementService signalementService,
                              UtilisateurService utilisateurService) {
        this.postService = postService;
        this.commentaireService = commentaireService;
        this.signalementService = signalementService;
        this.utilisateurService = utilisateurService;
    }
    
    public void setUtilisateurConnecteId(int id) {
        this.utilisateurConnecteId = id;
    }
    
    public void setOnPostClick(Consumer<Post> callback) {
        this.onPostClick = callback;
    }
    
    public void setOnLoginRequired(Runnable callback) {
        this.onLoginRequired = callback;
    }
    
    public void setOnLoginSuccess(Consumer<Utilisateur> callback) {
        this.onLoginSuccess = callback;
    }

    public VBox createPostCard(Post post) {
        VBox carte = new VBox(10);
        carte.getStyleClass().add("post-item");
        carte.setPadding(new Insets(20));
        carte.setStyle(carte.getStyle() + "-fx-cursor: hand;");
        
        if (onPostClick != null) {
            carte.setOnMouseClicked(e -> onPostClick.accept(post));
        }

        HBox userRow = createUserRow(post);
        
        Label dateLabel = new Label(post.getDate_creation().format(DATE_FORMAT));
        dateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");

        Label titreLabel = new Label(post.getTitre());
        titreLabel.getStyleClass().add("post-item-title");

        Label contenuLabel = new Label(post.getContenu());
        contenuLabel.getStyleClass().add("post-item-paragraph");
        contenuLabel.setWrapText(true);

        HBox buttonsRow = createButtonsRow(post);

        VBox commentsSection = createCommentsSection(post);

        carte.getChildren().addAll(userRow, dateLabel, titreLabel, contenuLabel, buttonsRow, commentsSection);

        return carte;
    }
    
    private HBox createUserRow(Post post) {
        HBox userRow = new HBox(5);
        userRow.setAlignment(Pos.CENTER_LEFT);
        
        try {
            ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            userIcon.setFitWidth(20);
            userIcon.setPreserveRatio(true);
            userIcon.setPickOnBounds(true);
            userRow.getChildren().add(userIcon);
        } catch (Exception e) {
        }
        
        Label userLabel = new Label(getNomUtilisateur(post.getId_utilisateur()));
        userRow.getChildren().add(userLabel);
        
        return userRow;
    }
    
    private HBox createButtonsRow(Post post) {
        HBox buttonsRow = new HBox(20);

        Button likeBtn = createLikeButton(post);
        Button commentBtn = createCommentButton(post);
        Button reportBtn = createReportButton(post);

        buttonsRow.getChildren().addAll(likeBtn, commentBtn, reportBtn);

        if (utilisateurService.estAuthentifie() && post.getId_utilisateur() == utilisateurConnecteId) {
            Button deleteBtn = createDeleteButton(post);
            buttonsRow.getChildren().add(deleteBtn);
        }

        return buttonsRow;
    }
    
    private Button createLikeButton(Post post) {
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
            String iconPath = dejaLike ? "/org/example/demo2/img/like_white.png" : "/org/example/demo2/img/like_svg.png";
            ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            likeIcon.setFitWidth(16);
            likeIcon.setPreserveRatio(true);
            likeIcon.setPickOnBounds(true);
            likeBtn.setGraphic(likeIcon);
        } catch (Exception e) {
        }
        
        likeBtn.setOnAction(event -> handleLikeAction(post, likeBtn));

        return likeBtn;
    }
    
    private void handleLikeAction(Post post, Button likeBtn) {
        if (!utilisateurService.estAuthentifie()) {
            if (onLoginRequired != null) {
                onLoginRequired.run();
            }
            return;
        }
        
        postService.toggleLike(post.getId_post(), utilisateurConnecteId);
    }
    
    private Button createCommentButton(Post post) {
        int nbCom = commentaireService.getNombreCommentaires(post.getId_post());
        Button commentBtn = new Button(String.valueOf(nbCom));
        commentBtn.getStyleClass().add("make-post-button-white");
        commentBtn.setStyle("-fx-text-fill: black;");
        
        try {
            ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/comment_svg.png")));
            commentIcon.setFitWidth(16);
            commentIcon.setPreserveRatio(true);
            commentIcon.setPickOnBounds(true);
            commentBtn.setGraphic(commentIcon);
        } catch (Exception e) {
        }
        
        return commentBtn;
    }
    
    private Button createReportButton(Post post) {
        Button reportBtn = new Button("Signaler");
        reportBtn.getStyleClass().add("make-post-button-white");
        reportBtn.setStyle("-fx-text-fill: #e74c3c;");
        reportBtn.setOnAction(e -> {
            signalementService.signalerPost(utilisateurConnecteId, post.getId_post());
            reportBtn.setText("Signalé");
            reportBtn.setDisable(true);
        });
        
        return reportBtn;
    }
    
    private Button createDeleteButton(Post post) {
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
                }
            });
        });
        
        return deleteBtn;
    }
    
    private VBox createCommentsSection(Post post) {
        VBox commentsSection = new VBox(5);
        commentsSection.setPadding(new Insets(10, 0, 0, 20));
        commentsSection.setVisible(false);
        commentsSection.setManaged(false);

        HBox replyBox = createReplyBox(post, commentsSection);
        commentsSection.getChildren().add(replyBox);

        List<Commentaire> commentaires = commentaireService.getCommentairesPost(post.getId_post());
        for (Commentaire com : commentaires) {
            VBox comBox = createCommentBox(com);
            commentsSection.getChildren().add(comBox);
        }

        return commentsSection;
    }
    
    private HBox createReplyBox(Post post, VBox commentsSection) {
        HBox replyBox = new HBox(10);
        
        TextField replyField = new TextField();
        replyField.setPromptText("Écrire une réponse...");
        replyField.setPrefWidth(300);
        
        Button replySubmit = new Button("Répondre");
        replySubmit.getStyleClass().add("make-post-button");
        replySubmit.setStyle("-fx-text-fill: black;");

        replySubmit.setOnAction(e -> handleSubmitComment(post, replyField, commentsSection));
        
        replyBox.getChildren().addAll(replyField, replySubmit);
        return replyBox;
    }
    
    private void handleSubmitComment(Post post, TextField replyField, VBox commentsSection) {
        String txt = replyField.getText();
        if (txt == null || txt.trim().isEmpty()) {
            return;
        }
        
        if (!utilisateurService.estAuthentifie()) {
            if (onLoginRequired != null) {
                onLoginRequired.run();
            }
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
    }
    
    private VBox createCommentBox(Commentaire com) {
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
        
        return comBox;
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
}
