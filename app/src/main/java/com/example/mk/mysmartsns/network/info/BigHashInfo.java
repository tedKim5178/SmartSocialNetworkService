package com.example.mk.mysmartsns.network.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mk on 2017-02-09.
 */

public class BigHashInfo implements Serializable{
    int bighash_no;
    String bighash_name;
    int bighash_count;   // 이건모지?

    private boolean isCheck;      // 서버에 올릴때도 이 클래스를 사용하기 때문에 선택을 했는지 안했는지 체크해주는 변수 추가

    protected BigHashInfo(Parcel in) {
        bighash_no = in.readInt();
        bighash_name = in.readString();
        bighash_count = in.readInt();
        isCheck = in.readByte() != 0;
    }


    public int getBighash_no() {
        return bighash_no;
    }

    public void setBighash_no(int bighash_no) {
        this.bighash_no = bighash_no;
    }

    public String getBighash_name() {
        return bighash_name;
    }

    public void setBighash_name(String bighash_name) {
        this.bighash_name = bighash_name;
    }

    public int getBighash_count() {
        return bighash_count;
    }

    public void setBighash_count(int bighash_count) {
        this.bighash_count = bighash_count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }


}
