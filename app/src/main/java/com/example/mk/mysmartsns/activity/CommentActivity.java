package com.example.mk.mysmartsns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.CommentAdapter;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.CommentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mk on 2017-02-23.
 */

public class CommentActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.recycler_view_in_comment)
    RecyclerView recyclerView_in_comment;
    @Bind(R.id.comment_edittext_in_comment_activity)
    EditText comment_edittext_in_comment_activity;
    @Bind(R.id.add_comment_button_in_comment_activity)
    Button add_comment_button_in_comment_activity;

    CommentAdapter commentAdapter;
    List<CommentInfo> commentInfoList;
    int content_no;
    int user_no;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_fragment);
        // Butterknife bind
        ButterKnife.bind(this);

        // 리사이클러뷰 넣어주자..!
        // 그리고 content no 받아오자
        Intent intent = getIntent();
        content_no = intent.getIntExtra("content_no", 0);
        user_no = intent.getIntExtra("user_no", 0);
        position = intent.getIntExtra("position", 0);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_in_comment.setLayoutManager(linearLayoutManager);

        // content_no를 넘겨서 서버로 부터 정보 받아오자..!
        // 레트로핏 써야지..!
        InteractionManager.getInstance(this).requestDownloadComment(content_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                commentInfoList = (List<CommentInfo>) response;
                // commentInfoList 써서 화면에 보여줘야함..! 즉 adapter 초기화 여기서..!
                commentAdapter = new CommentAdapter(getApplicationContext(),commentInfoList);
                recyclerView_in_comment.setAdapter(commentAdapter);
                Log.d(TAG, "requestComment보기" + commentInfoList.size());
                for(int i=0; i<commentInfoList.size(); i++){
                    Log.d(TAG, "requestComment보기" + commentInfoList.get(i).getComment_name());
                    Log.d(TAG, "requestComment보기" + commentInfoList.get(i).getUser_no());
                    Log.d(TAG, "requestComment보기" + commentInfoList.get(i).getUser_profile());
                    Log.d(TAG, "requestComment보기" + commentInfoList.get(i).getUser_id());
                }
            }

            @Override
            public void fail() {

            }
        });
    }

    @OnClick(R.id.add_comment_button_in_comment_activity)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_comment_button_in_comment_activity: {
                // add 누르면 해당 post의 댓글 갯수를 올려줘야함..!

                // 서버로 보내주는거
                // uc_comment_name 은 지금 댓글을 남긴 내용이다..!실행하자
                String uc_comment_name = comment_edittext_in_comment_activity.getText().toString();
//                InteractionManager.getInstance(this).requestAddComment(user_no, content_no, uc_comment_name, new OnMyApiListener() {
//                    @Override
//                    public void success(Object response) {
//                        // 이제 그 데이터 이용
//                        commentInfoList = (List<CommentInfo>) response;
//                        Log.d(TAG, "requestComment" + commentInfoList.size());
//                        for (int i = 0; i < commentInfoList.size(); i++) {
//                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getComment_name());
//                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_no());
//                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_profile());
//                            Log.d(TAG, "requestComment" + commentInfoList.get(i).getUser_id());
//                        }
//
//                        commentAdapter.setCommentData(commentInfoList);
//                        commentAdapter.notifyDataSetChanged();
//
//                        comment_edittext_in_comment_activity.setText("");
//                    }
//
//                    @Override
//                    public void fail() {
//
//                    }
//                });
                break;
            }
        }
    }

//    @Override
//    protected void onStop() {
//        Intent intent = new Intent();
//        intent.putExtra("position", position);
//        intent.putExtra("the_number_of_comments", commentInfoList.size());
//        setResult(RESULT_OK, intent);
//        Log.d(TAG, "테스트 onStop : " + position + ", " + commentInfoList.size());
//        super.onStop();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("the_number_of_comments", commentInfoList.size());
        setResult(RESULT_OK, intent);
        Log.d(TAG, "테스트 onStop : " + position + ", " + commentInfoList.size());
        super.onBackPressed();
    }
}
