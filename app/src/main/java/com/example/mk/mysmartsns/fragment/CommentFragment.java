package com.example.mk.mysmartsns.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.CommentInfo;
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
    int content_no;
    int user_no;
    int position;
    String content_url;
    String width;
    String height;

    public static CommentFragment newInstance(int content_no, int user_no, int position, String content_url, String width, String height) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("content_no", content_no);
        bundle.putInt("user_no", user_no);
        bundle.putInt("position", position);
        bundle.putString("content_url", content_url);
        bundle.putString("width", width);
        bundle.putString("height", height);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            content_no = getArguments().getInt("content_no");
            user_no = getArguments().getInt("user_no");
            position = getArguments().getInt("position");
            content_url = getArguments().getString("content_url");
            width = getArguments().getString("width");
            height = getArguments().getString("height");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.comment_fragment, container, false);
        ButterKnife.bind(this, view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_in_comment.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "레이아웃크기테스트 width, height in comment fragment: " +width + " , " + height);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Integer.parseInt(width), Integer.parseInt(height));
        content_image_in_comment_fragment.setLayoutParams(params);

        Glide.with(this).load(content_url).into(content_image_in_comment_fragment);

        InteractionManager.getInstance(getContext()).requestDownloadComment(content_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                commentInfoList = (List<CommentInfo>) response;
                // commentInfoList 써서 화면에 보여줘야함..! 즉 adapter 초기화 여기서..!
                commentAdapter = new CommentAdapter(getContext(),commentInfoList);
                recyclerView_in_comment.setAdapter(commentAdapter);
                Log.d(TAG, "requestComment보기" + commentInfoList.size());
                for(int i=0; i<commentInfoList.size(); i++){
                }
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

                // 서버로 보내주는거 실행하자
                // uc_comment_name 은 지금 댓글을 남긴 내용이다..!
                String uc_comment_name = comment_edittext_in_comment_activity.getText().toString();
                InteractionManager.getInstance(getContext()).requestAddComment(user_no, content_no, uc_comment_name, new OnMyApiListener() {
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
        Log.d(TAG, "이거나오는지테스트");
        super.onDestroy();
    }

}
