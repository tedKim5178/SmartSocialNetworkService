package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-05.
 */

public class UserInfo  {

    // 한번 검토
    private int user_no;
    private String user_name;
    private char user_gender;
    private String user_profile_url;
    private String user_id;

    public UserInfo(){}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public char getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(char user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_profile_url() {
        return "profile_image/"+user_profile_url;
    }

    public void setUser_profile_url(String user_profile_url) {
        this.user_profile_url = user_profile_url;
    }



}
