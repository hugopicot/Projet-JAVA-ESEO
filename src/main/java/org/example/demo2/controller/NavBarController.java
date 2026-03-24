package org.example.demo2.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.util.Pair;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.UtilisateurService;

public class NavBarController {
    
    public interface LoginCallback {
        void onLoginSuccess(Utilisateur user);
        void onLogout();
        void onRegisterSuccess(Utilisateur user);
        void onProfileClick();
    }
    
    private HBox userSection;
    private Label usernameLabel;
    private Button loginButton;
    private Button logoutButton;
    private Button profileButton;
    private Button createAccountButton;
    private UtilisateurService service;
    private LoginCallback callback;
    
    public HBox generateNavbar(String image, UtilisateurService service, LoginCallback callback) {
        this.service = service;
        this.callback = callback;
        
        HBox mainBox = new HBox(50);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(50);
        Insets mainBoxPadding = new Insets(20);
        mainBox.setPadding(mainBoxPadding);
        mainBox.setStyle("-fx-background-color: #EAEEF7; -fx-min-height: 80px;");
        HBox.setHgrow(mainBox, Priority.ALWAYS);

        ImageView novaLogo = new ImageView();
        Image novaLogoImage = new Image(image);
        novaLogo.setImage(novaLogoImage);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.getStyleClass().add("navbar-searchbar");

        userSection = new HBox(10);
        userSection.setAlignment(Pos.CENTER);

        loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-login");
        loginButton.setOnAction(e -> showLoginDialog());

        createAccountButton = new Button("Creer un compte");
        createAccountButton.getStyleClass().add("button-login");
        createAccountButton.setOnAction(e -> showRegisterDialog());

        logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button-login");
        logoutButton.setVisible(false);
        logoutButton.setManaged(false);
        logoutButton.setOnAction(e -> handleLogout());

        profileButton = new Button("Profil");
        profileButton.getStyleClass().add("button-login");
        profileButton.setVisible(false);
        profileButton.setManaged(false);
        profileButton.setOnAction(e -> {
            if (callback != null) {
                callback.onProfileClick();
            }
        });

        usernameLabel = new Label();
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db;");

        userSection.getChildren().addAll(loginButton, createAccountButton);

        mainBox.getChildren().addAll(novaLogo, searchBar, userSection);
        
        updateAuthState();
        
        return mainBox;
    }
    
    private void handleLogout() {
        service.logout();
        callback.onLogout();
        updateAuthState();
    }
    
    public void updateAuthState() {
        if (service.estAuthentifie()) {
            Utilisateur user = service.getUtilisateurConnecte();
            usernameLabel.setText(user.getPseudo());
            userSection.getChildren().clear();
            userSection.getChildren().addAll(usernameLabel, profileButton, logoutButton);
            profileButton.setVisible(true);
            profileButton.setManaged(true);
            logoutButton.setVisible(true);
            logoutButton.setManaged(true);
        } else {
            userSection.getChildren().clear();
            userSection.getChildren().addAll(loginButton, createAccountButton);
            profileButton.setVisible(false);
            profileButton.setManaged(false);
            logoutButton.setVisible(false);
            logoutButton.setManaged(false);
        }
    }
    
    public void showLoginDialogWithCallback(Runnable onLoginSuccessCallback) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connexion");
        dialog.setHeaderText("Entrez vos identifiants");

        ButtonType loginButtonType = new ButtonType("Connexion", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(emailField.getText(), passwordField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(credentials -> {
            String email = credentials.getKey();
            String password = credentials.getValue();
            
            boolean success = service.login(email, password);
            if (success) {
                Utilisateur user = service.getUtilisateurConnecte();
                updateAuthState();
                callback.onLoginSuccess(user);
                if (onLoginSuccessCallback != null) {
                    onLoginSuccessCallback.run();
                }
            }
        });
    }
    
    private void showLoginDialog() {
        showLoginDialogWithCallback(null);
    }
    
    private void showRegisterDialog() {
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Creer un compte");
        dialog.setHeaderText("Entrez vos informations");

        ButtonType registerButtonType = new ButtonType("Creer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Pseudo");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmer mot de passe");

        grid.add(new Label("Pseudo:"), 0, 0);
        grid.add(pseudoField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Mot de passe:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Confirmer:"), 0, 3);
        grid.add(confirmPasswordField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                String pseudo = pseudoField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                
                if (pseudo.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    return null;
                }
                
                if (!password.equals(confirmPassword)) {
                    return null;
                }
                
                boolean success = service.inscrire(pseudo, email, password);
                if (success) {
                    service.login(email, password);
                    return service.getUtilisateurConnecte();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            if (user != null) {
                updateAuthState();
                callback.onRegisterSuccess(user);
            }
        });
    }
}
