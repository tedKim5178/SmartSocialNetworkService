package com.example.mk.mysmartsns;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mk.mysmartsns.adapter.LikeAdapter;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mk on 2017-02-15.
 */

public class PeopleWhoLikeThisPostActivity extends AppCompatActivity{
    private static final String TAG = PeopleWhoLikeThisPostActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    private LikeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_activity);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.like_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        int content_no = intent.getIntExtra("contentNo", 0);

        // 테스트
        // 이제 content_no를 이용해서 누가 좋아요를 눌렀는지 가져오자. 여기는 RecyclerView가 필요할것이다.
        InteractionManager.getInstance(this).requestPeopleWhoLikeThisPost(content_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                // 성공하면 이제 콜백이니까 이 함수를 호출하게 될텐데...!
                // response에서 데이터 가져와서 리스트뷰에 뿌려주면 되겠지...!
                // 결국에는 유저 정보들 즉 개인 프로필 url, name, user_no 가 필요하니까 userInfo로 받으면 될거같다
                Log.d(TAG, "좋아요누른사람들보는테스트");
                List<UserInfo> userInfoList = (List<UserInfo>)response;
                Log.d(TAG, "좋아요누른사람들보는테스트 userInfoList : " + userInfoList.size());

                mAdapter = new LikeAdapter(PeopleWhoLikeThisPostActivity.this, userInfoList);     //
                mRecyclerView.setAdapter(mAdapter);
                // 리스트뷰 어댑터에 userInfoList 넘겨주기..!
            }
            @Override
            public void fail() {

            }
        });
    }
}
