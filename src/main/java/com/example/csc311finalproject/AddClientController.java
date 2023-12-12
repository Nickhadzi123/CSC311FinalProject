package com.example.csc311finalproject;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AddClientController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ssnField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField zipField;
    @FXML
    private Button saveButton;

    private static final String CLIENTS_COLLECTION_NAME = "clients";

    @FXML
    private void handleSaveButtonAction() {
        // Retrieve data from text fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String ssn = ssnField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();

        // Validate the input if needed

        // Call a method to add the client to Firestore
        boolean added = addClientToFirestore(firstName, lastName, ssn, telephone, email, address, city, state, zip);

        // If the client is successfully added, close the add client window
        if (added) {
            closeAddClientWindow();
        }
        // You can handle the case where adding the client failed if needed
    }

    private boolean addClientToFirestore(String firstName, String lastName, String ssn, String telephone,
                                         String email, String address, String city, String state, String zip) {
        // Get the Firestore instance
        Firestore firestore = FirestoreClient.getFirestore();

        // Create a data map with client details
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("ssn", ssn);
        data.put("telephone", telephone);
        data.put("email", email);
        data.put("address", address);
        data.put("city", city);
        data.put("state", state);
        data.put("zip", zip);

        // Add the data to the "clients" collection in Firestore
        try {
            DocumentReference documentReference = firestore.collection(CLIENTS_COLLECTION_NAME).document();
            documentReference.set(data, SetOptions.merge()).get();
            return true; // Client added successfully
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Adding client failed
        }
    }

    private void closeAddClientWindow() {
        // Retrieve the stage associated with this controller and close it
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
