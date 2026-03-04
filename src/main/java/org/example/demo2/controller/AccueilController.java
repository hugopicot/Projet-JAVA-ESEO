package org.example.demo2.controller;

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

public class AccueilController {
    public VBox generateAccueil(List<Post> allPosts) {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_RIGHT);
        mainBox.setPadding(new Insets(20));

        VBox createPostBox = new VBox(20);
        createPostBox.setAlignment(Pos.TOP_RIGHT);
        createPostBox.setPadding(new Insets(20));

        // 1. Capture value here
        TextField postText = new TextField();
        postText.setPromptText("Make a post...");
        postText.getStyleClass().add("make-post-text");


        // 2. Add createPost function here
        Button postButton = new Button("Post");
        postButton.getStyleClass().add("make-post-button");
        //3. Re fetch data: how do I re fetch the data from here ?

        createPostBox.getChildren().addAll(postText, postButton);

        // posts box
        PostGeneratorController postGeneratorController = new PostGeneratorController();
        VBox postsBoxes = postGeneratorController.generateAllPosts(allPosts);

        mainBox.getChildren().addAll(createPostBox, postsBoxes);
        return mainBox;
    }
}
