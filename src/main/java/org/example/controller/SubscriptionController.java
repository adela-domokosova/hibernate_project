package org.example.controller;

import javafx.beans.Observable;
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
import org.example.dao.SubscriptionDao;
import org.example.entity.Member;
import org.example.entity.Payment;
import org.example.entity.Subscription;
import org.example.services.PaymentService;
import org.example.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SubscriptionController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);
    @FXML
    public ListView<Subscription> subscriptionListView;
    public ObservableList<Subscription> subscriptionList;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private SubscriptionService subscriptionService;
    private PaymentService paymentService;


    /**
     * Konstruktor
     */
    public SubscriptionController() {
        this.subscriptionService = new SubscriptionService(new SubscriptionDao());
        this.paymentService = new PaymentService(new PaymentDao());
    }


    /**
     * Inicializační metoda
     * Načte předplatná do seznamu
     */
    public void initialize(){
        loadSubscriptions();
    }

    /**
     * Načte všechna předplatná z databáze a zobrazí je v seznamu
     */
    private void loadSubscriptions() {
        EntityManager em = Main.createEM();
        try {
            List<Subscription> subscriptions = subscriptionService.getAllSubscriptions(em);
            subscriptionList = FXCollections.observableArrayList(subscriptions);
            subscriptionListView.setItems(subscriptionList);
            LOG.info("Subscription list loaded");
        } catch (Exception e) {
            LOG.error("Failed to load subscriptions", e);
            showAlert("Error", "Failed to load subscriptions.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Pomocná metoda pro zobrazení zprávy
     * Používá se pro zobrazení chyb a informací uživateli
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handler pro tlačítko návratu na hlavní obrazovku
     * Přepne zpět na úvodní menu aplikace
     */
    @FXML
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
     * Handler pro tlačítko vytvoření platby
     * Vytvoří platbu pro vybrané předplatné a označí ho jako aktivní
     */
    public void createPayment(ActionEvent actionEvent) {
        EntityManager em = Main.createEM();
        Subscription selectedSubscription = subscriptionListView.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            Optional<Subscription> existingSub = subscriptionService.getSubscriptionById(em, selectedSubscription.getId());

            if (existingSub.isEmpty()) {
                LOG.warn("Subscription no longer exists in database");
                showAlert("Subscription Not Found", "The selected subscription has been deleted by another user.");
                loadSubscriptions();
                return;
            }
            
            Subscription subscription = existingSub.get();
            if (subscription.getActive()) {
                LOG.warn("Subscription already paid");
                showAlert("Subscription is paid", "This subscription was already paid by another user.");
                loadSubscriptions();
                return;
            }
            
            Payment payment = new Payment();
            payment.setSubscription(subscription);
            Double amount = Double.valueOf(promptForInput("Enter amount", "Enter amount paid:", "amount"));
            payment.setAmount(amount);
            payment.setPaymentDate(LocalDate.now());
            
            paymentService.CreatePayment(em, payment);
            subscription.setActive(true);
            LOG.info("Created payment for subscription");
            loadSubscriptions();
        } else {
            showAlert("No Subscription Selected", "Please select a subscription to create payment.");
        }
        
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
