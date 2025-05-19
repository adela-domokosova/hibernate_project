package org.example.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.Main;
import org.example.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;

public class PaymentController {
    private static  final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private Stage stage;
    private Scene scene;
    private Parent root;
    //TODO: paymentservices
    @FXML
    private ListView<Member> PaymentListView;
    private ObservableList<Member> PaymentList;


    public void switchToHome(ActionEvent event) throws IOException {

        EntityManager em = null;
        //try blok s entity manaerem

        try{
            em = Main.createEM();
            Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            //errorbox
        }finally {
            LOG.info("done");
            assert em != null;
            em.close();
            //is null -> vytvořit noveho
        }

    }
}
