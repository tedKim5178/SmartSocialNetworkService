package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-05.
 */

public class RegisterInfo {
    public String user_id;
    public String user_pw;
    public String user_name;
    public int user_age;
    public String user_gender;
    public String user_profile_url;
    public int user_interest_bighash1;
    public int user_interest_bighash2;
    public int user_interest_bighash3;

    public RegisterInfo(){

    }

    public RegisterInfo(String id, String password, String name, int age,
                        String gender, int user_interest_bighash1, int user_interest_bighash2, int user_interest_bighash3,String user_profile_url){
        // 우선 url 은 뺏다..
        this.user_id = id;
        this.user_pw = password;
        this.user_name = name;
        this.user_age = age;
        this.user_gender = gender;
        this.user_profile_url = user_profile_url;
        this.user_interest_bighash1 = user_interest_bighash1;
        this.user_interest_bighash2 = user_interest_bighash2;
        this.user_interest_bighash3 = user_interest_bighash3;
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

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public int getUser_interest_bighash1() {
        return user_interest_bighash1;
    }

    public void setUser_interest_bighash1(int user_interest_bighash1) {
        this.user_interest_bighash1 = user_interest_bighash1;
    }

    public int getUser_interest_bighash2() {
        return user_interest_bighash2;
    }

    public void setUser_interest_bighash2(int user_interest_bighash2) {
        this.user_interest_bighash2 = user_interest_bighash2;
    }

    public int getUser_interest_bighash3() {
        return user_interest_bighash3;
    }

    public void setUser_interest_bighash3(int user_interest_bighash3) {
        this.user_interest_bighash3 = user_interest_bighash3;
    }

    public String getUser_profile_url() {
        return user_profile_url;
    }

    public void setUser_profile_url(String user_profile_url) {
        this.user_profile_url = user_profile_url;
    }
}
