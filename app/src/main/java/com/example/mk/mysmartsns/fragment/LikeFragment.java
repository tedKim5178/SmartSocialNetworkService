package com.example.mk.mysmartsns.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.LikeAdapter;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mk on 2017-03-21.
 */

public class LikeFragment extends android.support.v4.app.Fragment{

    private static final String TAG = LikeFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    private LikeAdapter mAdapter;
    private int content_no;

    public static LikeFragment newInstance(int content_no) {
        LikeFragment fragment = new LikeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("content_no",content_no);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LikeFragment newInstance() {
        LikeFragment fragment = new LikeFragment();
        return fragment;
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            content_no = getArguments().getInt("content_no");
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_like_activity, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.like_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        InteractionManager.getInstance(getContext()).requestPeopleWhoLikeThisPost(content_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                List<UserInfo> userInfoList = (List<UserInfo>)response;
                mAdapter = new LikeAdapter(getContext(), userInfoList);     //
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void fail() {
            }
        });
        return view;
    }
}
