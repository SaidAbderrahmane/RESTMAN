package RESTMAN;

/*********************************************************************************************************/

public class Preparation {
    private int num_etape;
    private String nom_etape;
    private int tempsNec;
    private String description;

    Preparation(int num_etape, String nom_etape, int tempsNec, String description) {
        this.num_etape = num_etape;
        this.nom_etape = nom_etape;
        this.tempsNec = tempsNec;
        this.description = description;
    }

    public int getNum_etape() {
        return num_etape;
    }

    public void setNum_etape(int num_etape) {
        this.num_etape = num_etape;
    }

    public String getNom_etape() {
        return nom_etape;
    }

    public void setNom_etape(String nom_etape) {
        this.nom_etape = nom_etape;
    }

    public int getTempsNec() {
        return tempsNec;
    }

    public void setTempsNec(int tempsNec) {
        this.tempsNec = tempsNec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
