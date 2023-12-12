package com.example.csc311finalproject;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormController implements Initializable {
    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label ssnLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label stateLabel;

    @FXML
    private Label zipLabel;
    @FXML
    private ImageView formImageView;

    public void initData(Client selectedClient) {
        // Populate labels with data from the selectedClient
        firstNameLabel.setText( selectedClient.getFirstName());
        lastNameLabel.setText(selectedClient.getLastName());
        ssnLabel.setText(selectedClient.getSsn());
        addressLabel.setText(selectedClient.getAddress());
        cityLabel.setText(selectedClient.getCity());
        stateLabel.setText(selectedClient.getState());
        zipLabel.setText(selectedClient.getZip());
        // Set other labels accordingly...
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image formImage = new Image(getClass().getResourceAsStream("/Form.png"));
        formImageView.setImage(formImage);
        // Leave this method empty for now, as it's not needed in this class.
    }
}
