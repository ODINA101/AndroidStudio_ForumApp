package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by odina on 2/18/2018.
 */

public class NotiModel {
    String content;
    String uid;

    public NotiModel(String content, String uid) {
        this.content = content;
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
