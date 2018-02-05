package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by irakli on 1/19/2018.
 */

public class postModel {
    public String title;
    public String des;
    public String date;
    public String uid;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public postModel() {
    }

    public postModel(String title, String des, String date, String uid,String name) {
        this.title = title;
        this.des = des;
        this.date = date;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
