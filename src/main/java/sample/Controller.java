package sample;

import classes.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    public static void ChangeStage(ActionEvent event, Class classe, String fxmlPage){
        try {
            System.out.println("Change page");
            Parent root = FXMLLoader.load(classe.getResource("/fxml/"+fxmlPage));
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
            FXMLLoader loader = new FXMLLoader(classe.getResource("/fxml/Materiel.fxml"));
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
