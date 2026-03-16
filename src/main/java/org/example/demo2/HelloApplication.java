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
    public void start(Stage stage) throws IOException, SQLException {
        VBox mainScene = new VBox();
        NavBarController navBarController = new NavBarController();
        AccueilController accueilController = new AccueilController();
        SidebarController sidebarController = new SidebarController();

        String navbarLogoImage = HelloApplication.class.getResource("img/nova_logo_new.png").toExternalForm();
        String homeImage = HelloApplication.class.getResource("img/home_svg.png").toExternalForm();
        String usersImage = HelloApplication.class.getResource("img/user_svg.png").toExternalForm();
        List<Post> allPosts = PostDao.getAll();
        String css = HelloApplication.class.getResource("styles.css").toExternalForm();

        HBox navbar = navBarController.generateNavbar(navbarLogoImage);
        VBox accueil = accueilController.generateAccueil(allPosts);
        VBox sidebar = sidebarController.generateSidebar(homeImage, usersImage);
        HBox main = new HBox();

        main.getChildren().addAll(sidebar, accueil);

        mainScene.getChildren().addAll(navbar, main);


        Scene scene = new Scene(mainScene, 1440, 720);
        scene.getStylesheets().add(css);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
