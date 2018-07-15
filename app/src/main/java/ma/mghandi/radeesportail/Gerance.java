package ma.mghandi.radeesportail;

import java.util.Map;

/**
 * Created by mghandi on 10/03/2018.
 */

public class Gerance {
    private String id;
    private String name;

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

    public void setListData(Map<String,Object> classObj){
        this.id = classObj.get("id").toString();
        this.name = classObj.get("name").toString();
    }

}
