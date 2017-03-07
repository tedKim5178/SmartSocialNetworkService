package com.example.mk.mysmartsns.network.info;

/**
 * Created by gilsoo on 2017-02-14.
 */
public class SmallHashInfo {
    private String smallhash_no;
    private String smallhash_name;
    private String smallhash_count;             // 디비에 bighash와 같이 연관되게 저장해도 보내줄땐 COUNT함수 써서 보내줄까?

    public String getSmallhash_no() {
        return smallhash_no;
    }

    public void setSmallhash_no(String smallhash_no) {
        this.smallhash_no = smallhash_no;
    }

    public String getSmallhash_name() {
        return smallhash_name;
    }

    public void setSmallhash_name(String smallhash_name) {
        this.smallhash_name = smallhash_name;
    }

    public String getSmallhash_count() {
        return smallhash_count;
    }

    public void setSmallhash_count(String smallhash_count) {
        this.smallhash_count = smallhash_count;
    }
}
