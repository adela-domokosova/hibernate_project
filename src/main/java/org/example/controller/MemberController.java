package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import org.example.entity.Member;
import org.example.services.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberController {
    @FXML
    private ListView<Member> memberListView;

    private MemberService memberService;
    private ObservableList<Member> memberList;

    public void initialize() {
        memberService = new MemberService();
        loadMembers();
    }

    private void loadMembers() {
        List<Member> members = memberService.getAllMembers();
        memberList = FXCollections.observableArrayList(members);
        memberListView.setItems(memberList);
    }

    @FXML
    private void onEditMember() {
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            String newFirstName = promptForInput("Edit Member", "Enter new first name:", selectedMember.getFirstName());
            String newLastName = promptForInput("Edit Member", "Enter new last name:", selectedMember.getLastName());
            String newEmail = promptForInput("Edit Member", "Enter new email:", selectedMember.getEmail());

            if (newFirstName != null && newLastName != null && newEmail != null) {
                memberService.updateMember(selectedMember.getId(), newFirstName, newLastName, newEmail);
                loadMembers();
            }
        } else {
            showAlert("No Member Selected", "Please select a member to edit.");
        }
    }

    @FXML
    public void onDeleteMember(ActionEvent event) {
        Member selectedMember = memberListView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this member?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                memberService.deleteMember(selectedMember.getId());
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
}
