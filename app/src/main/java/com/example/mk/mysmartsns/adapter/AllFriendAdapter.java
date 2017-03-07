package com.example.mk.mysmartsns.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.util.List;

import static com.example.mk.mysmartsns.R.id.friend_frist_interest_in_friend_search_fragment;

/**
 * Created by mk on 2017-02-15.
 */

/**
 * Created by mk on 2017-02-15.
 */

public class AllFriendAdapter extends RecyclerView.Adapter<AllFriendAdapter.AllFriendViewHolder>{
    private static final String TAG = LikeAdapter.class.getSimpleName();
    private Context mContext;
    TimelineAdapter.EndlessScrollListener endlessScrollListener;
    List<UserInfo> userInfoList;

    public AllFriendAdapter(Context mContext, List<UserInfo> userInfoList){
        this.mContext = mContext;
        this.userInfoList = userInfoList;
}

    @Override
    public AllFriendAdapter.AllFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item_list, parent, false);
        return new AllFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllFriendViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    class AllFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView friend_profile_in_friend_search_fragment;
        TextView friend_name_in_friend_search_fragment;
        TextView friend_first_interest_in_friend_search_fragment;
        TextView friend_second_interest_in_friend_search_fragment;
        TextView friend_third_interest_in_friend_search_fragment;

        public AllFriendViewHolder(View itemView){
            super(itemView);
            friend_profile_in_friend_search_fragment = (ImageView) itemView.findViewById(R.id.friend_profile_in_friend_search_fragment);
            friend_name_in_friend_search_fragment = (TextView) itemView.findViewById(R.id.friend_name_in_friend_search_fragment);


            friend_first_interest_in_friend_search_fragment = (TextView)itemView.findViewById(friend_frist_interest_in_friend_search_fragment);
            friend_second_interest_in_friend_search_fragment = (TextView)itemView.findViewById(R.id.friend_second_interest_in_friend_search_fragment);
            friend_third_interest_in_friend_search_fragment = (TextView)itemView.findViewById(R.id.friend_third_interest_in_friend_search_fragment);

            itemView.setOnClickListener(this);
            friend_profile_in_friend_search_fragment.setOnClickListener(this);
            friend_name_in_friend_search_fragment.setOnClickListener(this);
            friend_first_interest_in_friend_search_fragment.setOnClickListener(this);
            friend_second_interest_in_friend_search_fragment.setOnClickListener(this);
            friend_third_interest_in_friend_search_fragment.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
