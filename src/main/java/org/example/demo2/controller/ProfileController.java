package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Post;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.PostService;
import org.example.demo2.service.UtilisateurService;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contrôleur pour la page profil utilisateur.
 */
public class ProfileController {

    public interface ProfileCallback {
        void onBackToHome();
        void onPostClick(Post post);
    }

    private UtilisateurService utilisateurService;
    private PostService postService;
    private Utilisateur utilisateur;
    private ProfileCallback callback;
    private DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProfileController(Utilisateur utilisateur, ProfileCallback callback) {
        this.utilisateur = utilisateur;
        this.callback = callback;
        this.utilisateurService = new UtilisateurService();
        this.postService = new PostService();
    }

    public VBox generateView() {
        VBox root = new VBox();
        root.setSpacing(0);
        root.setStyle("-fx-background-color: white;");

        // Conteneur principal avec HGrow
        HBox mainContent = new HBox();
        mainContent.setSpacing(0);

        // Colonne principale du profil
        VBox profileContent = createProfileContent();
        mainContent.getChildren().add(profileContent);
        HBox.setHgrow(profileContent, Priority.ALWAYS);

        // Colonne latérale avec les statistiques
        VBox statsSidebar = createStatsSidebar();
        mainContent.getChildren().add(statsSidebar);

        root.getChildren().add(mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        return root;
    }

    private VBox createProfileContent() {
        VBox container = new VBox();
        container.setSpacing(20);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: white;");

        // En-tête du profil
        HBox header = createProfileHeader();
        container.getChildren().add(header);

        // Séparateur
        Separator separator = new Separator();
        container.getChildren().add(separator);

        // Section des posts de l'utilisateur
        Label postsTitle = new Label("Mes publications");
        postsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");
        container.getChildren().add(postsTitle);

        VBox postsContainer = new VBox();
        postsContainer.setSpacing(15);
        
        List<Post> userPosts = postService.getPostsParUtilisateur(utilisateur.getId());
        
        if (userPosts.isEmpty()) {
            Label noPostsLabel = new Label("Aucune publication pour le moment.");
            noPostsLabel.setStyle("-fx-text-fill: #888; -fx-font-style: italic; -fx-padding: 20;");
            postsContainer.getChildren().add(noPostsLabel);
        } else {
            for (Post post : userPosts) {
                VBox postCard = createUserPostCard(post);
                postsContainer.getChildren().add(postCard);
            }
        }
        
        container.getChildren().add(postsContainer);

        return container;
    }

    private HBox createProfileHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");

        // Avatar
        VBox avatarBox = new VBox();
        avatarBox.setAlignment(Pos.CENTER);
        try {
            ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/org/example/demo2/img/user_svg.png")));
            avatar.setFitWidth(80);
            avatar.setPreserveRatio(true);
            avatarBox.getChildren().add(avatar);
        } catch (Exception e) {
            Label avatarPlaceholder = new Label("");
            avatarPlaceholder.setStyle("-fx-font-size: 48;");
            avatarBox.getChildren().add(avatarPlaceholder);
        }

        // Informations utilisateur
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label pseudoLabel = new Label(utilisateur.getPseudo());
        pseudoLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label emailLabel = new Label(utilisateur.getEmail());
        emailLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");

        String dateInscription = utilisateur.getDateInscription() != null 
            ? utilisateur.getDateInscription().toLocalDateTime().format(DATE_FORMAT) 
            : "Non disponible";
        Label dateLabel = new Label("Member since " + dateInscription);
        dateLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");

        // Karma calculated as the sum of the user's posts scores
        List<Post> userPosts = postService.getPostsParUtilisateur(utilisateur.getId());
        int karmaCalcule = userPosts.stream().mapToInt(Post::getScore).sum();
        Label karmaLabel = new Label("Karma: " + karmaCalcule);
        karmaLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #f39c12; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(pseudoLabel, emailLabel, dateLabel, karmaLabel);

        header.getChildren().addAll(avatarBox, infoBox);

        return header;
    }

    private VBox createStatsSidebar() {
        VBox sidebar = new VBox();
        sidebar.setSpacing(20);
        sidebar.setPadding(new Insets(30));
        sidebar.setMinWidth(250);
        sidebar.setStyle("-fx-background-color: #f8f9fa;");

        Label statsTitle = new Label("Statistiques");
        statsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Nombre de posts
        List<Post> userPosts = postService.getPostsParUtilisateur(utilisateur.getId());
        int nbPosts = userPosts.size();
        VBox postsStats = createStatItem("Publications", String.valueOf(nbPosts));

        // Karma calculated as the sum of the user's posts scores
        int karmaCalcule = userPosts.stream().mapToInt(Post::getScore).sum();
        VBox karmaStats = createStatItem("Karma", String.valueOf(karmaCalcule));

        sidebar.getChildren().addAll(statsTitle, postsStats, karmaStats);

        return sidebar;
    }

    private VBox createStatItem(String label, String value) {
        VBox item = new VBox(5);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        item.getChildren().addAll(titleLabel, valueLabel);

        return item;
    }

    private VBox createUserPostCard(Post post) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-cursor: hand;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label(post.getDate_creation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
        dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #888;");

        header.getChildren().add(dateLabel);

        Label titleLabel = new Label(post.getTitre());
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);

        Label contentLabel = new Label(post.getContenu());
        contentLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");
        contentLabel.setWrapText(true);

        // Limiter la longueur du contenu
        if (contentLabel.getText().length() > 150) {
            contentLabel.setText(contentLabel.getText().substring(0, 150) + "...");
        }

        HBox footer = new HBox(20);
        footer.setAlignment(Pos.CENTER_LEFT);

        Label scoreLabel = new Label(post.getScore() + " points");
        scoreLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #f39c12;");

        footer.getChildren().add(scoreLabel);

        card.getChildren().addAll(header, titleLabel, contentLabel, footer);

        // Action au clic
        card.setOnMouseClicked(e -> {
            if (callback != null) {
                callback.onPostClick(post);
            }
        });

        return card;
    }
}