package com.example.mk.mysmartsns.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.CommentAdapter;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.fragment.fragment_main.TimelineFragment;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.CommentInfo;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by mk on 2017-03-20.
 */

public class CommentFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.recycler_view_in_comment)
    RecyclerView recyclerView_in_comment;
    @Bind(R.id.comment_edittext_in_comment_activity)
    EditText comment_edittext_in_comment_activity;
    @Bind(R.id.add_comment_button_in_comment_activity)
    Button add_comment_button_in_comment_activity;
    @Bind(R.id.content_image_in_comment_fragment)
    ImageView content_image_in_comment_fragment;

    CommentAdapter commentAdapter;
    List<CommentInfo> commentInfoList;
    ContentInfo contentInfo;


    public static CommentFragment newInstance(ContentInfo contentInfo) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("content_info", contentInfo);
        fragment.setArguments(bundle);
        // 이런식으로하면 값 전달이 되려나..?
        return fragment;
    }

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "fragmentTest 2");
        if(getArguments() != null){
            this.contentInfo = (ContentInfo)getArguments().getSerializable("content_info");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.comment_fragment, container, false);
        ButterKnife.bind(this, view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_in_comment.setLayoutManager(linearLayoutManager);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Integer.parseInt(contentInfo.getContent_width()), Integer.parseInt(contentInfo.getContent_height()));
        content_image_in_comment_fragment.setLayoutParams(params);
        content_image_in_comment_fragment.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // 이미지 넣기!
        Glide.with(this).load(APIConfig.baseUrl + "/" + contentInfo.getContent_url()).into(content_image_in_comment_fragment);

        // 댓글 목록 recyclerview 정보 주고 연결해줘야함
        InteractionManager.getInstance(getContext()).requestDownloadComment(contentInfo.getContent_no(), new OnMyApiListener() {
            @Override
            public void success(Object response) {
                commentInfoList = (List<CommentInfo>) response;
                // commentInfoList 써서 화면에 보여줘야함..! 즉 adapter 초기화 여기서..!
                commentAdapter = new CommentAdapter(getContext(),commentInfoList);
                recyclerView_in_comment.setAdapter(commentAdapter);
            }

            @Override
            public void fail() {

            }
        });
        return view;
    }

    @OnClick(R.id.add_comment_button_in_comment_activity)
    public void onClick(View view){
        int id = view.getId();
        switch (id) {
            case R.id.add_comment_button_in_comment_activity: {
                // add 누르면 해당 post의 댓글 갯수를 올려줘야함..!

                InteractionManager.getInstance(getContext()).requestCountCommentIncrease(MyConfig.myInfo.getUser_no(), contentInfo, new OnMyApiListener() {
                    @Override
                    public void success(Object response) {

                    }

                    @Override
                    public void fail() {

                    }
                });

                // 서버로 보내주는거 실행하자
                // uc_comment_name 은 지금 댓글을 남긴 내용이다..!
                String uc_comment_name = comment_edittext_in_comment_activity.getText().toString();
                InteractionManager.getInstance(getContext()).requestAddComment(MyConfig.myInfo.getUser_no(), contentInfo.getContent_host_no(), contentInfo.getContent_no(), uc_comment_name, new OnMyApiListener() {
                    @Override
                    public void success(Object response) {
                        // 이제 그 데이터 이용
                        commentInfoList = (List<CommentInfo>) response;
                        Log.d(TAG, "requestComment" + commentInfoList.size());
                        for (int i = 0; i < commentInfoList.size(); i++) {
                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getComment_name());
                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_no());
                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_profile());
                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_id());
                        }

                        commentAdapter.setCommentData(commentInfoList);
                        commentAdapter.notifyDataSetChanged();

                        comment_edittext_in_comment_activity.setText("");
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
            }
        }
    }

    @Override
    public void onDestroy() {

        // Destroy 되기 전에 position을 넘겨주자
        Log.d(TAG, "fragmentTest!! Comment fragment destroy!!");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
