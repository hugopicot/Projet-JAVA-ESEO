package org.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo2.controller.NavBarController;
import org.example.demo2.controller.PostGeneratorController;
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
        PostGeneratorController postGeneratorController = new PostGeneratorController();


        String navbarLogoImage = HelloApplication.class.getResource("img/nova_logo_new.png").toExternalForm();
        List<Post> allPosts = PostDao.getAll();


        HBox navbar = navBarController.generateNavbar(navbarLogoImage);
        VBox posts = postGeneratorController.generateAllPosts(allPosts);


        mainScene.getChildren().addAll(navbar, posts);


        Scene scene = new Scene(mainScene, 1440, 720);
        scene.getStylesheets().add(HelloApplication.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
