package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by odina on 2/18/2018.
 */

public class NotiModel {
    String content;
    Long date;


    public NotiModel() {}
    public NotiModel(String content, Long date) {
        this.content = content;
        this.date = date;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
