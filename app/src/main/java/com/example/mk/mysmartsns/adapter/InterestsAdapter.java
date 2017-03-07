package com.example.mk.mysmartsns.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.model.Interest_item;

import java.util.ArrayList;

/**
 * Created by mk on 2017-02-22.
 */

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder>{
    private static final String TAG = InterestsAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<Interest_item> arraylist;

    public InterestsAdapter(Context mContext, ArrayList<Interest_item> arraylist) {
        this.mContext = mContext;
        this.arraylist = arraylist;
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.interest_list_item, parent, false);
        return new InterestsAdapter.InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InterestsViewHolder holder, int position) {
        Log.d(TAG, "Position테스트 : " + position);
        Log.d(TAG, "Position테스트 : " + arraylist.get(position).isClicked());
        if(arraylist.get(position).isClicked() == false){
            holder.interest.setText("#" + arraylist.get(position).getInterest() + ", ");
            holder.interest.setTextColor(Color.BLACK);
        }else{
            holder.interest.setText("#" + arraylist.get(position).getInterest() + ", ");
            holder.interest.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
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
            if(arraylist.get(position).isClicked() == false){
                for(int i=0; i<arraylist.size(); i++){
                    if( arraylist.get(i).isClicked() == true){
                        count = count + 1;
                    }
                }
                if(count == 3){
                    Toast.makeText(mContext, "3개를 모두 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                }else if(count < 3){
                    arraylist.get(position).setClicked(true);
                }
            }else{
                arraylist.get(position).setClicked(false);
            }
            notifyItemChanged(position);
        }
    }
}
