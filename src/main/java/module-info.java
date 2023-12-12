module com.example.csc311finalproject {
    opens com.example.csc311finalproject to google.cloud.firestore, javafx.fxml;
    requires javafx.controls;
    requires javafx.fxml;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires java.logging;
    requires com.google.api.apicommon;
    requires com.google.auth;
    requires google.cloud.core;
    requires jsr305;
    requires org.apache.pdfbox;
    requires java.desktop;


    exports com.example.csc311finalproject;
}