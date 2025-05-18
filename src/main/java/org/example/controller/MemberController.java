package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.persistence.Persistence;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.example.Main;
import org.example.dao.MemberDao;
import org.example.entity.Member;
import org.example.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class MemberController {
    private static  final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private MemberService memberService;
    @FXML
    private ListView<Member> memberListView;
    private ObservableList<Member> memberList;

    private Stage stage;
    private Scene scene;
    private Parent root;
    public MemberController(){
        this.memberService = new MemberService(new MemberDao());
    }

    public void initialize(){
        loadMembers();
    }

    private void loadMembers() {
        EntityManager em = Main.createEM();
        try {
            List<Member> members = memberService.getAllMembers(em);
            memberList = FXCollections.observableArrayList(members);
            memberListView.setItems(memberList);
        } catch (Exception e) {
            LOG.error("Failed to load members", e);
            showAlert("Error", "Failed to load members.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @FXML
    private void onCreateMember(ActionEvent event) {
        EntityManager em = Main.createEM();
        try {
            String newFirstName = promptForInput("new Member", "Enter new first name:", "firstname");
            String newLastName = promptForInput("new Member", "Enter new last name:", "lastname");
            String newEmail = promptForInput("new Member", "Enter new email:", "email");


            LocalDateTime dateTime = LocalDateTime.now();

            memberService.saveMember(em, newFirstName, newLastName, newEmail, dateTime);
            LOG.info("info given to services layer");
            loadMembers();
        } catch (Exception e) {
            em.getTransaction().rollback();
            LOG.error("Error creating member", e);
            showAlert("Error", "Failed to create member.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @FXML
    private void onEditMember(ActionEvent event) {
        EntityManager em = Main.createEM();
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Optional<Member> existingMember = memberService.getMemberById(em, selectedMember.getId());

            // Kontrola, zda záznam stále existuje
            if (existingMember.isEmpty()) {
                showAlert("Member Not Found", "The selected member has been deleted by another user.");
                loadMembers();
                return;
            }

            String newFirstName = promptForInput("Edit Member", "Enter new first name:", selectedMember.getFirstName());
            String newLastName = promptForInput("Edit Member", "Enter new last name:", selectedMember.getLastName());
            String newEmail = promptForInput("Edit Member", "Enter new email:", selectedMember.getEmail());

            if (newFirstName != null && newLastName != null && newEmail != null) {
                memberService.updateMember(em, selectedMember.getId(), newFirstName, newLastName, newEmail);
                loadMembers();
            }
        } else {
            showAlert("No Member Selected", "Please select a member to edit.");
        }
    }

    @FXML
    public void onDeleteMember( ActionEvent event) {
        EntityManager em = Main.createEM();
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this member?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                memberService.deleteMember(em, selectedMember.getId());
                loadMembers();
            }
        } else {
            showAlert("No Member Selected", "Please select a member to delete.");
        }
    }


    private String promptForInput(String title, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return (String) ((Optional<?>) result).orElse(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void onBackButton(ActionEvent event) {
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
            //is null -> vytvořit noveho
        }

    }
}
