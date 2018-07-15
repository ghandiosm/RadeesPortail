package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 10/03/2018.
 */

public class Reclamation {
    private String id;
    private String name;
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setListData(Map<String,Object> classObj){
        this.name = classObj.get("name").toString();
        this.category = classObj.get("cin").toString();
        this.date = classObj.get("date").toString();
    }
}
