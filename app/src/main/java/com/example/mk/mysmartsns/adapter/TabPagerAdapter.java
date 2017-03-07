package com.example.mk.mysmartsns.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.mk.mysmartsns.activity.LoginActivity;
import com.example.mk.mysmartsns.fragment.fragment__search.FriendSearchFragment;
import com.example.mk.mysmartsns.fragment.fragment__search.HashTagSearchFragment;

/**
 * Created by mk on 2017-02-02.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private int tabCount;
    String hash;

    public TabPagerAdapter(FragmentManager fm, int tabCount, String hash){
        super(fm);
        this.tabCount = tabCount;
        this.hash = hash;
    }


    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "[탭확인] position : " + position);
        switch (position){
            case 0 :
                HashTagSearchFragment tabFragment1 = HashTagSearchFragment.newInstance(hash);
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
