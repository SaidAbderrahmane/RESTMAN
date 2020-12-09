package RESTMAN;

/************************************************************************************/

public class Ingredients {
    String ingredient_nom;
    float quantite;
    String unite;
    int prix;

    public Ingredients(String ingredient_nom, float quantite, int prix, String unite) {
        this.ingredient_nom = ingredient_nom;
        this.quantite = quantite;
        this.unite = unite;
        this.prix = prix;
    }

    public String getIngredient_nom() {
        return ingredient_nom;
    }

    public void setIngredient_nom(String ingredient_nom) {
        this.ingredient_nom = ingredient_nom;
    }

    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

}
