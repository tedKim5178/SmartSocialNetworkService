package com.example.mk.mysmartsns.fragment.fragment_main;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
                if(getActivity() != null){
                    mAdapter = new TimelineAdapter(getContext(), contentInfoList, getActivity().getSupportFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void fail() {

            }
        });




        // 레트로핏을 이용해서 데이터를 받아오자... 그리고 그 안에서 어댑터를 설정해줘야 되나..?

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                mAdapter.notifyDataSetChanged();
            }
        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                visibleItemCount = mRecyclerView.getChildCount();
//                totalItemCount = linearLayoutManager.getItemCount();
//                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                    }
//                }
//                if (!loading && (totalItemCount - visibleItemCount)
//                        <= (firstVisibleItem + visibleThreshold)) {
//                    // End has been reached
//
//                    Log.i("Yaeye!", "end called");
//
//                    // Do something
//
//                    loading = true;
//                }
//            }
//        });

        // 서버로 user_no(number)를 보내준다. get방식으로 url 만들어서 보내주는거지..! @Path 이용
        // 그러면 길수가 json 형태로 많은 정보들을 넘겨 줄것인데 내가 사용할 정보는
        // content_no, content_url, content_desc, content_like_count, content_comment_count, bighash_name
        // 위의 정보들을 사용해서 imageView에 뿌려줘야 할것이다. 여기서 ContentInfo정보를 전달해줘도 될거같다.



        // 만약에 사용자가 이미지를 클릭하면 나는 content_no를 넘겨주고 서버는 오리지널 이미지를 보내준다..! 로직!

        // 만약에 사용자가 해쉬태그를 누른다면 빅해쉬인지 스몰해쉬인지 선택해가지고
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