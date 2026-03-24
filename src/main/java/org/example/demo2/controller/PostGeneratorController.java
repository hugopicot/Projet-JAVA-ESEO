package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo2.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostGeneratorController {
    public VBox generatePost (Post post){

        VBox mainBox = new VBox(20);
        Insets mainBoxPadding = new Insets(20);
        mainBox.setPadding(mainBoxPadding);
        mainBox.setAlignment(Pos.valueOf("CENTER_LEFT"));
        mainBox.getStyleClass().add("post-item");

        HBox userBox = new HBox(5);
        userBox.setAlignment(Pos.valueOf("CENTER_LEFT"));
        ImageView userImageBox = new ImageView();
        int userId = post.getId_utilisateur();
        Label userLabel = new Label("User " + userId);
        userBox.getChildren().addAll(userImageBox, userLabel);



        String postTitle = post.getTitre();
        Label titleLabel = new Label(postTitle);
        titleLabel.getStyleClass().add("post-item-title");


        String postContenu = post.getContenu();
        Label postContent = new Label(postContenu);
        postContent.getStyleClass().add("post-item-paragraph");



        mainBox.getChildren().addAll(userBox, titleLabel, postContent);

        return mainBox;
    }

    public VBox generateAllPosts(List<Post> posts) {
        VBox allPostsBox = new VBox();
        List<VBox> postsBoxes = new ArrayList<>(List.of());

        for (Post item : posts) {
            postsBoxes.add(this.generatePost(item));
        };

        allPostsBox.getChildren().addAll(postsBoxes);

        return allPostsBox;
    }
}
