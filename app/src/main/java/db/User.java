package db;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String UserID;
    private String password;
    private byte[] picture;
    private String nickname;
    private String introduce;
    public User(){}

    public String getUserID() {
        return UserID;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getpicture() {
        return picture;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setpicture(byte[] picture){
        this.picture = picture;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

}
