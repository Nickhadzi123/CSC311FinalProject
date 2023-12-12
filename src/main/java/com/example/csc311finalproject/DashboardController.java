package com.example.csc311finalproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import com.google.firebase.cloud.FirestoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DashboardController implements Initializable {
    @FXML
    private Button addClientButton;
    @FXML
    private Button removeClientButton;
    @FXML
    private ImageView imageLogo;
    @FXML
    private ImageView addClientView;
    @FXML
    private ImageView removeClientView;
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> firstNameColumn, lastNameColumn, ssnColumn, telephoneColumn, emailColumn, addressColumn, cityColumn, stateColumn, zipColumn;
    @FXML
    private TableColumn<Client, Boolean> selectColumn;

    @FXML
    private void handleAddClientButtonAction() {
        showAddClientDialog();
    }

    private static final String CLIENTS_COLLECTION_NAME = "clients";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image transparentLogo = new Image(getClass().getResourceAsStream("/TaxHome-logos_transparent.png"));
        imageLogo.setImage(transparentLogo);
        Image addImage = new Image(getClass().getResourceAsStream("/AddClient.png"));
        addClientView.setImage(addImage);
        Image removeImage = new Image(getClass().getResourceAsStream("/RemoveClient.png"));
        removeClientView.setImage(removeImage);
        TableColumn<Client, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        // Initialize columns
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));

        clientsTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Double-click detected
                    Client selectedClient = row.getItem();
                    openDetailsScene(selectedClient);
                }
            });
            return row;
        });

        // Load clients into the table
        loadClients();

        removeClientButton.setOnAction(event -> handleDeleteButtonAction());
    }
    private void openDetailsScene(Client selectedClient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("formview.fxml"));
            Parent root = loader.load();
            FormController formController = loader.getController();
            formController.initData(selectedClient);

            Stage stage = new Stage();
            stage.setTitle("Client Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClients() {
        Firestore firestore = FirestoreClient.getFirestore();
        List<Client> clients = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = firestore.collection("clients").get().get();

            for (QueryDocumentSnapshot document : querySnapshot) {
                String firstName = document.getString("firstName");
                String lastName = document.getString("lastName");
                String ssn = document.getString("ssn");
                String telephone = document.getString("telephone");
                String email = document.getString("email");
                String address = document.getString("address");
                String city = document.getString("city");
                String state = document.getString("state");
                String zip = document.getString("zip");

                clients.add(new Client(firstName, lastName, ssn, telephone, email, address, city, state, zip));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ObservableList<Client> observableClients = FXCollections.observableArrayList(clients);
        clientsTable.setItems(observableClients);
    }

    private void showAddClientDialog() {
        try {
            // Load the FXML file for the add client window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddClientView.fxml"));
            Parent root = loader.load();

            // Create a new stage for the add client window
            Stage addClientStage = new Stage();
            addClientStage.initModality(Modality.APPLICATION_MODAL);
            addClientStage.setTitle("Add Client");
            addClientStage.setScene(new Scene(root));

            // Get the controller for the add client window
            AddClientController addClientController = loader.getController();
            // Set any necessary references or parameters in the controller

            // Show the add client window
            addClientStage.showAndWait();

            // After the add client window is closed, you can refresh the TableView if needed
            loadClients();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButtonAction() {
        // Get the selected client from the table view
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Prompt the user for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Client");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete the client: " + selectedClient.getFirstName() + "?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, proceed with deletion
                boolean success = DatabaseHandler.deleteClient(selectedClient.getFirstName());

                if (success) {
                    // Update the table view or perform any other necessary actions
                    System.out.println("Client deleted successfully");
                } else {
                    System.err.println("Error deleting client");
                    // Handle error if needed
                }
            }
        } else {
            // No client selected, show a warning
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Client Selected");
            alert.setHeaderText("No Client Selected");
            alert.setContentText("Please select a client to delete.");
            alert.showAndWait();
        }


    }
}

