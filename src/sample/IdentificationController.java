package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class IdentificationController {

    @FXML
    private Button captureButton;

    @FXML
    private ImageView imageViewWebcam;

    @FXML
    private void searchDataBase(ActionEvent event){
        //takePicture()
        Controller.ChangeStage(event,getClass(),"materiel.fxml");
    }
}
