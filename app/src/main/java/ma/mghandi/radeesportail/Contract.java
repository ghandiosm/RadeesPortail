package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 10/03/2018.
 */

public class Contract {
    private String id;
    private String contract_number;
    private String tourne_number;
    private String gerance;
    private String localite;
    private String nature;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getTourne_number() {
        return tourne_number;
    }

    public void setTourne_number(String tourne_number) {
        this.tourne_number = tourne_number;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setListData(Map<String,Object> classObj){
        this.contract_number = classObj.get("contract_number").toString();
        this.tourne_number = classObj.get("tourne_num").toString();
    }

}
