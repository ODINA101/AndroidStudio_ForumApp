package app.iraklisamniashvilii.com.geoforum.models;

/**
 * Created by irakli on 1/21/2018.
 */

public class postReplyModel  {
    public String username;
    public String photo;
    public String content;
    public Long date;
    public String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

        public postReplyModel() {

        }
    public postReplyModel(String uid,String username, String photo, String content, Long date) {
        this.username = username;
        this.photo = photo;
        this.content = content;
        this.date = date;
        this.uid = uid;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
