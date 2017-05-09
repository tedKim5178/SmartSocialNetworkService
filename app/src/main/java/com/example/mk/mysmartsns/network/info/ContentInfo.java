package com.example.mk.mysmartsns.network.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mk on 2017-02-05.
 */

public class ContentInfo implements Serializable{

    private int content_no;
    private String content_desc;
    private String content_url;
    private String host_url;
    private String content_host_no;         //추가
    private String content_host;
    private String content_host_id;
    private String content_host_profile_url;        // 추가
    private String content_date;
    private String content_width;
    private String content_height;
    private int content_like_count;
    private int content_like_flag;          // 추가
    private int content_comment_count;
    private int content_size;                // 추가
    List<BigHashInfo> hash_list;
    List<SmallHashInfo> small_hash_list;

    public ContentInfo(){
        this.small_hash_list = new ArrayList<>();
    }

    public List<SmallHashInfo> getSmallHash_list() {
        return small_hash_list;
    }

    public void setSmallHash_list(List<SmallHashInfo> smallHash_list) {
        this.small_hash_list = smallHash_list;
    }

    public String getHost_url(){
        return host_url;
    }
    public void setHost_url(String host_url){
        this.host_url = host_url;
    }
    public int getContent_no() {
        return content_no;
    }

    public void setContent_no(int content_no) {
        this.content_no = content_no;
    }

    public String getContent_desc() {
        return content_desc;
    }

    public void setContent_desc(String content_desc) {
        this.content_desc = content_desc;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getContent_host() {
        return content_host;
    }

    public void setContent_host(String content_host) {
        this.content_host = content_host;
    }

    public String getContent_date() {
        return content_date;
    }

    public void setContent_date(String content_date) {
        this.content_date = content_date;
    }

    public String getContent_width() {
        return content_width;
    }

    public void setContent_width(String content_width) {
        this.content_width = content_width;
    }

    public String getContent_height() {
        return content_height;
    }

    public void setContent_height(String content_height) {
        this.content_height = content_height;
    }

    public int getContent_like_count() {
        return content_like_count;
    }

    public void setContent_like_count(int content_like_count) {
        this.content_like_count = content_like_count;
    }

    public int getContent_comment_count() {
        return content_comment_count;
    }

    public void setContent_comment_count(int content_comment_count) {
        this.content_comment_count = content_comment_count;
    }

    public List<BigHashInfo> getHash_list() {
        return hash_list;
    }

    public void setHash_list(List<BigHashInfo> bighash_name) {
        this.hash_list = bighash_name;
    }

    public String getContent_host_no() {
        return content_host_no;
    }

    public void setContent_host_no(String content_host_no) {
        this.content_host_no = content_host_no;
    }

    public String getContent_host_profile_url() {
        return content_host_profile_url;
    }

    public void setContent_host_profile_url(String content_host_profile_url) {
        this.content_host_profile_url = content_host_profile_url;
    }

    public int getContent_like_flag() {
        return content_like_flag;
    }

    public void setContent_like_flag(int content_like_flag) {
        this.content_like_flag = content_like_flag;
    }

    public String getContent_host_id() {
        return content_host_id;
    }

    public void setContent_host_id(String content_host_id) {
        this.content_host_id = content_host_id;
    }

    public int getContent_size() {
        return content_size;
    }

    public void setContent_size(int content_size) {
        this.content_size = content_size;
    }
}
