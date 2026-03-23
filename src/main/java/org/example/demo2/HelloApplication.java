package org.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo2.controller.AccueilController;
import org.example.demo2.controller.NavBarController;
import org.example.demo2.controller.SidebarController;
import org.example.demo2.dao.PostDao;
import org.example.demo2.model.Post;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        // 1️⃣ Structure principale
        VBox mainScene = new VBox(); // contiendra navbar + main (sidebar + accueil)

        // 2️⃣ Créer les controllers
        NavBarController navBarController = new NavBarController();
        AccueilController accueilController = new AccueilController();
        SidebarController sidebarController = new SidebarController();

        // 3️⃣ Images pour navbar et sidebar
        String navbarLogoImage = HelloApplication.class.getResource("img/nova_logo_new.png").toExternalForm();
        String homeImage = HelloApplication.class.getResource("img/home_svg.png").toExternalForm();
        String usersImage = HelloApplication.class.getResource("img/user_svg.png").toExternalForm();

        // 4️⃣ Charger les styles
        String css = HelloApplication.class.getResource("styles.css").toExternalForm();

        // 5️⃣ Générer la navbar
        HBox navbar = navBarController.generateNavbar(navbarLogoImage);

        // 6️⃣ Générer la zone accueil (posts + formulaire)
        VBox accueil = accueilController.generateAccueil();

        // 7️⃣ Générer la sidebar
        VBox sidebar = sidebarController.generateSidebar(homeImage, usersImage);

        // 8️⃣ Mettre sidebar et accueil dans HBox principale
        HBox main = new HBox();
        main.getChildren().addAll(sidebar, accueil); // sidebar à gauche, accueil à droite

        // 9️⃣ Ajouter navbar + main à la scène principale
        mainScene.getChildren().addAll(navbar, main);

        // 10️⃣ Créer la scène
        Scene scene = new Scene(mainScene, 1440, 1000);
        scene.getStylesheets().add(css); // ajouter ton CSS si tu en as

        // 11️⃣ Configurer la stage
        stage.setTitle("Application de Posts");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}