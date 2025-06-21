package org.example.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.application.Platform;
import org.example.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.function.Consumer;

public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private Stage stage;
    private Scene scene;
    private Parent root;
    private volatile boolean taskRunning = false;
    @FXML
    private Button memberListButton;
    @FXML
    private Button subscriptionListButton;
    @FXML
    private Button paymentListButton;
    private void setButtonsDisabled(boolean disabled) {
        if (memberListButton != null) {
            memberListButton.setDisable(disabled);
        }
        if (subscriptionListButton != null) {
            subscriptionListButton.setDisable(disabled);
        }
        if (paymentListButton != null) {
            paymentListButton.setDisable(disabled);
        }
    }

    protected <T> void runTask(Task<T> task, Consumer<T> onSuccess) {
        if (taskRunning) {
            LOG.warn("Task already running, ignoring new request.");
            return;
        }

        taskRunning = true;
        Platform.runLater(() -> setButtonsDisabled(true));

        task.setOnSucceeded(e -> {
            taskRunning = false;
            Platform.runLater(() -> {
                setButtonsDisabled(false);
                onSuccess.accept(task.getValue());
            });
        });

        task.setOnFailed(e -> {
            taskRunning = false;
            Platform.runLater(() -> {
                setButtonsDisabled(false);
                LOG.error("Task failed: " + e.getSource().getException().getMessage(), e.getSource().getException());
            });
        });

        new Thread(task).start();
    }

    public void switchToMemberList(ActionEvent event) {
        runTask(new Task<Parent>() {
            @Override
            protected Parent call() throws Exception {
                EntityManager em = null;
                try {
                    em = Main.createEM();
                    Parent root = FXMLLoader.load(getClass().getResource("/memberlist.fxml"));
                    return root;
                } finally {
                    if (em != null) {
                        LOG.info("Closing EntityManager for MemberList.");
                        em.close();
                    }
                }
            }
        }, root -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }

    public void switchToSubscriptionList(ActionEvent event) {
        runTask(new Task<Parent>() {
            @Override
            protected Parent call() throws Exception {
                EntityManager em = null;
                try {
                    em = Main.createEM();
                    Parent root = FXMLLoader.load(getClass().getResource("/subscriptionlist.fxml"));
                    return root;
                } finally {
                    if (em != null) {
                        LOG.info("Closing EntityManager for SubscriptionList.");
                        em.close();
                    }
                }
            }
        }, root -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }

    public void switchToPaymentList(ActionEvent event) {
        runTask(new Task<Parent>() {
            @Override
            protected Parent call() throws Exception {
                EntityManager em = null;
                try {
                    em = Main.createEM();
                    Parent root = FXMLLoader.load(getClass().getResource("/paymentlist.fxml"));
                    return root;
                } finally {
                    if (em != null) {
                        LOG.info("Closing EntityManager for PaymentList.");
                        em.close();
                    }
                }
            }
        }, root -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}