package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by Teacher on 03.03.2018.
 */

public class search {

 String name;
    String thumb_image;

    public search() {}

    public search(String name, String thumb_image) {
        this.name = name;
        this.thumb_image = thumb_image;
    }


    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
