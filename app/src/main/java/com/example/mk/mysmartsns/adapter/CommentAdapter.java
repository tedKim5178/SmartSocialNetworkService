package com.example.mk.mysmartsns.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.network.info.CommentInfo;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
/**
 * Created by mk on 2017-02-23.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    Context mContext;
    List<CommentInfo> commentInfoList;
    private static final String TAG = TimelineAdapter.class.getSimpleName();

    public CommentAdapter(Context mContext, List<CommentInfo> commentInfoList){
        this.mContext = mContext;
        this.commentInfoList = commentInfoList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item_list, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if(commentInfoList.get(position).getUser_profile() == null) {
            Glide.with(mContext).load(R.drawable.personurl).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.profile_picture_in_comment);
            Log.d(TAG, "CommentAdpaterTest when this user does not set his profile photo : ");
        }
        else {
            Glide.with(mContext).load(APIConfig.baseUrl + "profile_image/" + commentInfoList.get(position).getUser_profile()).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.profile_picture_in_comment);
            Log.d(TAG, "CommentAdpaterTest when this user has his profile photo : " + APIConfig.baseUrl + "/" + commentInfoList.get(position).getUser_profile());
        }
        holder.user_name.setText(commentInfoList.get(position).getUser_id());
        holder.comment.setText(commentInfoList.get(position).getComment_name());
        Log.d(TAG, "BindViewHolder : " + commentInfoList.get(position).getComment_name());
    }

    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_picture_in_comment;
        TextView user_name;
        TextView comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            profile_picture_in_comment = (ImageView) itemView.findViewById(R.id.profile_picture_in_comment);
            user_name = (TextView) itemView.findViewById(R.id.user_name_in_comment);
            comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }

    public void setCommentData(List<CommentInfo> commentInfoList){
        this.commentInfoList = commentInfoList;
    }
}