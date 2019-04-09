package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.FileInputStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Initialisation de la base de données
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("mspr-java-8b250.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        //Création de la première page
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Identification.fxml"));
        Controller.FinishStage(root);
    }

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        launch(args);
    }
}
