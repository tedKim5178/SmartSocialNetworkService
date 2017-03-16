package com.example.mk.mysmartsns.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mk on 2017-03-16.
 */

public class CallManagement {
    private static String TAG = CallManagement.class.getSimpleName();

    public static CallManagement callManagement;

    HashMap callManageHashMap;

    public CallManagement(){
        callManageHashMap = new HashMap();
    }

    public static void test(){

    }

    public HashMap getCallManageHashMap(){
        return callManageHashMap;
    }

    public static CallManagement getInstance(){
        if(callManagement == null){
            callManagement = new CallManagement();
        }
        return callManagement;
    }
    public void addCall(String key, boolean isUsed){
        callManageHashMap.put(key, isUsed);
        // addCall이 실행되면 무조건 prefetching stop!!
        Log.d(TAG, "Prefetching Stop");
    }
    public  void subtractCall(String key, boolean isUsed){
        callManageHashMap.put(key, isUsed);

        Iterator<String> keys = callManageHashMap.keySet().iterator();
        int count = 0;
        while(keys.hasNext()){
            String keyName = keys.next();
            Log.d(TAG, "Prefetching keyName : " + keyName);
            if((boolean)callManageHashMap.get(keyName) == false){
                count = count + 1;
            }
        }
        Log.d(TAG, "Prefetching HashMap size" + callManageHashMap.size());
        Log.d(TAG, "Prefetching count " + count);
        if(count == callManageHashMap.size()){
            // 모두 사용 안하고 있으니까 prefetching start!
            Log.d(TAG, "Prefetching Start");
        }
    }
}
