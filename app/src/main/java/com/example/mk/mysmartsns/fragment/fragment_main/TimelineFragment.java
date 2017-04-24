package com.example.mk.mysmartsns.fragment.fragment_main;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.EndlessRecyclerOnScrollListener;
import com.example.mk.mysmartsns.adapter.TimelineAdapter;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;
import com.example.mk.mysmartsns.ztest.ListItems;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mk on 2017-02-02.
 */

public class TimelineFragment extends android.support.v4.app.Fragment {
    private static final String TAG = TimelineFragment.class.getSimpleName();
    private TimelineAdapter mAdapter;
    RecyclerView mRecyclerView;

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    //---------------
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    List<ListItems> listItemsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_timeline, container, false);

        // 지금은 어댑터에서 뷰를 연결해줄때 임의의 이미지를 연결해주지만 원래는 데이터베이스에서 가져오는 이미지를 가져와야된다.
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);


        // Download Thmbnail Contents
        InteractionManager.getInstance(getContext()).requestContentThumbnailDownload(MyConfig.myInfo.getUser_no(), new OnMyApiListener() {
            @Override
            public void success(Object response) {
                List<ContentInfo> contentInfoList = (List<ContentInfo>) response;

                for(int i=0; i<contentInfoList.size(); i++){
                    if(contentInfoList.get(i).getHash_list() != null){
                        for(int j=0; j<contentInfoList.get(i).getHash_list().size(); j++){
                            Log.d(TAG, "빅해쉬테스트 : " +contentInfoList.get(i).getHash_list().get(j).getBighash_no() + ", " + contentInfoList.get(i).getHash_list().get(j).getBighash_name());
                        }
                    }
                    Log.d(TAG, "빅해쉬테스트 : 절취선 =================================");
                }

                for(int i=0; i<contentInfoList.size(); i++){
                    if(contentInfoList.get(i).getSmallHash_list() != null){
                        for(int j=0; j<contentInfoList.get(i).getSmallHash_list().size(); j++){
                            Log.d(TAG, "스몰해시테스트 : " + contentInfoList.get(i).getSmallHash_list().get(j).getSmallhash_name() + " , " +contentInfoList.get(i).getSmallHash_list().get(j).getSmallhash_no());
                        }
                    }
                }
                if(getActivity() != null){
                    mAdapter = new TimelineAdapter(getContext(), contentInfoList, getActivity().getSupportFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void fail() {

            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                mAdapter.notifyDataSetChanged();
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