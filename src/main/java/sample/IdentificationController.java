package sample;

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
        //takePicture()
        Controller.ChangeStage(event,getClass(),"Materiel.fxml");
    }

    @FXML
    private void initialize(){
        System.out.println("Demarrage de la page d'identification");
        Image image = new Image("eyes.jpg");
        imageViewWebcam.setImage(image);
    }
}
