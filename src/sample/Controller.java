package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    public static void ChangeStage(ActionEvent event, Class classe, String fxmlPage){
        Parent root;
        try {
            System.out.println("Change page");
            root = FXMLLoader.load(classe.getResource(fxmlPage));
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
