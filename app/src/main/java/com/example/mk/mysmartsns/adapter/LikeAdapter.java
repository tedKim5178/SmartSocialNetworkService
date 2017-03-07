package com.example.mk.mysmartsns.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.activity.OtherTimelineActivity;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mk on 2017-02-15.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeViewHolder>{
    private static final String TAG = LikeAdapter.class.getSimpleName();
    private Context mContext;
    TimelineAdapter.EndlessScrollListener endlessScrollListener;
    List<UserInfo> userInfoList;
    private FragmentManager fragmentManager;

    public LikeAdapter(Context mContext, List<UserInfo> userInfoList, FragmentManager fragmentManager){
        this.mContext = mContext;
        this.userInfoList = userInfoList;
        this.fragmentManager = fragmentManager;
    }
    public LikeAdapter(Context mContext, List<UserInfo> userInfoList){
        this.mContext = mContext;
        this.userInfoList = userInfoList;
    }

    @Override
    public LikeAdapter.LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.like_item_list, parent, false);
        return new LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikeAdapter.LikeViewHolder holder, int position) {
        if(userInfoList.get(position).getUser_profile_url() == null){
            Glide.with(mContext).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.person_profile_like_this_post);
        }else{
            Glide.with(mContext).load(Uri.parse(userInfoList.get(position).getUser_profile_url())).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.person_profile_like_this_post);
        }
        Log.d(TAG, "Like리사이클러뷰 정상작동 position " + position);
        holder.person_name_like_this_post.setText(userInfoList.get(position).getUser_name());
        holder.person_id_who_this_post.setText(userInfoList.get(position).getUser_id());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "좋아요누른사람들보는테스트 getItemCount : " + userInfoList.size());
        return userInfoList.size();
    }

    class LikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView person_profile_like_this_post;
        TextView person_name_like_this_post;
        TextView person_id_who_this_post;

        public LikeViewHolder(View itemView){
            super(itemView);
            person_profile_like_this_post = (ImageView) itemView.findViewById(R.id.person_profile_who_like_post);
            person_name_like_this_post = (TextView) itemView.findViewById(R.id.person_name_who_like_post);
            person_id_who_this_post = (TextView) itemView.findViewById(R.id.person_id_who_like_post);
            person_profile_like_this_post.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.person_profile_who_like_post:
//                    Log.d(TAG, userInfoList.get(getAdapterPosition()).getUser_no()+"");
                    if(userInfoList.get(getAdapterPosition()).getUser_no() == MyConfig.myInfo.getUser_no()){
                        //  내 정보를 클릭했을떄 MyTimeline으로 가게
                        if(fragmentManager == null) {
                            ((Activity) mContext).setResult(Activity.RESULT_OK);
                            ((Activity) mContext).finish();
                        }else {
                            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "nav_my_timeline_fragment");
                            transaction.addToBackStack("");
                            transaction.commit();
                        }
                    }else{
                        Intent intent = new Intent(mContext, OtherTimelineActivity.class);
                        intent.putExtra("user_no", MyConfig.myInfo.getUser_no());
                        intent.putExtra("host_no", String.valueOf(userInfoList.get(getAdapterPosition()).getUser_no()));
                        mContext.startActivity(intent);

                    }
                    break;
            }
        }
    }
}
