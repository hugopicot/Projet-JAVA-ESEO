package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NavBarController {
    public HBox generateNavbar(String image) {
        HBox mainBox = new HBox(400);
        mainBox.setAlignment(Pos.valueOf("CENTER"));
//        mainBox.setId("navbar-wrapper");
        Insets mainBoxPadding = new Insets(20);
        mainBox.setPadding(mainBoxPadding);

        // ImageView
        ImageView novaLogo = new ImageView();
        Image novaLogoImage = new Image(image);
        novaLogo.setImage(novaLogoImage);

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.getStyleClass().add("navbar-searchbar");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-login");

        mainBox.getChildren().addAll(novaLogo, searchBar, loginButton);
        return mainBox;
    }
}
