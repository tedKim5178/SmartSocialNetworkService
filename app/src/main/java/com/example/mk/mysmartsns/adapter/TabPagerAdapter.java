package com.example.mk.mysmartsns.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.mk.mysmartsns.activity.LoginActivity;
import com.example.mk.mysmartsns.fragment.fragment__search.FriendSearchFragment;
import com.example.mk.mysmartsns.fragment.fragment__search.HashTagSearchFragment;
import com.example.mk.mysmartsns.network.info.BigHashInfo;

import java.util.ArrayList;

/**
 * Created by mk on 2017-02-02.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private int tabCount;
    private int smallhash_no;
    private int bighash_no;
    private int judge;
    String hash;
    ArrayList<BigHashInfo> bigHash_list;

    public TabPagerAdapter(FragmentManager fm, int tabCount, String hash, ArrayList<BigHashInfo> bighash_list, int bighash_no, int smallhash_no, int judge){
        super(fm);
        this.tabCount = tabCount;
        this.hash = hash;
        this.bigHash_list = bighash_list;
        this.smallhash_no = smallhash_no;
        this.bighash_no = bighash_no;
        this.judge = judge;
    }


    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "[탭확인] position : " + position);
        switch (position){
            case 0 :
                HashTagSearchFragment tabFragment1 = HashTagSearchFragment.newInstance(hash, bigHash_list, bighash_no, smallhash_no, judge);
                return tabFragment1;
            case 1:
                FriendSearchFragment tabFragment2 = new FriendSearchFragment();
                return tabFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return  tabCount;
    }
}
