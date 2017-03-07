package com.example.mk.mysmartsns.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.activity.CommentActivity;
import com.example.mk.mysmartsns.activity.OriginalImageActivity;
import com.example.mk.mysmartsns.activity.OtherTimelineActivity;
import com.example.mk.mysmartsns.PeopleWhoLikeThisPostActivity;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.SearchFragment;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mk on 2017-02-02.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.PostViewHolder>{
    private static final String TAG = TimelineAdapter.class.getSimpleName();
    private Context mContext;
    Spannable span;

    private FragmentManager fragmentManager;
    EndlessScrollListener endlessScrollListener;
    public List<ContentInfo> contentInfoList;
    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener){
        this.endlessScrollListener = endlessScrollListener;
    }

    public TimelineAdapter(Context mContext, List<ContentInfo> contentInfoList, FragmentManager fragmentManager){
        this.mContext = mContext;
        this.contentInfoList = contentInfoList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public TimelineAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_list, parent, false);
        Log.d(TAG, "프레그먼트 온크리에이트뷰홀더1");
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.PostViewHolder holder, final int position) {
        Log.d(TAG, "리사이클러뷰 position " + position);
        if(position == getItemCount() -1){
            if(endlessScrollListener != null){
                endlessScrollListener.onLoadMore(position);
            }
        }
//        Log.d(TAG, "content_like_flag : " + contentInfoList.get(position).getContent_like_flag());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Integer.parseInt(contentInfoList.get(position).getContent_width()), Integer.parseInt(contentInfoList.get(position).getContent_height()));        // 크기 지정
        holder.image_view.setLayoutParams(params);
        Glide.with(mContext).load(APIConfig.baseUrl + "/" + contentInfoList.get(position).getContent_url()).into(holder.image_view);
        if(contentInfoList.get(position).getHost_url() == null)
            Glide.with(mContext).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.poster_profile);
        else
            Glide.with(mContext).load(APIConfig.baseUrl + "/" + contentInfoList.get(position).getHost_url()).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.poster_profile);
        holder.the_number_of_likes.setText(String.valueOf(contentInfoList.get(position).getContent_like_count()) + "명이 좋아합니다");
        Log.d(TAG, "댓글갯수Test : " + contentInfoList.get(position).getContent_comment_count());
        holder.the_number_of_comments.setText(String.valueOf(contentInfoList.get(position).getContent_comment_count()) + "개의 댓글 보기");
        holder.poster_name.setText(contentInfoList.get(position).getContent_host());

        // 여기서 디스크립션을 그냥 setText 해준다. 결국 이부분에서 로직을 돌려야 할거같음...!
        String description = contentInfoList.get(position).getContent_desc();
        span = Spannable.Factory.getInstance().newSpannable(description);
        descriptionToHashNavigation(description, holder.post_description_textview);
        holder.post_description_textview.setText(span);
        holder.post_description_textview.setMovementMethod(LinkMovementMethod.getInstance());
//        holder.post_description.setText(contentInfoList.get(position).getContent_desc());


        // add bighash
        holder.layoutPostBigHash.removeAllViews();                                     // 부모의 자식 뷰를 모두 지우고
        if(contentInfoList.get(position).getHash_list() != null) {
            for (int i = 0; i < contentInfoList.get(position).getHash_list().size(); i++) {          // ** 새로 할당해줘야 된다 (new)
                final TextView hashText = new TextView(holder.layoutPostBigHash.getContext());
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = 15;
                params.topMargin = 5;
                params.bottomMargin = 10;

                hashText.setLayoutParams(params);
                hashText.setText("#" + contentInfoList.get(position).getHash_list().get(i).getBighash_name());
                hashText.setTextAppearance(mContext, R.style.HashStyle);

                holder.layoutPostBigHash.addView(hashText);
                hashText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_layout, SearchFragment.newInstance(hashText.getText().toString().substring(1)), "nav_search_fragment");
                        transaction.addToBackStack("hash_serach_fragment");
                        transaction.commit();
                    }
                });
            }
        }

//        add Date
//        Log.d(TAG, contentInfoList.get(position).getContent_date().substring(0, contentInfoList.get(position).getContent_date().lastIndexOf("00:00:00")));
        holder.poster_date.setText(contentInfoList.get(position).getContent_date().substring(0,contentInfoList.get(position).getContent_date().lastIndexOf("00:00:00")));
//        add like or unlike icon
        Log.d(TAG, "position : likf_flag value - "+contentInfoList.get(position).getContent_like_flag());
        if(contentInfoList.get(position).getContent_like_flag() == 0)
            Glide.with(mContext).load(R.drawable.ic_unlike).fitCenter().into(holder.like_button);
        else
            Glide.with(mContext).load(R.drawable.ic_like).fitCenter().into(holder.like_button);



    }

    @Override
    public int getItemCount() {
        return contentInfoList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image_view;
        ImageView like_button;
        TextView the_number_of_likes;
        ImageButton comment_button;
        TextView the_number_of_comments;
        ImageView poster_profile;
        TextView poster_name;
        TextView poster_date;
        LinearLayout layoutPostBigHash;
        TextView post_description_textview;


        public PostViewHolder(View itemView){
            super(itemView);

            image_view = (ImageView) itemView.findViewById(R.id.image_view);
            poster_profile = (ImageView) itemView.findViewById(R.id.poster_profile_view);
            poster_name = (TextView) itemView.findViewById(R.id.poster_name_);
            like_button = (ImageView)itemView.findViewById(R.id.like_button);
            the_number_of_likes = (TextView) itemView.findViewById(R.id.the_number_of_likes);
            comment_button = (ImageButton) itemView.findViewById(R.id.comment_button);
            the_number_of_comments = (TextView) itemView.findViewById(R.id.the_number_of_comments);
            post_description_textview = (TextView) itemView.findViewById(R.id.post_description_textview);
            layoutPostBigHash = (LinearLayout)itemView.findViewById(R.id.layoutPostBigHash);
            poster_date = (TextView)itemView.findViewById(R.id.poster_date);

            itemView.setOnClickListener(this);
            image_view.setOnClickListener(this);
            like_button.setOnClickListener(this);
            the_number_of_likes.setOnClickListener(this);
            comment_button.setOnClickListener(this);
            the_number_of_comments.setOnClickListener(this);

            poster_profile.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //실제 포지션을 구할때 어댑터 포지션을 사용해야되는지, 레이아웃포지션을 사용해야 하는지는 테스트 할때 결정해야할거 같다.
            // 지금은 layout으로 사용해보자
            final int position = getAdapterPosition();
            Log.d(TAG, "포지션테스트 " + getAdapterPosition() + getLayoutPosition());
            // 아이템이클릭되면 여기로옴
            int viewId = view.getId();
            if(viewId == R.id.image_view){
                Intent intent = new Intent(mContext, OriginalImageActivity.class);
                intent.putExtra("thumbnail_url", contentInfoList.get(getAdapterPosition()).getContent_url());
                mContext.startActivity(intent);
            }else if(viewId == R.id.like_button){
                Log.d(TAG, "position : likf_flag value in clicklistener  - "+contentInfoList.get(position).getContent_like_flag());
                if(contentInfoList.get(position).getContent_like_flag() == 0){          // 좋아요 event
                    InteractionManager.getInstance(mContext).requestLikeClicked(MyConfig.myInfo.getUser_no(), contentInfoList.get(position).getContent_no(), new OnMyApiListener() {
                        @Override
                        public void success(Object response) {
                            ContentInfo contentInfo = (ContentInfo) response;
                            Log.d(TAG,"좋아요눌렀을때테스트 contentinfo like 갯수" + contentInfo.getContent_like_count());
                            contentInfoList.get(position).setContent_like_count(contentInfo.getContent_like_count());
                            contentInfoList.get(position).setContent_like_flag(contentInfo.getContent_like_flag());
                            notifyItemChanged(position);
                        }

                        @Override
                        public void fail() {

                        }
                    });
                }else{                                                                  // 좋아요 취소 event
                    InteractionManager.getInstance(mContext).requestUnLikeClicked(MyConfig.myInfo.getUser_no(), contentInfoList.get(position).getContent_no(), new OnMyApiListener() {
                        @Override
                        public void success(Object response) {
                            ContentInfo contentInfo = (ContentInfo) response;
                            contentInfoList.get(position).setContent_like_count(contentInfo.getContent_like_count());
                            contentInfoList.get(position).setContent_like_flag(contentInfo.getContent_like_flag());
                            notifyItemChanged(position);
                        }

                        @Override
                        public void fail() {

                        }
                    });
                }



            }else if(viewId == R.id.the_number_of_likes){
                // 좋아요 숫자 눌렀을때 이벤트
                // 누가 좋아요를 눌렀는지 새로운 엑티비티에 보여줄 예정
                // 그렇다면 엑티비티로 보내줄 데이터는 뭐가 있지? 이 해당 콘텐츠를 좋아요 누른 사람이니까 이 해당 콘텐츠의 no를 보내줘야한다.
                Intent peopleWhoLikeThisPostIntent = new Intent(mContext, PeopleWhoLikeThisPostActivity.class);
                peopleWhoLikeThisPostIntent.putExtra("contentNo", contentInfoList.get(position).getContent_no());
                ((Activity)mContext).startActivityForResult(peopleWhoLikeThisPostIntent, 888);

            }else if(viewId == R.id.comment_button){
                Toast.makeText(mContext, "댓글 클릭", Toast.LENGTH_SHORT).show();
                // 새로운 엑티비티 띄워주자..!
                Intent intent = new Intent(mContext, CommentActivity.class);
                // intent에 content_no 보내줘야함..!
                // content_no 만 넘겨줄게 아니고 지금 내 유저가 누구인지 알아야 되기때문에 user_no도 보내줘야함...!
                intent.putExtra("position", getLayoutPosition());
                intent.putExtra("content_no", contentInfoList.get(position).getContent_no());
                intent.putExtra("user_no", MyConfig.myInfo.getUser_no());
                Log.d(TAG, "Comment테스트 user_no : " + MyConfig.myInfo.getUser_no());
//                mContext.startActivity(intent);
                ((Activity)mContext).startActivityForResult(intent, 0);

            }else if(viewId == R.id.the_number_of_comments){
                Toast.makeText(mContext, "댓글 버튼 클릭", Toast.LENGTH_SHORT).show();
                // 새로운 엑티비티 띄워주자..!
                Intent intent = new Intent(mContext, CommentActivity.class);
                // intent에 content_no 보내줘야함..!
                intent.putExtra("position", getLayoutPosition());
                intent.putExtra("user_no", MyConfig.myInfo.getUser_no());
                intent.putExtra("content_no", contentInfoList.get(position).getContent_no());
                ((Activity)mContext).startActivityForResult(intent, 0);

            }else if(viewId == R.id.poster_profile_view){           // 상대방 타임라인 방문
                if(Integer.parseInt(contentInfoList.get(position).getContent_host_no()) == MyConfig.myInfo.getUser_no()){
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "nav_my_timeline_fragment");
                    transaction.addToBackStack("");
                    transaction.commit();
                }else{
                    Intent intent = new Intent(mContext, OtherTimelineActivity.class);
                    intent.putExtra("user_no", MyConfig.myInfo.getUser_no());
                    Log.d(TAG, "host_no : " + contentInfoList.get(position).getContent_host_no());
                    intent.putExtra("host_no", contentInfoList.get(position).getContent_host_no());
                    mContext.startActivity(intent);
//                    ((Activity)mContext).overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//                    ((Activity)mContext).startActivityForResult(intent, -1);

                }

            }
//            else{
//                Log.d(TAG, "span테스트 onClick");
//                TextView tv = (TextView)view.;
//                Spanned s = (Spanned) tv.getText();
//                int start = s.getSpanStart(this);
//                int end = s.getSpanEnd(this);
//                Log.d(TAG, "테스트 : " + s.subSequence(start, end));
//
//            }
        }
    }

    public interface EndlessScrollListener{
        boolean onLoadMore(int position);
    }

    public void setListAll(List<ContentInfo> list){
        contentInfoList = list;
    }


    public void descriptionToHashNavigation(String description, TextView tv){

        boolean start_tag = false;
        String des = "";
        description = " " + description + " ";
        int start = 0;
        int end = 0;
        Log.d(TAG, "description : " + description);
        for(int i=0; i<description.length()-1; i++){
            if(String.valueOf(description.charAt(i)).equals("#") && start_tag == false && !(String.valueOf(description.charAt(i+1)).equals("#")) && !String.valueOf(description.charAt(i+1)).equals(" ")){

                des = "";
                start_tag = true;
                start = i;

            }else if(((String.valueOf(description.charAt(i)).equals("#")|| String.valueOf(description.charAt(i)).equals(" ")) && start_tag == true)){
                end = i;
                span(start, end);

//                des = description.substring(start, end);
//                final TextView textView = new TextView(mContext);
//                textView.setText(String.valueOf(description.substring(start,end)));
//                textView.setTypeface(null, Typeface.BOLD);
//                textView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.replace(R.id.frame_layout, SearchFragment.newInstance(textView.getText().toString().substring(1)), "nav_search_fragment");
//                        transaction.addToBackStack("hash_serach_fragment");
//                        transaction.commit();
//                    }
//                });
//                linearLayout.addView(textView);

                start = 0;
                end = 0;
                start_tag = false;
                if(String.valueOf(description.charAt(i)).equals("#") && start_tag == false && !(String.valueOf(description.charAt(i+1)).equals("#")) && !String.valueOf(description.charAt(i+1)).equals(" ")){
                    start = i;
                    start_tag = true;
                }

            }
        }

    }

    public void span(int start, int end){
        Log.d(TAG, "span테스트 : " + start + " , " + end);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(TAG, "span테스트 onClick");
                TextView tv = (TextView) widget;
                Spanned s = (Spanned) tv.getText();
                int start = s.getSpanStart(this);
                int end = s.getSpanEnd(this);
                Log.d(TAG, "테스트 : " + s.subSequence(start, end));

                // 여기서 네비게이션 처리..!
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, SearchFragment.newInstance(s.subSequence(start, end).toString()), "nav_search_fragment");
                transaction.addToBackStack("hash_serach_fragment");
                transaction.commit();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.linkColor = 0xff000000;
                ds.setUnderlineText(false);
                ds.bgColor = 0xffffffff;


//                super.updateDrawState(ds);
            }

        }, start, end-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        final SpannableStringBuilder str = new SpannableStringBuilder();
        span.setSpan(new StyleSpan(Typeface.BOLD), start,end-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
