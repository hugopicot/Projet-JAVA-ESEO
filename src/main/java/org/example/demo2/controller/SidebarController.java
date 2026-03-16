package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SidebarController {
    public VBox generateSidebar (String homeImage, String usersImage) {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getStyleClass().add("sidebar-wrapper");


        HBox sidebarItemHome = new HBox(15);
        sidebarItemHome.setAlignment(Pos.CENTER);
        sidebarItemHome.getStyleClass().add("sidebar-item-wrapper");
        sidebarItemHome.setPadding(new Insets(20));
        ImageView homeIcon = new ImageView();
        homeIcon.setImage(new Image(homeImage));
        Label labelHome = new Label("Home");
        sidebarItemHome.getChildren().addAll(homeIcon, labelHome);


        HBox sidebarItemUsers = new HBox(15);
        sidebarItemUsers.setAlignment(Pos.CENTER);
        sidebarItemUsers.getStyleClass().add("sidebar-item-wrapper-2");
        ImageView usersIcon = new ImageView();
        usersIcon.setImage(new Image(usersImage));
        Label usersLabel = new Label("Users");
        usersLabel.getStyleClass().add("sidebar-item-2");
        sidebarItemUsers.getChildren().addAll(usersIcon, usersLabel);

        mainBox.getChildren().addAll(sidebarItemHome, sidebarItemUsers);

        return mainBox;
    }
}
