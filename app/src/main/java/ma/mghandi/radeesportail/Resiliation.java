package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 10/03/2018.
 */

public class Resiliation {
    private String id;
    private String name;
    private String contract_number;
    private String gerance;
    private String localite;
    private String nature;
    private String etat;
    private String date;

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

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getGerance() {
        return gerance;
    }

    public void setGerance(String gerance) {
        this.gerance = gerance;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setListData(Map<String,Object> classObj){
        this.name = classObj.get("name").toString();
        this.contract_number = classObj.get("contract_number").toString();
        this.etat = classObj.get("state").toString();
    }

}
