package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by Teacher on 11.02.2018.
 */

public class MessagesModel {
    public MessagesModel() {

    }


    String uid;
    String txt;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public MessagesModel(String uid,String txt) {
        this.uid = uid; this.txt = txt;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
