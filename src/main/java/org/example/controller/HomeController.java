package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//otestovat odpojení DB a to ui
//entity manaera sem??? podle prednasky. typické pro spring aplikace
//je do dobre i při spadnutí aplikace
public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMemberList(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/memberlist.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleButtonClick(ActionEvent actionEvent) {
        System.out.println("hello");
    }

}
