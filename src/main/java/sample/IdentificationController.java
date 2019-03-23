package sample;

import classes.Users;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class IdentificationController {

    @FXML
    private Button captureButton;

    @FXML
    private ImageView imageViewWebcam;

    @FXML
    private void searchDataBase(ActionEvent event){
        //Get base de donnée
        Firestore db = FirestoreClient.getFirestore();

        //takePicture()
        Users user = new Users("Agent1", "Agent1", "Agent1");
        Controller.ChangeStage(event,getClass(), user);
    }

    @FXML
    private void initialize(){
        System.out.println("Demarrage de la page d'identification");
        Image image = new Image("eyes.jpg");
        imageViewWebcam.setImage(image);
    }
}
