package sample;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import classes.Materiaux;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../../resources/Identification.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Identification.fxml"));
        primaryStage.setTitle("Go Securi");
        primaryStage.setScene(new Scene(root));
        root.getStylesheets().add("style.css");
        primaryStage.show();

        BaseDeDonnes();
    }

    public void BaseDeDonnes() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            Firestore db = FirestoreClient.getFirestore();

           /* Exemple pour ajouter un objet
            DocumentReference docRef = db.collection("Materiaux").document("GantsIntervention");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("Nom","Gants d'intervention");
            data.put("Quantite",10);
            ApiFuture<WriteResult> result = docRef.set(data);*/

            ApiFuture<QuerySnapshot> query = db.collection("Materiaux").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for(QueryDocumentSnapshot document : documents){
                System.out.println("Materiaux : "+document.getId());
                System.out.println("Nom : "+document.getString("Nom"));
                System.out.println("Quantite : "+document.getLong("Quantite"));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
