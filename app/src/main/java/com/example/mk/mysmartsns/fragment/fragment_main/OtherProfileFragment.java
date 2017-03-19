package com.example.mk.mysmartsns.fragment.fragment_main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.TimelineAdapter;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mk on 2017-03-19.
 */

public class OtherProfileFragment extends android.support.v4.app.Fragment{
    private static final String TAG = TimelineFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TimelineAdapter mAdapter;
    private ImageView imgMyTimeline;
    private TextView textMyTimeLineID;
    private TextView textMyTimeLineName;
    private int user_no;
    private String profile_url;
    private String user_id;
    private String user_name;

    public static OtherProfileFragment newInstance(int user_no, String profile_url, String user_name, String user_id) {
        OtherProfileFragment fragment = new OtherProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("user_no", user_no);
        bundle.putString("profile_url", profile_url);
        bundle.putString("user_name", user_name);
        bundle.putString("user_id", user_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_timeline, container, false);

        if(getArguments() != null){
            user_no = getArguments().getInt("user_no");
            profile_url = getArguments().getString("profile_url");
            user_name = getArguments().getString("user_name");
            user_id = getArguments().getString("user_id");
        }


        imgMyTimeline = (ImageView)view.findViewById(R.id.imgMyTimeline);
        if(MyConfig.myInfo.getUser_profile_url() == null)
            Glide.with(getContext()).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(getContext())).into(imgMyTimeline);
        else {
//            Glide.with(getContext()).load(APIConfig.baseUrl + "/" + profile_url).bitmapTransform(new CropCircleTransformation(getContext())).into(imgMyTimeline);
            Log.d(TAG, "otherfragmenttest profile_url : " + profile_url + " , APIConfig : "  + APIConfig.baseUrl + "/" + profile_url);
            Glide.with(getContext()).load(profile_url).bitmapTransform(new CropCircleTransformation(getContext())).into(imgMyTimeline);
        }
        textMyTimeLineID = (TextView)view.findViewById(R.id.textMyTimeLineID);
        textMyTimeLineName = (TextView)view.findViewById(R.id.textMyTimeLineName);
        textMyTimeLineID.setText(user_id);
        textMyTimeLineName.setText(user_name);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mytimeline_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        InteractionManager.getInstance(getContext()).requestUserThumbnailContents(MyConfig.myInfo.getUser_no(), MyConfig.myInfo.getUser_no(), new OnMyApiListener() {
            @Override
            public void success(Object response) {
                List<ContentInfo> contentInfoList = (List<ContentInfo>) response;
                if (getActivity() != null) {
                    mAdapter = new TimelineAdapter(getContext(), contentInfoList, getActivity().getSupportFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void fail() {

            }
        });
        return view;
    }
}
