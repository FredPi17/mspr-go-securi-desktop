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
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("materiel.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Go Securi");
            stage.setScene(new Scene(root));
            root.getStylesheets().add("style.css");
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
