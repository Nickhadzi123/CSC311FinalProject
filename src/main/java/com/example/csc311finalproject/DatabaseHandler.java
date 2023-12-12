package com.example.csc311finalproject;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler {
    private static final String PROJECT_ID = "csc311capstone";
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String CLIENTS_COLLECTION_NAME = "clients";

    private static Firestore usersFirestore;
    private static Firestore clientsFirestore;

    static {
        initializeUsersFirestore();
        initializeClientsFirestore();
    }

    private static void initializeUsersFirestore() {
        initializeFirestore(USERS_COLLECTION_NAME);
    }

    private static void initializeClientsFirestore() {
        initializeFirestore(CLIENTS_COLLECTION_NAME);
    }

    private static void initializeFirestore(String collectionName) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount =
                        new FileInputStream("src/main/resources/csc311capstone.json");

                FirebaseOptions firestoreOptions = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId(PROJECT_ID)
                        .build();

                FirebaseApp.initializeApp(firestoreOptions);
            }

            if (collectionName.equals(USERS_COLLECTION_NAME) && usersFirestore == null) {
                usersFirestore = FirestoreClient.getFirestore();

            } else if (collectionName.equals(CLIENTS_COLLECTION_NAME) && clientsFirestore == null) {
                clientsFirestore = FirestoreClient.getFirestore();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password, String firstName, String lastName, String email, String zipCode) {
        // Check if the username already exists
        if (userExists(username)) {
            return false; // Username already taken
        }

        // Create a new user document in the "users" collection
        DocumentReference docRef = usersFirestore.collection(USERS_COLLECTION_NAME).document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("email", email);
        data.put("zipCode", zipCode);

        ApiFuture<WriteResult> result = docRef.set(data);

        try {
            // Wait for the write operation to complete
            result.get();
            return true; // Registration successful
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    public boolean authenticateUser(String username, String password) {
        // Query the "users" collection and check if the username and password match
        CollectionReference users = usersFirestore.collection(USERS_COLLECTION_NAME);
        Query query = users.whereEqualTo("username", username).whereEqualTo("password", password);

        try {
            QuerySnapshot querySnapshot = query.get().get();
            return !querySnapshot.isEmpty(); // User exists if the query result is not empty
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean userExists(String username) {
        // Check if a user with the given username already exists
        DocumentReference docRef = usersFirestore.collection(USERS_COLLECTION_NAME).document(username);

        try {
            DocumentSnapshot documentSnapshot = docRef.get().get();
            return documentSnapshot.exists();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteClient(String firstName) {
        Firestore firestore = FirestoreClient.getFirestore();

        // Delete the client document from the "clients" collection
        DocumentReference docRef = firestore.collection(CLIENTS_COLLECTION_NAME)
                .document(firstName);

        ApiFuture<WriteResult> result = docRef.delete();

        try {
            // Wait for the delete operation to complete
            result.get();
            System.out.println("Client deleted successfully");
            return true; // Deleting client successful
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.err.println("Error deleting client: " + e.getMessage());
            return false; // Deleting client failed
        }
    }
        // Implement methods for adding, deleting, and editing clients using Firestore
}

