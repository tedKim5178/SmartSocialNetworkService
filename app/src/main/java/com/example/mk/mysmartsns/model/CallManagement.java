package com.example.mk.mysmartsns.model;

import android.content.Context;
import android.util.Log;

import com.example.mk.mysmartsns.PrefetchingThread;
import com.example.mk.mysmartsns.activity.MainActivity;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.prefetch.Message;
import com.example.mk.mysmartsns.prefetch.PrefetchDownload;
import com.example.mk.mysmartsns.prefetch.ResumeDownloadListener;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mk on 2017-03-16.
 */

public class CallManagement implements ResumeDownloadListener{
    private static String TAG = CallManagement.class.getSimpleName();
    public static CallManagement callManagement;
    PrefetchingThread prefetchingThread ;
    HashMap callManageHashMap;
    Context context;

    public CallManagement(Context context){
        callManageHashMap = new HashMap();
        prefetchingThread = new PrefetchingThread();
//        prefetchingThread.start();
        this.context = context;
    }

    public static void test(){

    }

    public HashMap getCallManageHashMap(){
        return callManageHashMap;
    }

    // Singleton
    public static CallManagement getInstance(Context context){
        if(callManagement == null){
            callManagement = new CallManagement(context);
        }
        return callManagement;
    }

    public void addCall(String key, boolean isUsed){
        callManageHashMap.put(key, isUsed);
        PrefetchDownload.newInstance(this).stopPrefetching();
        Log.d(TAG, "Prefetching Stop");
    }

    public  void subtractCall(String key, boolean isUsed){
        callManageHashMap.put(key, isUsed);

        Iterator<String> keys = callManageHashMap.keySet().iterator();
        int count = 0;
        while(keys.hasNext()){
            String keyName = keys.next();
            if((boolean)callManageHashMap.get(keyName) == false){
                count = count + 1;
            }
        }
        if(count == callManageHashMap.size()){
            if(PrefetchConfig.prefetching_queue.size() != 0) {
                PrefetchDownload.newInstance(this).initUrl(APIConfig.prefetchUrl + PrefetchConfig.prefetching_queue.peek()).startPrefetching();
            }
        }
    }


    @Override
    public void progressUpdate(Message message) {
//        ((MainActivity)context).updateProgressBar(message);
        MainActivity.updateProgressBar(message);

    }

    @Override
    public void onComplete() {
        Log.d(TAG, "CallManagerMent:::Prefetching onComplete");
        Log.d(TAG, "CallManagerMent::: element - " +  PrefetchConfig.prefetching_queue.peek());
        PrefetchConfig.prefetching_queue.poll();        // 완료된 프레페칭 콘텐츠 큐에서 제거
        if(!PrefetchConfig.prefetching_queue.isEmpty()) {
            MainActivity.updateProgressBar(new Message(0,PrefetchConfig.prefetching_queue.peek() ,100));
            PrefetchDownload.newInstance(this).initUrl(APIConfig.prefetchUrl + PrefetchConfig.prefetching_queue.peek()).startPrefetching();
        }
    }
}
