package com.example.mk.mysmartsns.fragment.fragment_main;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.activity.LoginActivity;
import com.example.mk.mysmartsns.adapter.TabPagerAdapter;
import com.example.mk.mysmartsns.network.info.BigHashInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mk on 2017-02-03.
 */

public class SearchFragment extends android.support.v4.app.Fragment{
    FragmentManager fm;
    android.app.FragmentTransaction fragmentTransaction;
    Fragment test;
    ActionBar actionBar;
    private int judge;
    private int smallhash_no;
    private int bighash_no;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String hash;
    private ArrayList<BigHashInfo> bigHashList;

    private static final String TAG = LoginActivity.class.getSimpleName();



    public static SearchFragment newInstance(String hash, List<BigHashInfo> hashlist, int smallhash_no, int judge) {

        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hash", hash);
        bundle.putInt("smallhash_no", smallhash_no);
        bundle.putSerializable("hashlist", (ArrayList)hashlist);
        bundle.putInt("judge", judge);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchFragment newInstance(String hash, int bighash_no, int judge) {

        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("bighash_no", bighash_no);
        bundle.putString("hash", hash);
        bundle.putInt("judge", judge);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.judge = getArguments().getInt("judge");
            hash = getArguments().getString("hash");
            if(judge == 1){
                bigHashList = (ArrayList) getArguments().getSerializable("hashlist");
                this.smallhash_no = getArguments().getInt("smallhash_no");
            }else{
                Log.d(TAG, "빅해쉬카운트테스트 in SearchFragment : " + bighash_no);
                this.bighash_no = getArguments().getInt("bighash_no");
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_search, container, false);
//         검색 fragment
        // initializing the TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("해쉬태그"));
        tabLayout.addTab(tabLayout.newTab().setText("친구"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), hash, bigHashList, bighash_no,  smallhash_no, judge);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // set TabSelectedListenter
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }
}
