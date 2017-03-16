package com.example.mk.mysmartsns.network.manager;

import android.content.Context;
import android.util.Log;

import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.CallManagement;
import com.example.mk.mysmartsns.network.SNSService;
import com.example.mk.mysmartsns.network.ServerController;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class FriendManager {

    private static String TAG = ContentManager.class.getSimpleName();
    private OnMyApiListener listener;
    private Context context;

    public FriendManager(Context context, OnMyApiListener listener){
        this.context = context;
        this.listener = listener;
    }

    public void requestFriendsList(){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestFriendsList", true);

        Call<List<UserInfo>> friendsList = snsService.requestFriendsList();
        friendsList.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestFriendsList::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestFriendsList::isNotSuccessful() : " + response.code());
                }
                callManagement.subtractCall("requestFriendsList", false);
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d(TAG, "requestFriendsList::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestFriendsList", false);
            }
        });
    }

    public void requestSearchUser(int user_no, String search_name){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestSearchUser", true);

        Call<List<UserInfo>> friendsList = snsService.requestSearchUser(user_no, search_name);
        friendsList.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestSearchUser::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestSearchUser::isNotSuccessful() : " + response.code());
                }
                callManagement.subtractCall("requestSearchUser", false);
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d(TAG, "requestSearchUser::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestSearchUser", false);
            }
        });
    }

}
