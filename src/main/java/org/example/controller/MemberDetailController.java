package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Main;
import org.example.dao.MemberDao;
import org.example.dao.SubscriptionDao;
import org.example.entity.Member;
import org.example.entity.Subscription;
import org.example.services.MemberService;
import org.example.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MemberDetailController {
    private static  final Logger LOG = LoggerFactory.getLogger(MemberDetailController.class);
    private MemberService memberService;
    private SubscriptionService subscriptionService;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Member member;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label registrationLabel;
    @FXML
    private ListView<Subscription> subscriptionsListView;
    public MemberDetailController(){
        this.memberService = new MemberService(new MemberDao());
        this.subscriptionService = new SubscriptionService(new SubscriptionDao());
    }

    public void setMember(Member member) {
        this.member = member;
        LOG.info("Member set: " + member);
        updateMemberDetails();
    }

    private void initialize(){
    }


    private void updateMemberDetails() {
        EntityManager em = null;
        try {
            em = Main.createEM();
            if (member != null) {
                nameLabel.setText("Name: " + member.getFirstName() + " " + member.getLastName());
                emailLabel.setText("Email: " + member.getEmail());
                registrationLabel.setText("Registration date: " + member.getRegistrationDate().toString());

                List<Subscription> subscriptions = subscriptionService.getSubscriptionsByMember(em, member);

                ObservableList<Subscription> observableList = FXCollections.observableArrayList(subscriptions);
                subscriptionsListView.setItems(observableList);

                subscriptionsListView.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(Subscription s, boolean empty) {
                        super.updateItem(s, empty);
                        if (empty || s == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            VBox box = new VBox();
                            box.setSpacing(5);
                            box.setPadding(new Insets(10));
                            box.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-color: #f9f9f9;");

                            Label typeLabel = new Label("Type of subscription: " + s.getSubscriptionType());
                            Label activeLabel = new Label("Active: " + (s.getActive() ? "Yes" : "No"));
                            Label startLabel = new Label("Start date: " + s.getStartDate());
                            Label createdLabel = new Label("Created: " + s.getCreatedDate());
                            Label priceLabel = new Label("Price: " + s.getPrice() + " Kč");

                            typeLabel.setStyle("-fx-font-weight: bold");

                            box.getChildren().addAll(typeLabel, activeLabel, startLabel, createdLabel, priceLabel);
                            setGraphic(box);
                        }
                    }
                });
            }

        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            LOG.info("done");
            if (em != null) {
                em.close();
            }
        }
    }


    public void switchToHome(ActionEvent event) throws IOException {
        EntityManager em = null;
        try{
            em = Main.createEM();
            Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
        }finally {
            LOG.info("done");
            assert em != null;
            em.close();
        }

    }
}
