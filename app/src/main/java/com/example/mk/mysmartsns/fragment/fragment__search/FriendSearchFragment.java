package com.example.mk.mysmartsns.fragment.fragment__search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.AllFriendAdapter;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mk on 2017-02-14.
 */

public class FriendSearchFragment extends android.support.v4.app.Fragment{
    private static final String TAG = FriendSearchFragment.class.getSimpleName();

    @Bind(R.id.search_friend_editText_in_search_fragment)
    EditText search_friend_editText_in_search_fragment;
    @Bind(R.id.search_button_in_friend_search)
    Button search_friend_imageButton_in_search_fragment;

    RecyclerView mRecyclerView;
    private AllFriendAdapter mAdapter;
//    private LikeAdapter mLikeAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friend_search, container, false);
        ButterKnife.bind(this, view);
        search_friend_editText_in_search_fragment = (EditText)  view.findViewById(R.id.search_friend_editText_in_search_fragment);
        // 유저목록 보여주는 리사이클러뷰 필요..! 우선 서버로 데이터를 요청해야겠지..!
        // 우선은 전체 유저 목록 보여주는거니까 get으로 하자
//        search_friend_imageButton_in_search_fragment = (Button) view.findViewById(R.id.search_friend_imageButton_in_search_fragment);
//        search_friend_imageButton_in_search_fragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "친구검색테스트");
//                // 이 버튼 눌리면 edit text 에서 가져와
//                String search_name = search_friend_editText_in_search_fragment.getText().toString();
//                InteractionManager.getInstance(getContext()).requestSearchUser(MyConfig.myInfo.getUser_no(), search_name, new OnMyApiListener() {
//                    @Override
//                    public void success(Object response) {
//                        // 받아온 콘텐츠 정보를 리사이클러뷰에 띄워주자..!
//                        Log.d(TAG, "친구검색테스트 success" );
//                        List<UserInfo> userInfoList = (List<UserInfo>)response;
//                        Log.d(TAG, "친구검색테스트 success" + userInfoList.size());
//                        mLikeAdapter = new LikeAdapter(getContext(), userInfoList, getActivity().getSupportFragmentManager());
//                        mRecyclerView.setAdapter(mLikeAdapter);
//                    }
//
//                    @Override
//                    public void fail() {
//
//                    }
//                });
//            }
//        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.firend_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        Log.d(TAG, "친구검색테스트 userInfoList size : ");
//
//        InteractionManager.getInstance(getContext()).requestFriendsList(new OnMyApiListener() {
//            @Override
//            public void success(Object response) {
//                // success 했으면 response를 가지고 리사이클러뷰 초기화
//                List<UserInfo> userInfoList = (List<UserInfo>)response;
//                Log.d(TAG, "친구검색테스트 userInfoList size : " + userInfoList.size());
//                mAdapter = new AllFriendAdapter(getContext(), userInfoList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void fail() {
//                Log.d(TAG, "친구검색테스트 fail");
//            }
//        });

        return view;
    }

    @OnClick(R.id.search_button_in_friend_search)
    public void onClick(View view){
        Toast.makeText(getContext(), "asdf", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "친구검색테스트11");
        switch (view.getId()){
            case  R.id.search_button_in_friend_search:
                Log.d(TAG, "친구검색테스트");
                // 이 버튼 눌리면 edit text 에서 가져와
                String search_name = search_friend_editText_in_search_fragment.getText().toString();
                InteractionManager.getInstance(getContext()).requestSearchUser(MyConfig.myInfo.getUser_no(), search_name, new OnMyApiListener() {
                    @Override
                    public void success(Object response) {
                        // 받아온 콘텐츠 정보를 리사이클러뷰에 띄워주자..!
                        List<UserInfo> userInfoList = (List<UserInfo>)response;
                        Log.d(TAG, "친구검색테스트 userInfoList size  : " +userInfoList.size());

                        mAdapter = new AllFriendAdapter(getContext(), userInfoList, getActivity().getSupportFragmentManager());
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void fail() {

                    }
                });

                break;
        }
    }


}
