package sample;

import classes.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Controller {

    public static void ChangeStage(ActionEvent event, Class classe, String fxmlPage){
        try {
            System.out.println("Change page");
            URL urlDestination = new File("src/main/resources/fxml/" + fxmlPage).toURL();
            Parent root = FXMLLoader.load(urlDestination);
            FinishStage(root);
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
            catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ChangeStage(ActionEvent event, Class classe, Users users){
        try {
            System.out.println("Change page");
            URL urlMateriel = new File("src/main/resources/fxml/Materiel.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(urlMateriel);
            Parent root = loader.load();
            MaterielController controller = loader.getController();
            controller.initData(users);
            FinishStage(root);
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
                catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FinishStage(Parent root){
        Stage stage = new Stage();
        stage.setTitle("Go Securi");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        root.getStylesheets().add("style.css");
        stage.show();
    }
}
