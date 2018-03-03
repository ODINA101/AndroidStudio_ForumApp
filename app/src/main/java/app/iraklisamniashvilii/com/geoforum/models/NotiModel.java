package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by odina on 2/18/2018.
 */

public class NotiModel {
    String content;
    Long date;
   String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public NotiModel() {}


    public NotiModel(String content, Long date,String uid) {
        this.content = content;
        this.date = date;
        this.uid = uid;
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
