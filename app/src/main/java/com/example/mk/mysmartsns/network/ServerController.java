package com.example.mk.mysmartsns.network;

import android.app.Application;

import com.example.mk.mysmartsns.config.APIConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class ServerController extends Application{
    private final String TAG = ServerController.class.getSimpleName();

    private static ServerController instance;           // singleton
    private SNSService snsService;

    @Override
    public void onCreate() {
        super.onCreate();

        ServerController.instance = this;
        instance.buildSNSService();                          // retrofit build !!
    }
    private void buildSNSService(){
        synchronized (Application.class){
            if(snsService == null){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIConfig.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                snsService = retrofit.create(SNSService.class);
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;            // 종료될때 초기화
    }

    public static ServerController getInstance(){ return instance; }
    public SNSService getSnsService(){ return snsService; }
}
