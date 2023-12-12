package com.example.csc311finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RegistrationController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private TextField zipCodeField;
    @FXML private Button registerButtonTwo;

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void registerButtonClicked() {
        if (showTermsAndConditionsDialog()) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String zipCode = zipCodeField.getText();

            if (validateName(firstName, "First Name") && validateName(lastName, "Last Name") &&
                    validateUsername(username) && validatePassword(password) &&
                    validateEmail(email) && validateZipCode(zipCode)) {
                // Proceed with registration
                if (databaseHandler.registerUser(username, password, firstName, lastName, email, zipCode)) {
                    // Registration successful
                    showConfirmationDialog(firstName);
                    // Close the registration window
                    Stage stage = (Stage) firstNameField.getScene().getWindow();
                    stage.close();
                } else {
                    showErrorDialog("Registration Failed", "Could not register user. Please try again.");
                }
            } else {
                showErrorDialog("Invalid Input", "Please enter valid information in all fields.");
            }
        }
        else{

            loadLoginScene();

        }
    }
    private boolean showTermsAndConditionsDialog() {
        // Create a custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Terms and Conditions");
        dialog.setHeaderText("Please read and agree to the terms and conditions.");

        // Set the expandable content (terms and conditions text)
        TextArea textArea = new TextArea(
                "Welcome to TaxHome!\n" + "\n" +
                        " These Terms and Conditions (\"Terms'\") govern your use of the TaxHome mobile application and website (collectively, the \"Service\"), operated by TaxHome Technologies Inc. ('us\", \"we\", or\"our\").By accessing or using the Service, you agree to be bound by these Terms. If you disagree with any part of the Terms, you do not have permission to access the Service. \n" +
                        "\n"+"TaxHome is a digital platform designed to provide users with tax preparation and filing services. The service includes, but is not limited to, tax form preparation, submission assistance, and personalized tax advice.\n" + "\n" +
                        "To use TaxForm, you must register for an account. You are responsible for maintaining the confidentiality of your account and password and for restricting access to your computer or device. You agree to accept responsibility for all activities that occur under your account or password\n" + "\n" +
                        "You agree not to use the service: \n" + "\n" +
                        "For any unlawful purposes, to solicit others to perform unlawful acts. To violate any international or national regulations, rules, laws or local ordinances. To infringe upon or violate our international property rights or the intellectual property rights of others."
        );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expandableContent = new GridPane();
        expandableContent.setMaxWidth(Double.MAX_VALUE);
        expandableContent.add(textArea, 0, 0);

        // Set the dialog content
        dialog.getDialogPane().setContent(expandableContent);

        // Add buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = dialog.showAndWait();

        // Return true if the user clicked OK (agreed to terms)
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Tax Application - Login");
            stage.show();

            // Close the current registration window (optional)
            Stage registrationStage = (Stage) registerButtonTwo.getScene().getWindow();
            registrationStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateName(String name, String fieldName) {
        // Example: Name must contain only letters and be 2 to 30 characters long
        String regex = "^[a-zA-Z]{2,30}$";
        if (!name.matches(regex)) {
            showErrorDialog("Invalid " + fieldName, "Please enter a valid " + fieldName + ".");
            return false;
        }
        return true;
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

    private boolean validateEmail(String email) {
        // Example: Basic email validation
        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    private boolean validateZipCode(String zipCode) {
        // Example: Zip code must be a 5-digit number
        String regex = "^[0-9]{5}$";
        return zipCode.matches(regex);
    }

    private void showConfirmationDialog(String firstName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText("Welcome to TaxHome, " + firstName + "!");
        alert.setContentText("Please login to get started.");

        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

