package db;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public String UserID;
    public String password ;
    public User(){}

    public String getUserID() {
        return UserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }


}
