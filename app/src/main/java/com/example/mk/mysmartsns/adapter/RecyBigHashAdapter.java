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
import com.example.mk.mysmartsns.network.info.BigHashInfo;

import java.util.List;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class RecyBigHashAdapter extends RecyclerView.Adapter<RecyBigHashAdapter.UploadHashViewHolder>{
    private static final String TAG = RecyBigHashAdapter.class.getSimpleName();
    private static final int MAX_COUNT = 3;     // 최대 hashtag 선택 가능수 (Bighash)
    private List<BigHashInfo> list;
    private Context context;
    private int count;      // hash tag 선택 count

    public RecyBigHashAdapter(Context context, List<BigHashInfo> list){
        this.context = context;
        this.list = list;
        count = 0;
    }

    public void addAllData(List<BigHashInfo> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public UploadHashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rows_bighash, parent, false);
        return new UploadHashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UploadHashViewHolder holder, int position) {
        holder.bigHash.setText("#" + list.get(position).getBighash_name());
        Log.d(TAG, list.get(position).getBighash_name());
        if(list.get(position).isCheck()){
            holder.bigHash.setTextColor(Color.BLACK);
        }else{
            holder.bigHash.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UploadHashViewHolder extends RecyclerView.ViewHolder{
        TextView bigHash;

        public UploadHashViewHolder(View itemView) {
            super(itemView);
            bigHash = (TextView)itemView.findViewById(R.id.bigHash);
            bigHash.setTextColor(Color.LTGRAY);
            bigHash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getAdapterPosition()).isCheck()) {
                        list.get(getAdapterPosition()).setCheck(false);
                        bigHash.setTextColor(Color.LTGRAY);
                        count --;           // hash tag갯수 제한
                    } else {
                        if(count < MAX_COUNT) {
                            list.get(getAdapterPosition()).setCheck(true);
                            bigHash.setTextColor(Color.BLACK);
                            count ++;           // hash tag갯수 제한
                        }else{
                            Toast.makeText(context, "해쉬태그는 최대 "+ MAX_COUNT +"개까지만 선택됩니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
