package com.example.mk.mysmartsns.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.network.info.BigHashInfo;

import java.util.List;

/**
 * Created by mk on 2017-02-22.
 */

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder>{
    private static final String TAG = InterestsAdapter.class.getSimpleName();
    Context mContext;
    List<BigHashInfo> bigHashList;

    public InterestsAdapter(Context mContext, List<BigHashInfo> bigHashList) {
        this.mContext = mContext;
        this.bigHashList = bigHashList;
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.interest_list_item, parent, false);
        return new InterestsAdapter.InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InterestsViewHolder holder, int position) {

        if(bigHashList.get(position).isCheck() == false){
            holder.interest.setText("#" + bigHashList.get(position).getBighash_name() + ", ");
            holder.interest.setTextColor(Color.BLACK);
        }else{
            holder.interest.setText("#" + bigHashList.get(position).getBighash_name() + ", ");
            holder.interest.setTextColor(Color.BLUE);
        }
    }


    @Override
    public int getItemCount() {
        return bigHashList.size();
    }

    class InterestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // TextView 하나 있을 꺼다..!
        TextView interest;
        public InterestsViewHolder(View itemView) {
            super(itemView);
            interest = (TextView)itemView.findViewById(R.id.interest_item);
            interest.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            int count = 0;
            if(bigHashList.get(position).isCheck() == false){
                for(int i=0; i<bigHashList.size(); i++){
                    if( bigHashList.get(i).isCheck() == true){
                        count = count + 1;
                    }
                }
                if(count == 3){
                    Toast.makeText(mContext, "3개를 모두 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                }else if(count < 3){
                    bigHashList.get(position).setCheck(true);
                }
            }else{
                bigHashList.get(position).setCheck(false);
            }
            notifyItemChanged(position);
        }
    }
}
