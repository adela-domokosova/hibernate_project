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
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.example.Main;
import org.example.dao.PaymentDao;
import org.example.entity.Member;
import org.example.entity.Payment;
import org.example.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PaymentController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    private Stage stage;
    private Scene scene;
    private Parent root;
    private PaymentService paymentService;
    @FXML
    public ListView<Payment> paymentListView;
    public ObservableList<Payment> paymentList;

    public PaymentController() {
        this.paymentService = new PaymentService(new PaymentDao());
    }

    public void initialize(){
        loadPayments();
    }

    private void loadPayments() {
        EntityManager em = Main.createEM();
        try {
            List<Payment> payments = paymentService.getAllPayments(em);
            paymentList = FXCollections.observableArrayList(payments);
            paymentListView.setItems(paymentList);
            LOG.info("Payment list loaded");
        } catch (Exception e) {
            LOG.error("Failed to load payments", e);
            showAlert("Error", "Failed to load payments.");
        } finally {
            if (em != null && em.isOpen()) {
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
            LOG.info("Returned to home screen");
        }catch (Exception e){
            LOG.error("Error navigating to home", e);
        }finally {
            assert em != null;
            em.close();
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String promptForInput(String title, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return (String) ((Optional<?>) result).orElse(null);
    }
}
