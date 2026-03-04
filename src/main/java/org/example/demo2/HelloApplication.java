package org.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/post-detail-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/post-create.fxml"));


//        comment je peux ajouter les donnes (for loop components comment Nextjs (data.map(item => <Component item={item} />)) en java ?
//        Scene scene = new Scene(fxmlLoader.load(), 1440, 720);

        List<Post> allPosts = PostDao.getAll();

        PostGeneratorController postGeneratorController = new PostGeneratorController();
        Scene scene = new Scene(postGeneratorController.generateAllPosts(allPosts), 1440, 720);
        scene.getStylesheets().add(HelloApplication.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
