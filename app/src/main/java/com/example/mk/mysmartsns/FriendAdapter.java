//package com.example.mk.mysmartsns;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
///**
// * Created by mk on 2017-02-05.
// */
//
//public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{
//    private static final String TAG = LoginActivity.class.getSimpleName();
//    private Context mContext;
//    @Override
//    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item_list, parent, false);
//
//        return new FriendViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(FriendViewHolder holder, int position) {
//        // 사진이랑 이름
//    }
//
//    @Override
//    public int getItemCount() {
//        //@@@ 수정필요
//        return 3;
//    }
//
//    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        ImageView friend_profile_pic;
//        TextView friend_name;
//
//        public FriendViewHolder(View itemView){
//            super(itemView);
//            friend_profile_pic = itemView.findViewById();
//            friend_name = itemView.findViewById();
//            itemView.setOnClickListener(this);
//        }
//        @Override
//        public void onClick(View view) {
//
//            // 아이템이 클릭되면 여기로온다!
//        }
//    }
//}
