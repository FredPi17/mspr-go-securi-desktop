package classes;

public class Users {
    private String Id;
    private String Nom;
    private String Prenom;

    public Users(String id, String nom, String prenom) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
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
