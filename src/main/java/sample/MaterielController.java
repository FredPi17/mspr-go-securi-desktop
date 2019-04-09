package sample;

import classes.Materiaux;
import classes.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class MaterielController {

    @FXML
    private Button btnRetourIdentification;

    private Users CurUser;
    private Firestore db;
    private Bucket bucket;
    private ObservableList<Materiaux> LstComparaisonMateriaux;

    @FXML
    private ImageView ImgVPhotoUtilisateur;

    @FXML
    private ListView lstMateriel;

    @FXML
    private void BackToIdentification(ActionEvent event) {
        SaveBaseDeDonnee();
        Controller.ChangeStage(event, getClass(), "Identification.fxml");
    }

    public void initData(Users user){
        CurUser = user;
        initializing();
    }

    @FXML
    private void initialize() {
        System.out.println("Demarrage de la page Materiel");
        //Image image = new Image("eyes.jpg");
        //ImgVPhotoUtilisateur.setImage(image);
        ArrayList<Materiaux> list = new ArrayList<Materiaux>();
        ObservableList<Materiaux> ObListMateriaux = FXCollections.observableArrayList(list);
        lstMateriel.setItems(ObListMateriaux);
    }

    private void initializing() {
        ArrayList<Materiaux> list = BaseDeDonnees();
        LstComparaisonMateriaux = FXCollections.observableArrayList(list);
        lstMateriel.setItems(LstComparaisonMateriaux);

        //Put check in checkboxes and disable out of stock items
        lstMateriel.setCellFactory(listview -> {
            CheckBoxListCell<Materiaux> cell = new CheckBoxListCell<>();
            cell.setSelectedStateCallback(Materiaux::isUsedProperty);

            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if(cell.getItem() != null){
                    //disable if out of stock and not in used
                    cell.setDisable(cell.getItem().isOutOfStock() && !cell.getItem().isUsed());
                    if(cell.isDisabled()){
                        cell.setTextFill(Color.GRAY);
                    }
                    else {
                        cell.setTextFill(Color.BLACK);
                    }
                }
            });

            return cell;
        });

        Predicate<Materiaux> isChecked = Materiaux::isUsed;
        LstComparaisonMateriaux = lstMateriel.getItems().filtered(isChecked);
    }

    private void SaveBaseDeDonnee() {


        //Recuperation des materiaux qui sont Cocher
        Predicate<Materiaux> isChecked = Materiaux::isUsed;
        ObservableList<Materiaux> newlist = lstMateriel.getItems().filtered(isChecked);

        DocumentReference userRef = db.collection("Agents").document(CurUser.getId());

        for(Materiaux m : newlist){
            if(!LstComparaisonMateriaux.contains(m)){
                //Ajouter a la base de donnée
                DocumentReference matRef = db.collection("Materiaux").document(m.getId());
                matRef.update("Sortie", m.getSortie() + 1);

                userRef.update(m.getNom(), true);
            }
        }

        for(Materiaux m : LstComparaisonMateriaux){
            if(!newlist.contains(m)){
                //Enlever de la base de donnée
                DocumentReference docRef = db.collection("Materiaux").document(m.getId());
                docRef.update("Sortie", m.getSortie() - 1);

                userRef.update(m.getNom(), false);
            }
        }
    }

    private ArrayList<Materiaux> BaseDeDonnees() {
        ArrayList<Materiaux> materiaux = new ArrayList<Materiaux>();

        try {
            //Get base de donnée
            db = FirestoreClient.getFirestore();

            //Recuper tous les materiaux
            ApiFuture<QuerySnapshot> query = db.collection("Materiaux").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                String id = document.getId();
                String nom = document.getString("Nom");
                Long quantite = document.getLong("Quantite");
                Long sortie = document.getLong("Sortie");
                Materiaux m = new Materiaux(id, nom, quantite.intValue(), sortie.intValue());
                materiaux.add(m);
            }

            //Cherche les infos de l'utilisateur connecté
            ApiFuture<DocumentSnapshot> documentUser = db.collection("Agents").document(CurUser.getId()).get();
            DocumentSnapshot documentSnapshotUsers = documentUser.get();

            for (Materiaux m: materiaux) {
                if(documentSnapshotUsers.contains(m.getNom())){
                    if(documentSnapshotUsers.getBoolean(m.getNom())){
                        m.setUsed(true);
                    }
                    else {
                        m.setUsed(false);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return materiaux;
    }
}
