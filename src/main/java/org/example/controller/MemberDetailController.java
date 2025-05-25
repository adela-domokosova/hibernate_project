package org.example.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.Main;
import org.example.dao.MemberDao;
import org.example.entity.Member;
import org.example.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;

public class MemberDetailController {
    private static  final Logger LOG = LoggerFactory.getLogger(MemberDetailController.class);
    private MemberService memberService;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Member member;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label registrationLabel;
    public MemberDetailController(){
        this.memberService = new MemberService(new MemberDao());
    }

    public void setMember(Member member) {
        this.member = member;
        LOG.info("Member set: " + member);
        updateMemberDetails();
    }

    private void initialize(){
    }


    private void updateMemberDetails() {
        if (member != null) {
            nameLabel.setText("Name: " + member.getFirstName() + " " + member.getLastName());
            emailLabel.setText("Email: " + member.getEmail());
            registrationLabel.setText("Registration date: " + member.getRegistrationDate().toString());
        }
        LOG.info("Labels: name={}, email={}, registration={}",
                member.getFirstName(), member.getEmail(), member.getRegistrationDate());
    }

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
