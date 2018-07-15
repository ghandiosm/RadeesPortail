package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 16/01/2018.
 */

public class Facture {
    private String id;
    private String name;
    private String date;
    private String montant;


    public Facture() {
    }

    public Facture(String id, String name, String date, String montant) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.montant = montant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }


    public void setListData(Map<String,Object> classObj){
        this.name = classObj.get("number").toString();
        this.date = classObj.get("date_invoice").toString();
        this.montant = classObj.get("amount_total").toString();
    }

    /*
    public void setDetailsData(Map<String,Object> classObj){
        this.name = classObj.get("name").toString();
        this.street = classObj.get("street").toString();
        this.street2 = classObj.get("street2").toString();
        this.city = classObj.get("city").toString();
        this.phone = classObj.get("phone").toString();
        this.mobile = classObj.get("mobile").toString();
        this.email = classObj.get("email").toString();
        this.website = classObj.get("website").toString();
    }
    */


}
