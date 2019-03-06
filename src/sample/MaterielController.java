package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MaterielController {

    @FXML
    private Button btnRetourIdentification;

    @FXML
    private ImageView ImgVPhotoUtilisateur;

    @FXML
    private ListView lstMateriel;

    @FXML
    private void BacktoIdentification(ActionEvent event){
        Controller.ChangeStage(event, getClass(), "Identification.fxml");
    }

}
