package org.example.demo2.controller;
import org.example.demo2.service.PostService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Post;
import java.util.List;

// TODO:
// Create post functionality:
// 1. Capture input value
// 2. Add createPost function to button
// 3. Refetch data




import java.time.LocalDateTime;
import java.util.List;

public class AccueilController {

    private PostService postService = new PostService();  // Service qui utilise le DAO

    // Ici currentUserId = id de l'utilisateur connecté
    private int currentUserId = 1;        // Exemple, à remplacer par l'utilisateur réel
    private int currentSubredditId = 1;   // Exemple, si tu gères les sous-forums

    public VBox generateAccueil() {

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_RIGHT);
        mainBox.setPadding(new Insets(20));


        VBox createPostBox = new VBox(20);
        createPostBox.setAlignment(Pos.TOP_RIGHT);

        TextField postText = new TextField();
        postText.setPromptText("Écris un post...");
        postText.getStyleClass().add("make-post-text");

        Button postButton = new Button("Post");
        postButton.getStyleClass().add("make-post-button");


        PostGeneratorController controller = new PostGeneratorController();


        VBox postsBox = controller.generateAllPosts(postService.getAllPosts());


        postButton.setOnAction(e -> {
            String text = postText.getText();

            if (!text.isEmpty()) {


                Post newPost = new Post(
                        "Titre temporaire",  // ou ajoute un TextField pour le titre
                        text,
                        LocalDateTime.now(),
                        0,
                        currentUserId,
                        currentSubredditId
                );


                postService.createPost(newPost);

                List<Post> updatedPosts = postService.getAllPosts();
                VBox newPostsBox = controller.generateAllPosts(updatedPosts);
                mainBox.getChildren().set(1, newPostsBox);

                postText.clear();
            }
        });

        createPostBox.getChildren().addAll(postText, postButton);


        mainBox.getChildren().addAll(createPostBox, postsBox);

        return mainBox;
    }
}