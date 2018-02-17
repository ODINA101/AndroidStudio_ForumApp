package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by Teacher on 17.02.2018.
 */

public class ReplycommentsModel {
    String uid;
    String content;
    Long date;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public ReplycommentsModel() {

    }


    public ReplycommentsModel(String uid,Long date, String content) {
        this.uid = uid;
        this.content = content;
        this.date = date;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
