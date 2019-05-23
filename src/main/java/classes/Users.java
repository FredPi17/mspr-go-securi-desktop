package classes;

import javafx.scene.image.Image;

public class Users {
    private String Id;
    private String Nom;
    private String Prenom;
    private Image Image;

    public Users(Image image, String id, String nom, String prenom) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
        Image = image;
    }

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        this.Image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }
}
