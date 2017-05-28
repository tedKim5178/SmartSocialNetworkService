package com.example.mk.mysmartsns.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.fragment.fragment_main.OtherProfileFragment;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.mk.mysmartsns.config.APIConfig.baseUrl;

/**
 * Created by mk on 2017-02-15.
 */

/**
 * Created by mk on 2017-02-15.
 */

public class AllFriendAdapter extends RecyclerView.Adapter<AllFriendAdapter.AllFriendViewHolder>{
    private static final String TAG = AllFriendAdapter.class.getSimpleName();
    private Context mContext;
    TimelineAdapter.EndlessScrollListener endlessScrollListener;
    List<UserInfo> userInfoList;
    private FragmentManager fragmentManager;
    public AllFriendAdapter(Context mContext, List<UserInfo> userInfoList, FragmentManager fragmentManager){
        this.mContext = mContext;
        this.userInfoList = userInfoList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public AllFriendAdapter.AllFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item_list, parent, false);
        return new AllFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllFriendViewHolder holder, int position) {
        Log.d(TAG, "친구검색테스트 in onBIndViewHolder" );
        if(userInfoList.get(position).getUser_profile_url() != null){
            // TODO :: check
            Glide.with(mContext).load(baseUrl + userInfoList.get(position).getUser_profile_url()).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.friend_profile_in_friend_search_fragment);
        }else{
            Glide.with(mContext).load(R.drawable.personurl).into(holder.friend_profile_in_friend_search_fragment);
        }
        holder.friend_name_in_friend_search_fragment.setText(userInfoList.get(position).getUser_name());
        holder.friend_id_in_friend_search_fragment.setText(userInfoList.get(position).getUser_id());
    }


    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    class AllFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView friend_profile_in_friend_search_fragment;
        TextView friend_name_in_friend_search_fragment;
        TextView friend_id_in_friend_search_fragment;

        public AllFriendViewHolder(View itemView){
            super(itemView);
            friend_profile_in_friend_search_fragment = (ImageView) itemView.findViewById(R.id.friend_profile_in_friend_search_fragment);
            friend_name_in_friend_search_fragment = (TextView) itemView.findViewById(R.id.friend_name_in_friend_search_fragment);
            friend_id_in_friend_search_fragment = (TextView)itemView.findViewById(R.id.friend_id_in_friend_search_fragment);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            int user_no = userInfoList.get(position).getUser_no();
            String profile_url = userInfoList.get(position).getUser_profile_url();
            String user_name = userInfoList.get(position).getUser_name();
            String user_id = userInfoList.get(position).getUser_id();

            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, OtherProfileFragment.newInstance(user_no, profile_url, user_name, user_id), "nav_other_profile_fragment");
            transaction.addToBackStack("");
            transaction.commit();
        }
    }
}
