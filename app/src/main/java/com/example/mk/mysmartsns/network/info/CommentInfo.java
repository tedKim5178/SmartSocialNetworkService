package com.example.mk.mysmartsns.network.info;

/**
 * Created by mk on 2017-02-23.
 */

public class CommentInfo {
    private String user_no;
    private String user_profile_url;
    private String user_id;
    private String uc_comment_name;

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getUser_profile() {
        return user_profile_url;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile_url = user_profile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_name() {
        return uc_comment_name;
    }

    public void setComment_name(String comment_name) {
        this.uc_comment_name = comment_name;
    }
}
