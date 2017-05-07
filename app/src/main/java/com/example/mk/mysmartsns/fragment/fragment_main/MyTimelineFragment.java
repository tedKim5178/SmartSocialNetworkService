package com.example.mk.mysmartsns.fragment.fragment_main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by mk on 2017-02-02.
 */

public class MyTimelineFragment extends android.support.v4.app.Fragment {
    private static final String TAG = MyTimelineFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TimelineAdapter mAdapter;
    private ImageView imgMyTimeline;
    private TextView textMyTimeLineID;
    private TextView textMyTimeLineName;

    public static MyTimelineFragment newInstance() {
        MyTimelineFragment fragment = new MyTimelineFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_my_timeline, container, false);
        Log.d(TAG, "프레그먼트2속온크리에이트");

        imgMyTimeline = (ImageView)view.findViewById(R.id.imgMyTimeline);

        Toast.makeText(getContext(), MyConfig.myInfo.getUser_profile_url(),Toast.LENGTH_SHORT).show();
        if(MyConfig.myInfo.getUser_profile_url() == null)
            Glide.with(getContext()).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(getContext())).into(imgMyTimeline);
        else
            Glide.with(getContext()).load(APIConfig.baseUrl + "/" + MyConfig.myInfo.getUser_profile_url()).bitmapTransform(new CropCircleTransformation(getContext())).into(imgMyTimeline);
        textMyTimeLineID = (TextView)view.findViewById(R.id.textMyTimeLineID);
        textMyTimeLineName = (TextView)view.findViewById(R.id.textMyTimeLineName);
        textMyTimeLineID.setText(MyConfig.myInfo.getUser_id());
        textMyTimeLineName.setText(MyConfig.myInfo.getUser_name());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mytimeline_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // user_no 부분 수정 필요
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setDataChanged(int position, int the_number_of_comment){
        Toast.makeText(getContext(),"tqtqtq", Toast.LENGTH_SHORT).show();
        mAdapter.contentInfoList.get(position).setContent_comment_count(the_number_of_comment);
        mAdapter.notifyItemChanged(position);
    }
}
