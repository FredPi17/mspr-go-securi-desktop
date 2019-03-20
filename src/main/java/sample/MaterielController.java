package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import classes.Materiaux;

public class MaterielController {

    @FXML
    private Button btnRetourIdentification;

    @FXML
    private ImageView ImgVPhotoUtilisateur;

    @FXML
    private ListView lstMateriel;

    @FXML
    private void BackToIdentification(ActionEvent event){
        Controller.ChangeStage(event, getClass(), "Identification.fxml");
    }

    @FXML
    private void initialize(){
        System.out.println("Demarrage de la page Materiel");
        Image image = new Image("eyes.jpg");
        ImgVPhotoUtilisateur.setImage(image);

       /* ListView<Materiaux> listView = new ListView<Materiaux>();
        for (int i=1; i<=20; i++) {
            Materiaux item = new Materiaux("Item "+i, 5);

            // observe item's on property and display message if it changes:
            /*item.onProperty().addListener((obs, wasOn, isNowOn) -> {
                System.out.println(item.getNom() + " changed on state from "+wasOn+" to "+isNowOn);
            });

            listView.getItems().add(item);
        }*/

        Materiaux m = new Materiaux("Object1",5);
        m.setOn(false);
        Materiaux m1 = new Materiaux("Object2",10);
        m1.setOn(true);
        Materiaux m2 = new Materiaux("Object3",0);

        ObservableList<Materiaux> materiaux = FXCollections.observableArrayList(m,m1);

        lstMateriel.setItems(materiaux);

        //Put checkboxes
        lstMateriel.setCellFactory(CheckBoxListCell.forListView(new Callback<Materiaux, ObservableValue<Boolean>>() {
            public ObservableValue<Boolean> call(Materiaux materiaux) {
                return materiaux.onProperty();
            }
        }));
    }

}
