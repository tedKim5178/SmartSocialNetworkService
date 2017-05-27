package com.example.mk.mysmartsns.fragment.fragment_main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.activity.MainActivity;
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.model.CallManagement;

import java.util.Iterator;

/**
 * Created by mk on 2017-02-03.
 */

public class SettingFragment extends android.support.v4.app.Fragment implements CompoundButton.OnCheckedChangeListener{
    private final String TAG = SettingFragment.class.getSimpleName();
    private Switch switchSettingPrefetchMode, switchSettingProgress, switchSettingDataMode, switchSettingNetworkMode;
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_setting, container, false);
        switchSettingPrefetchMode = (Switch)view.findViewById(R.id.switchSettingPrefetchMode);
        switchSettingPrefetchMode.setOnCheckedChangeListener(this);
        switchSettingProgress = (Switch)view.findViewById(R.id.switchSettingProgress);
        switchSettingProgress.setOnCheckedChangeListener(this);
        switchSettingDataMode = (Switch)view.findViewById(R.id.switchSettingDataMode);
        switchSettingDataMode.setOnCheckedChangeListener(this);
        switchSettingNetworkMode = (Switch)view.findViewById(R.id.switchSettingNetworkMode);
        switchSettingNetworkMode.setOnCheckedChangeListener(this);
        settingModeInfo();

        return view;
    }

    /**
     * 설정 화면 setting - preference로 by gilsoo.
     */
    private void settingModeInfo(){
        // prfetch mode setting
        switchSettingPrefetchMode.setChecked((getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE).getBoolean(PrefetchConfig.PREFETCH_MODE, true)));
        // progress bar setting
        switchSettingProgress.setChecked((getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE).getBoolean(PrefetchConfig.PREFETCH_SHOW, false)));
        // data mode setting
        switchSettingDataMode.setChecked((getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE).getBoolean(PrefetchConfig.DATA_MODE, false)));
        // network mode setting
        switchSettingNetworkMode.setChecked((getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE).getBoolean(PrefetchConfig.NETWORK_MODE, false)));
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences pref;
        SharedPreferences.Editor edit;
        switch(buttonView.getId()){
            case R.id.switchSettingPrefetchMode:            // prfetching mode
                pref = getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE);
                edit = pref.edit();
                edit.putBoolean(PrefetchConfig.PREFETCH_MODE, isChecked);
                edit.apply();
                break;
            case R.id.switchSettingProgress:                // progress bar
                pref = getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE);
                edit= pref.edit();
                edit.putBoolean(PrefetchConfig.PREFETCH_SHOW, isChecked);
                edit.apply();
                PrefetchConfig.isPrefetchingShow = isChecked;
                MainActivity.onShowProgressbar(PrefetchConfig.isPrefetchingShow);
                break;
            case R.id.switchSettingDataMode:                // data mode
                pref = getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE);
                edit= pref.edit();
                edit.putBoolean(PrefetchConfig.DATA_MODE, isChecked);
                edit.apply();
                synchronized (PrefetchConfig.prefetching_queue) {
                    Iterator<String> iter = PrefetchConfig.prefetching_queue.iterator();
                    Log.d(TAG, "PrefetchingList : "+ PrefetchConfig.prefetching_queue.size());
                    while (iter.hasNext()) {
                        Log.d(TAG, iter.next());
                    }
                }
                break;
            case R.id.switchSettingNetworkMode:             // network mode
                pref = getContext().getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE);
                edit= pref.edit();
                edit.putBoolean(PrefetchConfig.NETWORK_MODE, isChecked);
                edit.apply();
                CallManagement.getInstance(getContext()).printCall();
                break;

        }
    }
}
