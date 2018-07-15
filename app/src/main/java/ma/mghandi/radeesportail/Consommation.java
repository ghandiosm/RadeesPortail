package ma.mghandi.radeesportail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by mghandi on 19/03/2018.
 */

public class Consommation {
    private String id;
    private String name;
    private String consommation;
    private String index;
    private String periode;
    private String tourne_num;

    public Consommation() {
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

    public String getConsommation() {
        return consommation;
    }

    public void setConsommation(String consommation) {
        this.consommation = consommation;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getTourne_num() {
        return tourne_num;
    }

    public void setTourne_num(String tourne_num) {
        this.tourne_num = tourne_num;
    }

    public void setListData(Map<String,Object> classObj){
        this.id = classObj.get("id").toString();
        this.name = classObj.get("name").toString();
        this.tourne_num = classObj.get("tourne_num").toString();
        this.periode = classObj.get("period").toString();
        this.index = classObj.get("index").toString();
        this.consommation = classObj.get("consumption").toString();
    }



}
