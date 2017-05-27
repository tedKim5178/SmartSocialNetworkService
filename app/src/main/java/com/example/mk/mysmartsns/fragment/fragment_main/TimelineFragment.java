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
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.CallManagement;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.info.CountInfo;
import com.example.mk.mysmartsns.network.info.PrefetchImageInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by mk on 2017-02-02.
 */

public class TimelineFragment extends android.support.v4.app.Fragment {
    private static final String TAG = TimelineFragment.class.getSimpleName();
    private TimelineAdapter mAdapter;
    RecyclerView mRecyclerView;
    private final int INITIAL_CURRENT_PAGE = 1;


    List<ContentInfo> contentInfoList;

    static TimelineFragment timelineFragment;

    int backPosition= 0;
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        timelineFragment = fragment;
        return timelineFragment;
    }

    public static TimelineFragment getInstance(){
        return timelineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_timeline, container, false);
        Log.d(TAG, "fragmentTest!! onCreateView in TimelineFragment");

        // 지금은 어댑터에서 뷰를 연결해줄때 임의의 이미지를 연결해주지만 원래는 데이터베이스에서 가져오는 이미지를 가져와야된다.
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "여기1");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        Log.d(TAG, "여기2");

        mRecyclerView.setLayoutManager(linearLayoutManager);

        Log.d(TAG, "여기3");
        if(TimelineAdapter.getTimelineAdapterInstance() != null){
            mAdapter = TimelineAdapter.getTimelineAdapterInstance();
            Log.d(TAG, "여기4");
        }
        Log.d(TAG, "여기5");
        // mAdapter 가 null이라는 뜻은 이 fragment가 처음으로 attach됬단 뜻이다.
        // mAdapter null이 아니라면 else 부분에서 원래의 포지션 값으로 보내주는 역할을 수행하자 원래 데이터를 이용해서 뷰를 구성해야한다
        // 서버로부터 총 아이템 갯수 가져오자.!

        if(mAdapter == null){
            Log.d(TAG, "fragmentTest!! mAdapter is null");
            InteractionManager.getInstance(getContext()).requestTotalCount(new OnMyApiListener() {
                @Override
                public void success(Object response) {
                    // count.get(0).getCount(); 이런식으로 전체 count를 가져올 수 있다.
                    List<CountInfo> count = (List<CountInfo>) response;
                    PrefetchConfig.totalContentsCount = count.get(0).getCount();

                    // 서버로부터 thumbnail image를 포함한 contents 받기
                    getThumbnailContentsFromServer(INITIAL_CURRENT_PAGE);

                    // 서버로부터 프리패칭 리스트 받기, 받기가 완료되면 네트워크 사용 여부에 따라 프리패칭 시작됨
                    if(PrefetchConfig.isPrefetching){
                        Log.d(TAG, "PrefetchingImage ??");
                        getPrefetchingImageFromServer(INITIAL_CURRENT_PAGE);
                        getPrefetchingImageFromServer(INITIAL_CURRENT_PAGE+1);
                        PrefetchConfig.isPrefetching = false;
                    }
                }

                @Override
                public void fail() {

                }
            });
        }else{
            Log.d(TAG, "fragmentTest!! mAdapter is not null and size @@@ " + mAdapter.contentInfoList.size() + "?? :"+backPosition);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.scrollToPosition(backPosition);
            Toast.makeText(getContext(),"바보",Toast.LENGTH_LONG).show();
        }

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(TAG, "onLoadMore:::::gilsoo");
                getThumbnailContentsFromServer(current_page);
                getPrefetchingImageFromServer(current_page+1);

            }
        });
    }

    public void setDataChanged(int position, int the_number_of_comment){
        mAdapter.contentInfoList.get(position).setContent_comment_count(the_number_of_comment);
        mAdapter.notifyItemChanged(position);
    }


    public void getPrefetchingImageFromServer(final int current_page){
        // current_page를 이용해서 prefetching을 진행하자. 즉 server로 current_page를 넘겨줘야한다.
        CallManagement.getInstance(getContext()).addCall("requestPrefetchingList", true);
        InteractionManager.getInstance(getContext()).requestPrefetchingList(MyConfig.myInfo.getUser_no(), current_page, PrefetchConfig.totalContentsCount, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                Log.d("gilsoo", "getPrefetchingImageFromServer::: success");
                // prefetch image 들의 정보를 가지고 있다...
                // 넣어주기 전에 queue의 상태 확인
                int remainImagesInQueue = PrefetchConfig.prefetching_queue.size();

                List<PrefetchImageInfo> prefetching_image = (List<PrefetchImageInfo>) response;
                for (int i = 0; i < prefetching_image.size(); i++) {
                    String str = "thumbnail_contents/";
                    int str_length = str.length();
                    String prefetchImageUrl = prefetching_image.get(i).getContent_url().substring(str_length);
                    Log.d(TAG, "CallManagerMent:::Prefetching offer prefetchImageUrl : " + prefetchImageUrl);
                    synchronized (PrefetchConfig.prefetching_queue) {
                        PrefetchConfig.prefetching_queue.offer(prefetchImageUrl);
                    }
                }

                // queue 순서 바꾸기
                if(current_page > 2){
                    for(int i=0; i<remainImagesInQueue; i++){
                        synchronized (PrefetchConfig.prefetching_queue) {
                            String temp = PrefetchConfig.prefetching_queue.poll();
                            PrefetchConfig.prefetching_queue.offer(temp);
                        }
                    }
                }

                // 프리페칭 시작
                CallManagement.getInstance(getContext()).subtractCall("requestPrefetchingList", false);
            }

            @Override
            public void fail() {
                Log.d("gilsoo", "getPrefetchingImageFromServer::: fail");

                CallManagement.getInstance(getContext()).subtractCall("requestPrefetchingList", false);
            }
        });
    }

    public void getThumbnailContentsFromServer(int current_page){
        // Download Thmbnail Contents
        InteractionManager.getInstance(getContext()).requestContentThumbnailDownload(MyConfig.myInfo.getUser_no(), current_page, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                contentInfoList = (List<ContentInfo>) response;
                if (getActivity() != null) {
                    if (mAdapter == null) {
                        Log.d(TAG, "contentInfoList size : " + contentInfoList.size());
                        mAdapter = new TimelineAdapter(getContext(), contentInfoList, getActivity().getSupportFragmentManager());
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.setTimelineAdapterInstance(mAdapter);
                    } else {
                        Log.d(TAG, "contentInfoList size : " + contentInfoList.size());
                        mAdapter.addContentInfo(contentInfoList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void fail() {
                Log.d("gilsoo", "getThumbnailContentsFromServer::: fail");
            }
        });
    }

    // queue에서 image 지워주기
    public void deleteImagesFromQueue(){

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "fragmentTest!! onDestroyView in TimelineFragment");
        super.onDestroyView();
    }

    public void setBackPosition(int position){
        Log.d(TAG, "fragmentTest 여기왜작동안해 " + position);
        backPosition =  position;
        Log.d(TAG, "fragmentTest 여기왜작동안해 " + + backPosition + " , " + position);
        mRecyclerView.scrollToPosition(backPosition);
    }

}