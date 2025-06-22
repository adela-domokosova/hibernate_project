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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.Main;
import org.example.dao.MemberDao;
import org.example.dao.PaymentDao;
import org.example.dao.SubscriptionDao;
import org.example.entity.Member;
import org.example.entity.Payment;
import org.example.entity.Subscription;
import org.example.entity.SubscriptionType;
import org.example.services.MemberService;
import org.example.services.PaymentService;
import org.example.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private MemberService memberService;


    /**
     * Konstruktor
     */
    public SubscriptionController() {
        this.subscriptionService = new SubscriptionService(new SubscriptionDao());
        this.paymentService = new PaymentService(new PaymentDao());
        this.memberService = new MemberService(new MemberDao());
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
            subscription.setActiveTrue();
            LOG.info("Created payment for subscription");
            loadSubscriptions();
        } else {
            showAlert("No Subscription Selected", "Please select a subscription to create payment.");
        }
        
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    
    /**
     * Handler pro tlačítko vytvoření nového předplatného
     * Zobrazí dialog pro výběr člena a zadání údajů o předplatném
     */
    @FXML
    public void createSubscription(ActionEvent actionEvent) {
        EntityManager em = Main.createEM();
        try {
            List<Member> members = memberService.getAllMembers(em);
            
            if (members.isEmpty()) {
                showAlert("No Members", "There are no members in the system. Please create a member first.");
                return;
            }
            
            Dialog<Subscription> dialog = new Dialog<>();
            dialog.setTitle("Create New Subscription");
            dialog.setHeaderText("Select a member and enter subscription details");
            
            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
            
            ComboBox<Member> memberComboBox = new ComboBox<>();
            memberComboBox.setItems(FXCollections.observableArrayList(members));
            memberComboBox.setCellFactory(new Callback<ListView<Member>, ListCell<Member>>() {
                @Override
                public ListCell<Member> call(ListView<Member> param) {
                    return new ListCell<Member>() {
                        @Override
                        protected void updateItem(Member item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getFirstName() + " " + item.getLastName());
                            }
                        }
                    };
                }
            });
            memberComboBox.setButtonCell(new ListCell<Member>() {
                @Override
                protected void updateItem(Member item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getFirstName() + " " + item.getLastName());
                    }
                }
            });
            
            ComboBox<SubscriptionType> typeComboBox = new ComboBox<>();
            typeComboBox.setItems(FXCollections.observableArrayList(SubscriptionType.values()));
            
            TextField priceField = new TextField();
            priceField.setPromptText("Price");
            
            DatePicker startDatePicker = new DatePicker();
            startDatePicker.setValue(LocalDate.now());
            
            grid.add(new Label("Member:"), 0, 0);
            grid.add(memberComboBox, 1, 0);
            grid.add(new Label("Type:"), 0, 1);
            grid.add(typeComboBox, 1, 1);
            grid.add(new Label("Price:"), 0, 2);
            grid.add(priceField, 1, 2);
            grid.add(new Label("Start Date:"), 0, 3);
            grid.add(startDatePicker, 1, 3);
            
            dialog.getDialogPane().setContent(grid);
            
            memberComboBox.requestFocus();
            
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    Member selectedMember = memberComboBox.getValue();
                    SubscriptionType selectedType = typeComboBox.getValue();
                    String priceText = priceField.getText();
                    LocalDate startDate = startDatePicker.getValue();
                    
                    if (selectedMember == null || selectedType == null || priceText.isEmpty() || startDate == null) {
                        showAlert("Missing Information", "Please fill in all fields.");
                        return null;
                    }
                    
                    try {
                        Double price = Double.parseDouble(priceText);
                        
                        Subscription subscription = new Subscription();
                        subscription.setMember(selectedMember);
                        subscription.setSubscriptionType(selectedType);
                        subscription.setPrice(price);
                        subscription.setStartDate(startDate);
                        subscription.setCreatedDate(LocalDateTime.now());
                        subscription.setActive(false);
                        
                        return subscription;
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Price", "Please enter a valid number for price.");
                        return null;
                    }
                }
                return null;
            });
            
            Optional<Subscription> result = dialog.showAndWait();
            result.ifPresent(subscription -> {
                try {
                    em.getTransaction().begin();
                    em.persist(subscription);
                    em.getTransaction().commit();
                    LOG.info("Created new subscription for member: {} {}", 
                        subscription.getMember().getFirstName(), 
                        subscription.getMember().getLastName());
                    loadSubscriptions();
                } catch (Exception e) {
                    LOG.error("Failed to create subscription", e);
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    showAlert("Error", "Failed to create subscription: " + e.getMessage());
                }
            });
            
        } catch (Exception e) {
            LOG.error("Error in create subscription dialog", e);
            showAlert("Error", "An error occurred: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
