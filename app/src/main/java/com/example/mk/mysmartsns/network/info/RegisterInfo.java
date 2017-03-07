package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-05.
 */

public class RegisterInfo {
    public String user_id;
    public String user_pw;
    public String user_name;
    public String user_gender;
    public String user_interest_bighash1;
    public String user_interest_bighash2;
    public String user_interest_bighash3;
    public String user_profile_url;

    public RegisterInfo(){

    }

    public RegisterInfo(String id, String password, String name,
                        String gender, String user_profile_url, String bighash1, String bighash2, String bighash3){
        // 우선 url 은 뺏다..
        this.user_id = id;
        this.user_pw = password;
        this.user_name = name;
        this.user_gender = gender;
        this.user_profile_url = user_profile_url;
        this.user_interest_bighash1 = bighash1;
        this.user_interest_bighash2 = bighash2;
        this.user_interest_bighash3 = bighash3;
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

    public String getUser_interest_bighash1() {
        return user_interest_bighash1;
    }

    public void setUser_interest_bighash1(String user_interest_bighash1) {
        this.user_interest_bighash1 = user_interest_bighash1;
    }

    public String getUser_interest_bighash2() {
        return user_interest_bighash2;
    }

    public void setUser_interest_bighash2(String user_interest_bighash2) {
        this.user_interest_bighash2 = user_interest_bighash2;
    }

    public String getUser_interest_bighash3() {
        return user_interest_bighash3;
    }

    public void setUser_interest_bighash3(String user_interest_bighash3) {
        this.user_interest_bighash3 = user_interest_bighash3;
    }

    public String getUser_profile_url() {
        return user_profile_url;
    }

    public void setUser_profile_url(String user_profile_url) {
        this.user_profile_url = user_profile_url;
    }
}
