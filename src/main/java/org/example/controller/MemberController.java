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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class MemberController {
    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    private MemberService memberService;
    @FXML
    private ListView<Member> memberListView;
    private ObservableList<Member> memberList;

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    /**
     * Konstruktor 
     */
    public MemberController(){
        this.memberService = new MemberService(new MemberDao());
    }

    /**
     * Inicializační metoda 
     * Načte členy do seznamu
     */
    public void initialize(){
        loadMembers();
    }

    /**
     * Načte všechny členy z databáze a zobrazí je v seznamu
     */
    private void loadMembers() {
        EntityManager em = Main.createEM();
        try {
            List<Member> members = memberService.getAllMembers(em);
            memberList = FXCollections.observableArrayList(members);
            memberListView.setItems(memberList);
            LOG.info("Member list loaded");
        } catch (Exception e) {
            LOG.error("Failed to load members", e);
            showAlert("Error", "Failed to load members.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    /**
     * Handler pro tlačítko vytvoření člena
     * Zobrazí dialogy pro zadání údajů a vytvoří nového člena
     */
    @FXML
    private void onCreateMember(ActionEvent event) {
        EntityManager em = Main.createEM();
        try {
            String newFirstName = promptForInput("new Member", "Enter new first name:", "firstname");
            String newLastName = promptForInput("new Member", "Enter new last name:", "lastname");
            String newEmail = promptForInput("new Member", "Enter new email:", "email");

            LocalDateTime dateTime = LocalDateTime.now();

            memberService.saveMember(em, newFirstName, newLastName, newEmail, dateTime);
            LOG.info("Created new member: {} {}", newFirstName, newLastName);
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

    /**
     * Handler pro tlačítko úpravy člena
     * Umožňuje editovat údaje vybraného člena
     */
    @FXML
    private void onEditMember(ActionEvent event) {
        EntityManager em = Main.createEM();
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Optional<Member> existingMember = memberService.getMemberById(em, selectedMember.getId());

            if (existingMember.isEmpty()) {
                LOG.warn("Member no longer exists in database");
                showAlert("Member Not Found", "The selected member has been deleted by another user.");
                loadMembers();
                return;
            }

            String newFirstName = promptForInput("Edit Member", "Enter new first name:", selectedMember.getFirstName());
            String newLastName = promptForInput("Edit Member", "Enter new last name:", selectedMember.getLastName());
            String newEmail = promptForInput("Edit Member", "Enter new email:", selectedMember.getEmail());

            if (newFirstName != null && newLastName != null && newEmail != null) {
                memberService.updateMember(em, selectedMember.getId(), newFirstName, newLastName, newEmail);
                LOG.info("Updated member: {} {}", newFirstName, newLastName);
                loadMembers();
            }
        } else {
            showAlert("No Member Selected", "Please select a member to edit.");
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Zobrazí detailní informace o vybraném členovi
     * Přepne na obrazovku s detaily a předplatnými člena
     */
    public void showDetail(ActionEvent event) throws IOException {
        EntityManager em = Main.createEM();
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Optional<Member> existingMember = memberService.getMemberById(em, selectedMember.getId());

            if (existingMember.isEmpty()) {
                LOG.warn("Member no longer exists in database");
                showAlert("Member Not Found", "The selected member has been deleted by another user.");
                loadMembers();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/memberdetail.fxml"));
            Parent root = loader.load();

            MemberDetailController controller = loader.getController();
            controller.setMember(selectedMember);
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            LOG.info("Opened details for member: {} {}", 
                selectedMember.getFirstName(), selectedMember.getLastName());
        } else {
            showAlert("No Member Selected", "Please select a member to show details.");
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Handler pro tlačítko smazání člena
     * Zobrazí potvrzovací dialog a smaže vybraného člena
     */
    @FXML
    public void onDeleteMember(ActionEvent event) {
        EntityManager em = Main.createEM();
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this member?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                memberService.deleteMember(em, selectedMember.getId());
                LOG.info("Deleted member: {} {}", 
                    selectedMember.getFirstName(), selectedMember.getLastName());
                loadMembers();
            }
        } else {
            showAlert("No Member Selected", "Please select a member to delete.");
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Pomocná metoda pro zobrazení dialogu pro zadání hodnoty
     */
    private String promptForInput(String title, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return (String) ((Optional<?>) result).orElse(null);
    }

    /**
     * Pomocná metoda pro zobrazení informační zprávy
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handler pro tlačítko návratu na hlavní obrazovku
     */
    public void onBackButton(ActionEvent event) {
        EntityManager em = null;

        try{
            em = Main.createEM();
            Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            LOG.info("Returned to home screen");
        }catch (Exception e){
            LOG.error("Error navigating to home", e);
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
