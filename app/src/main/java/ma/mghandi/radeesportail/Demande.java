package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 10/03/2018.
 */

public class Demande {
    private String id;
    private String name;
    private String nature;
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
        this.name = classObj.get("name").toString();
        this.nature = classObj.get("nature").toString();
        this.date = classObj.get("date").toString();
    }

}
