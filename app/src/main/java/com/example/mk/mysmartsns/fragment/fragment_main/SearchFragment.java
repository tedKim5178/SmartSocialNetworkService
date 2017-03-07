package com.example.mk.mysmartsns.fragment.fragment_main;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mk.mysmartsns.activity.LoginActivity;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.TabPagerAdapter;

/**
 * Created by mk on 2017-02-03.
 */

public class SearchFragment extends android.support.v4.app.Fragment{
    FragmentManager fm;
    android.app.FragmentTransaction fragmentTransaction;
    Fragment test;
    ActionBar actionBar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String hash;

    private static final String TAG = LoginActivity.class.getSimpleName();



    public static SearchFragment newInstance(String hash) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hash", hash);
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
            hash = getArguments().getString("hash");
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
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), hash);
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
