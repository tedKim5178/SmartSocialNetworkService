package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-05.
 */

public class RegisterInfo {
    public String user_id;
    public String user_pw;
    public String user_name;
    public String user_gender;
    public String big_hash;
    public String user_profile_url;

    public RegisterInfo(){

    }

    public RegisterInfo(String id, String password, String name,
                        String gender, String user_profile_url, String big_hash){
        // 우선 url 은 뺏다..
        this.user_id = id;
        this.user_pw = password;
        this.user_name = name;
        this.user_gender = gender;
        this.user_profile_url = user_profile_url;
        this.big_hash = big_hash;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getBig_hash() {
        return big_hash;
    }

    public void setBig_hash(String big_hash) {
        this.big_hash = big_hash;
    }

    public String getUser_profile_url() {
        return user_profile_url;
    }

    public void setUser_profile_url(String user_profile_url) {
        this.user_profile_url = user_profile_url;
    }
}
