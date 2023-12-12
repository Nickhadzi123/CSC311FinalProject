package com.example.csc311finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label registerLabel;

    private DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML
    private void initialize() {
        // Load the image when the controller is initialized
        Image logoImage = new Image(getClass().getResourceAsStream("/TaxHome-logos.jpeg"));
        logoImageView.setImage(logoImage);
        registerLabel.setOnMouseClicked(event -> loadRegistrationScene());
    }

    @FXML
    private void loginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateUsername(username) && validatePassword(password)) {
            // Proceed with login
            if (databaseHandler.authenticateUser(username, password)) {
                // Switch to the dashboard view upon successful login
                loadDashboardScene();
            } else {
                showErrorDialog("Login Failed", "Invalid username or password.");
            }
        } else {
            showErrorDialog("Invalid Input", "Please enter valid username and password.");
        }
    }

    @FXML
    private void registerButtonClicked() {
        // Load the registration view
        loadRegistrationScene();
    }

    private void loadRegistrationScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrationView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Tax Application - Registration");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadDashboardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Tax Application - Dashboard");
            stage.show();

            // Close the login window (optional)
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean validateUsername(String username) {
        // Example: Username must contain only letters and numbers, 3 to 20 characters
        String regex = "^[a-zA-Z0-9]{3,20}$";
        return username.matches(regex);
    }

    private boolean validatePassword(String password) {
        // Example: Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(regex);
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
