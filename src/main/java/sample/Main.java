package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Initialisation de la base de donnée
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);

        URL urlIdentification = new File("src/main/resources/fxml/Identification.fxml").toURL();

        //Création de la première page
        Parent root = FXMLLoader.load(urlIdentification);
        Controller.FinishStage(root);

    }

    public static void main(String[] args) {

        nu.pattern.OpenCV.loadShared();
        launch(args);
    }
}
