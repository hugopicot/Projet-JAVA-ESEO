package org.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo2.dao.PostDao;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/post-detail-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/post-create.fxml"));

//        PostDao dao = new PostDao();
//        System.out.println("Start");
//        dao.getAll().forEach(c -> {
//            System.out.println("1.: " + c.getId_post());
//        });
//        System.out.println("Finish");

//        comment je peux ajouter les donnes (for loop components comment Nextjs (data.map(item => <Component item={item} />)) en java ?
        Scene scene = new Scene(fxmlLoader.load(), 1440, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
