package com.example.mk.mysmartsns.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.TimelineAdapter;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class
OtherTimelineActivity extends AppCompatActivity {

    private static final String TAG = MyTimelineFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TimelineAdapter mAdapter;
    private ImageView imgOtherTimeline;
    private TextView textOtherTimeLineID;
    private TextView textOtherTimeLineName;
    int user_no, host_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_timeline);
        user_no = getIntent().getIntExtra("user_no", -1);
        host_no = Integer.parseInt(getIntent().getStringExtra("host_no"));
        initLayout();
    }

    private void initLayout(){
        imgOtherTimeline = (ImageView)findViewById(R.id.imgOtherTimeline);
        if(MyConfig.myInfo.getUser_profile_url() == null)
            Glide.with(this).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(this)).into(imgOtherTimeline);
        else
            Glide.with(this).load(APIConfig.baseUrl + "/" + MyConfig.myInfo.getUser_profile_url()).bitmapTransform(new CropCircleTransformation(this)).into(imgOtherTimeline);
        textOtherTimeLineID = (TextView)findViewById(R.id.texOtherTimeLineID);
        textOtherTimeLineName = (TextView)findViewById(R.id.textOtherTimeLineName);
        textOtherTimeLineID.setText("");
        textOtherTimeLineName.setText("");

        mRecyclerView = (RecyclerView)findViewById(R.id.otherTimeline_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        InteractionManager.getInstance(this).requestUserThumbnailContents(user_no, host_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                List<ContentInfo> contentInfoList = (List<ContentInfo>) response;
                mAdapter = new TimelineAdapter(OtherTimelineActivity.this, contentInfoList, getSupportFragmentManager());
                mRecyclerView.setAdapter(mAdapter);
                // ToDo. 임시로 이렇게 해서 user_id, user_name 받아왔으나, 사용자가 올린 콘텐츠가 없을 경우 예외처리 해줘야됨, 수정하자
                if(contentInfoList.size() != 0) {
                    textOtherTimeLineID.setText(contentInfoList.get(0).getContent_host_id());
                    textOtherTimeLineName.setText(contentInfoList.get(0).getContent_host());
                }
            }

            @Override
            public void fail() {

            }
        });

    }
}
