package classes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Materiaux {
    private String id;
    private String nom;
    private Integer quantite;
    private Integer sortie;
    private final BooleanProperty outOfStock = new SimpleBooleanProperty();
    private final BooleanProperty used = new SimpleBooleanProperty();

    public Materiaux(String id, String nom, Integer quantite, Integer sortie){
        this.id = id;
        this.nom = nom;
        this.quantite = quantite;
        setSortie(sortie);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getSortie() {
        return sortie;
    }

    public void setSortie(Integer sortie) {
        this.sortie = sortie;
        if(sortie.equals(quantite)){
            setOutOfStock(true);
        }
        else {
            setOutOfStock(false);
        }
    }

    @Override
    public String toString() {
        return nom; //+", "+quantite + ", " + sortie;
    }

    public final BooleanProperty isUsedProperty(){
        return this.used;
    }

    public final boolean isUsed(){
        return this.isUsedProperty().get();
    }

    public final void setUsed(boolean use){
        this.isUsedProperty().set(use);
    }

    public final BooleanProperty outOfStockProperty() {
        return this.outOfStock;
    }

    public final boolean isOutOfStock() {
        return this.outOfStockProperty().get();
    }

    private final void setOutOfStock(final boolean outOfStock) {
        this.outOfStockProperty().set(outOfStock);
    }

}
