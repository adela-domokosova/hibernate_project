package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static EntityManagerFactory EMF;

    static {
        try {
            EMF = Persistence.createEntityManagerFactory("punit");
            LOG.info("EntityManagerFactory initialized successfully.");
        } catch (Exception e) {
            LOG.error("Failed to initialize EntityManagerFactory", e);
            throw new RuntimeException("Could not initialize database connection.", e);
        }
    }
    public static EntityManager createEM(){
        return EMF.createEntityManager();
    }

    public static void main(String[] args) {
        LOG.info("app has started");
        EMF = Persistence.createEntityManagerFactory("punit");
        launch(args);
        EMF.close();
        LOG.info("Application terminated");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/home.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hibernate Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}