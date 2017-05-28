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
import java.util.Set;

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
        Log.d(TAG, "CallManagementff addCall!!!" + key);

        printCall();
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
        Log.d(TAG, "CallManagementff subTract!!!" + key);
        printCall();
        if(count == callManageHashMap.size()){
            if(PrefetchConfig.prefetching_queue.size() != 0) {
                PrefetchDownload.newInstance(this).initUrl(APIConfig.prefetchUrl + PrefetchConfig.prefetching_queue.peek()).startPrefetching();
            }
        }
    }

    public void clearCall(){                    // call이 남아있는 경우가 있음 -> clear(),  by gilsoo.
        if(callManagement != null)
            callManagement = null;
    }

    public void printCall(){
        if(callManagement!=null){
            Set<String> set = callManagement.callManageHashMap.keySet();
            Iterator<String> iter = set.iterator();
            Log.d(TAG, String.valueOf(callManagement.callManageHashMap.size()));
            while(iter.hasNext()){
                String key = iter.next();
                Log.d(TAG, "CallManagementff : " + key + ", " + callManagement.callManageHashMap.get(key));
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
        }else{
            MainActivity.updateProgressBar(new Message(100, "Prefetch Completed",100));
        }
    }



    public static void remainInQueue(int remainCalls){

    }
}
