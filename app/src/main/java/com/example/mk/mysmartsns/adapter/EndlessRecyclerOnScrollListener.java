package com.example.mk.mysmartsns.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by mk on 2017-02-08.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener{
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0;  // the total number of items in the dataset after the last load
    private boolean loading = true;
    private int visibleThreshold = 3; // the minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleitem, visibleItemCount, totalItemCount;

    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
        Log.d(TAG, "리사이클러뷰  생성자 " + current_page);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(TAG, "리사이클러뷰  스크롤 " + current_page);
        visibleItemCount = recyclerView.getChildCount();
        Log.d(TAG, "리사이클러뷰 visibleItemCount = " + visibleItemCount);
        totalItemCount = mLinearLayoutManager.getItemCount();
        Log.d(TAG, "리사이클러뷰 totalItemCount = " + totalItemCount);
        firstVisibleitem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if(loading){
            if(totalItemCount > previousTotal){
                loading = false;
                previousTotal = totalItemCount;
                Log.d(TAG, "리사이클러뷰 previous = " + previousTotal);
            }
        }
        if(!loading && (totalItemCount - visibleItemCount)<=(firstVisibleitem + visibleThreshold)){
            // End has been reached

            // Do something
            current_page ++;
            Log.d(TAG, "리사이클러뷰 current_page " + current_page);
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
