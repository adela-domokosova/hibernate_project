package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;
//otestovat odpojení DB a to ui
//entity manaera sem do každé metody??? podle prednasky. typické pro spring aplikace
//je do dobre i při spadnutí aplikace
//executor v controlleru

//použít verzování?????

//task<list<user>> new task ->override ->bindbout jeoh metody na ui update message a updateprogress
public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    //public controller konstruktor se Executors.newSinleTreadExecutor()

    public void switchToMemberList(ActionEvent event) throws IOException {

        EntityManager em = null;
        //try blok s entity manaerem

        try{
            em = Main.createEM();
            Parent root = FXMLLoader.load(getClass().getResource("/memberlist.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }cach{
            //errorbox
        }final {
            //is null -> vytvořit noveho
        }

    }

    public void handleButtonClick(ActionEvent actionEvent) {
        System.out.println("hello");
    }

}
