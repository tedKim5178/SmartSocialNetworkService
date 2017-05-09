package com.example.mk.mysmartsns.model;

import android.content.Context;
import android.util.Log;

import com.example.mk.mysmartsns.PrefetchingThread;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.PrefetchConfig;
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

    public CallManagement(){
        callManageHashMap = new HashMap();
        prefetchingThread = new PrefetchingThread();
//        prefetchingThread.start();
        // ToDo. gilsoo_이런식으로 큐에다가 받을 이미지 담아논다. 그리고 그냥 시작하면 큐에서 하나씩 빼와서 받아옴, 완료되면 큐에서 제거

    }

    public static void test(){

    }

    public HashMap getCallManageHashMap(){
        return callManageHashMap;
    }

    // Singleton
    public static CallManagement getInstance(){
        if(callManagement == null){
            callManagement = new CallManagement();
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
    public void progressUpdate() {

    }

    @Override
    public void onComplete() {
        Log.d(TAG, "Prefetching onComplete");
        if(!PrefetchConfig.prefetching_queue.isEmpty()) {
            PrefetchDownload.newInstance(this).initUrl(APIConfig.prefetchUrl + PrefetchConfig.prefetching_queue.peek()).startPrefetching();
        }
    }
}
