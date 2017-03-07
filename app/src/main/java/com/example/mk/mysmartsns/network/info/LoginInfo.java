package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-05.
 */

public class LoginInfo {
    String user_id;
    String user_pw;

    public LoginInfo(String user_id, String user_password){
        this.user_id = user_id;
        this.user_pw = user_password;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }
}
