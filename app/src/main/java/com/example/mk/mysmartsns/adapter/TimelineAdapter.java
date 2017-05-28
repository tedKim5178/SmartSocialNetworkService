package com.example.mk.mysmartsns.adapter;

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

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.activity.OriginalImageActivity;
import com.example.mk.mysmartsns.activity.OtherTimelineActivity;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.fragment.CommentFragment;
import com.example.mk.mysmartsns.fragment.LikeFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.SearchFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.TimelineFragment;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.mk.mysmartsns.config.APIConfig.baseUrl;

/**
 * Created by mk on 2017-02-02.
 */

/**
 * Created by mk on 2017-02-02.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.PostViewHolder>{
    private static final String TAG = TimelineAdapter.class.getSimpleName();
    private Context mContext;
    Spannable span;

    private FragmentManager fragmentManager;
    //    EndlessScrollListener endlessScrollListener;
    public List<ContentInfo> contentInfoList;

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

        Log.d(TAG, "ThumbnailContentsNo : " + contentInfoList.get(position).getContent_no());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Integer.parseInt(contentInfoList.get(position).getContent_width()), Integer.parseInt(contentInfoList.get(position).getContent_height()));        // 크기 지정
        holder.image_view.setLayoutParams(params);
        Glide.with(mContext).load(baseUrl + "/" + contentInfoList.get(position).getContent_url()).into(holder.image_view);
        if(contentInfoList.get(position).getContent_host_profile_url() == null)
            Glide.with(mContext).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.poster_profile);
        else {
            Glide.with(mContext).load(baseUrl + "profile_image/" + contentInfoList.get(position).getContent_host_profile_url()).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.poster_profile);
        }
        holder.the_number_of_likes.setText(String.valueOf(contentInfoList.get(position).getContent_like_count()) + "명이 좋아합니다");
        holder.the_number_of_comments.setText(String.valueOf(contentInfoList.get(position).getContent_comment_count()) + "개의 댓글 보기");
        holder.poster_name.setText(contentInfoList.get(position).getContent_host());

        String description = contentInfoList.get(position).getContent_desc();
        span = Spannable.Factory.getInstance().newSpannable(description);
        descriptionToHashNavigation(description, holder.post_description_textview, position);
        holder.post_description_textview.setText(span);
        holder.post_description_textview.setMovementMethod(LinkMovementMethod.getInstance());

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
                        // 빅해쉬 넘버를 넘겨줘야함
                        int bighash_no = 0;
                        for(int i=0; i<contentInfoList.get(position).getHash_list().size(); i++){
                            if(contentInfoList.get(position).getHash_list().get(i).getBighash_name().equals(hashText.getText().toString().substring(1))){
                                bighash_no = contentInfoList.get(position).getHash_list().get(i).getBighash_no();
                            }
                        }
                        Log.d(TAG, "빅해쉬카운트테스트 in TimelineAdapter : " + bighash_no);
                        transaction.replace(R.id.frame_layout, SearchFragment.newInstance(hashText.getText().toString().substring(1), bighash_no, 0), "nav_search_fragment");
                        transaction.addToBackStack("");
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
                // 원본이미지(original보기) 보기
                Gson gson = new Gson();
                ArrayList<String> smallHashArray = new ArrayList<>();
                for(int i=0; i<contentInfoList.get(getAdapterPosition()).getSmallHash_list().size(); i++){
                    smallHashArray.add(gson.toJson(contentInfoList.get(getAdapterPosition()).getSmallHash_list().get(i).getSmallhash_no()));
                    Log.d(TAG, "스몰해시테스트 : " + contentInfoList.get(getAdapterPosition()).getSmallHash_list().size());
                }
                String smallHashInfo = gson.toJson(smallHashArray);
                ArrayList bigHashArray = new ArrayList();
                if(contentInfoList.get(getAdapterPosition()).getHash_list() != null){
                    for(int i=0; i<contentInfoList.get(getAdapterPosition()).getHash_list().size(); i++){
                        bigHashArray.add(contentInfoList.get(getAdapterPosition()).getHash_list().get(i).getBighash_no());
                    }
                }

                String bigHashInfo = gson.toJson(bigHashArray);

                Intent intent = new Intent(mContext, OriginalImageActivity.class);
                intent.putExtra("big_hash_info", bigHashInfo);
                intent.putExtra("small_hash_info", smallHashInfo);
                intent.putExtra("thumbnail_url", contentInfoList.get(getAdapterPosition()).getContent_url());

                // 원본이지지 볼 때 새로운 activity를 띄워준뒤에 그 이미지를 intent로 넘겨준다. 띄워줄 때 만약 이 사진이 프리패칭 되어있다면
                // 내부 저장소에 담겨져 있는 파일들 갯수 가져와야지...!

                mContext.startActivity(intent);
            }else if(viewId == R.id.like_button){
                // 좋아요 눌렀을때
                Log.d(TAG, "position : likf_flag value in clicklistener  - "+contentInfoList.get(position).getContent_like_flag());
                if(contentInfoList.get(position).getContent_like_flag() == 0){

                    // 좋아요를 누르면
                    InteractionManager.getInstance(mContext).requestCountLikeIncrease(MyConfig.myInfo.getUser_no(), contentInfoList.get(position), new OnMyApiListener() {
                        @Override
                        public void success(Object response) {
                            Log.d(TAG, "좋아요카운트테스트!!");
                        }

                        @Override
                        public void fail() {
                            Log.d(TAG, "좋아요카운트테스트 fail!!");
                        }
                    });

                    // 유저 정보와 contentInfo에서의 정보를 넘겨줌. 그렇다면 contentInfo속의 주인 즉 host정보를 빼올 수 있을 것이다. 그럼 이 두가지 이용해서 count 증가 시켜주자
                    InteractionManager.getInstance(mContext).requestLikeClicked(MyConfig.myInfo.getUser_no(), contentInfoList.get(position).getContent_no(), new OnMyApiListener() {
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
                }else{                                                                  // 좋아요 취소 event
                    // 유저 정보와 contentInfo에서의 정보를 넘겨줌. 그렇다면 contentInfo속의 주인 즉 host정보를 빼올 수 있을 것이다. 그럼 이 두가지 이용해서 count 빼주자
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
                // 좋아요 눌렀을 때 이벤트
//                Intent peopleWhoLikeThisPostIntent = new Intent(mContext, PeopleWhoLikeThisPostActivity.class);
//                peopleWhoLikeThisPostIntent.putExtra("contentNo", contentInfoList.get(position).getContent_no());
//                ((Activity)mContext).startActivityForResult(peopleWhoLikeThisPostIntent, 888);

                int content_no = contentInfoList.get(position).getContent_no();

                Log.d(TAG, "좋아요눌렀을때테스트");
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, LikeFragment.newInstance(content_no), "nav_like_fragment");
                transaction.addToBackStack("");
                transaction.commit();

            }else if(viewId == R.id.comment_button){
                // 댓글 버튼 눌렀을 때 이벤트
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, CommentFragment.newInstance(contentInfoList.get(position)), "nav_comment_fragment");
                transaction.addToBackStack("");
                transaction.commit();

            }else if(viewId == R.id.the_number_of_comments){

                Log.d(TAG, "comment button clicked !!");
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, CommentFragment.newInstance(contentInfoList.get(position)), "nav_comment_fragment");
                transaction.addToBackStack("");
                transaction.commit();

            }else if(viewId == R.id.poster_profile_view){           // 상대방 타임라인 방문
                if(Integer.parseInt(contentInfoList.get(position).getContent_host_no()) == MyConfig.myInfo.getUser_no()){
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // TODO:: 다른사람 타임라인 방문할 때 추가적으로 profile_url도 넘겨줘야함
                    transaction.replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "nav_my_timeline_fragment");
                    transaction.addToBackStack("");
                    transaction.commit();
                }else{
                    Intent intent = new Intent(mContext, OtherTimelineActivity.class);
                    intent.putExtra("user_no", MyConfig.myInfo.getUser_no());
                    intent.putExtra("host_profile_url", contentInfoList.get(position).getContent_host_profile_url());
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


    public void descriptionToHashNavigation(String description, TextView tv, int position){

        // 서버에서는 smallhash를 description형태로 가지고 있었던거다..!
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
                span(start, end, position);
                // start+1 부터 end까지 smallhash 임..!
                String smallHash = description.substring(start+1, end);

//                if(!contentInfoList.get(position).getSmallHash_list().contains(smallHash)){
//                    contentInfoList.get(position).getSmallHash_list().add(description.substring(start+1, end));
//                }
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

    public void span(int start, int end, final int position){
        final int position_final = position;
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

                // 여기서 스몰 해쉬 네비게이션 처리..!
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                String smallhash = s.subSequence(start, end).toString();
                int smallhash_no = 0;
                // 검색
                for(int i=0; i<contentInfoList.get(position).getSmallHash_list().size(); i++){
                    if(contentInfoList.get(position).getSmallHash_list().get(i).getSmallhash_name().equals(smallhash)){
                        smallhash_no = contentInfoList.get(position).getSmallHash_list().get(i).getSmallhash_no();
                    };
                }

                Log.d(TAG, "리스트테스트 왜 여기 안되는데 ㅆㅃ");
                transaction.replace(R.id.frame_layout, SearchFragment.newInstance(smallhash, contentInfoList.get(position_final).getHash_list(), smallhash_no,1), "nav_search_fragment");
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

    public void addContentInfo(List<ContentInfo> contentInfos){
        for(int i=0; i<contentInfos.size(); i++){
            contentInfoList.add(contentInfos.get(i));
        }
    }


}
