package db;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
    public String UserID;
    public String password;
    public byte[] picture;
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
    
}
