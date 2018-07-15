package ma.mghandi.radeesportail;

/**
 * Created by mghandi on 20/10/2017.
 */

public class ItemMenu {
    private String name;
    private int thumbnail;

    public ItemMenu(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
