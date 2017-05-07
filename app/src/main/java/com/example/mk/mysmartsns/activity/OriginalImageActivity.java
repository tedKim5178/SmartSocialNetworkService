package com.example.mk.mysmartsns.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.io.File;

/**
 * Created by mk on 2017-02-15.
 */

public class OriginalImageActivity extends AppCompatActivity{
    private static final String TAG = OriginalImageActivity.class.getSimpleName();
    File file;
    ImageView original_image_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_image);

        original_image_view = (ImageView)findViewById(R.id.original_image_view);
        String bigHashInfo = getIntent().getStringExtra("big_hash_info");
        String smallHashInfo = getIntent().getStringExtra("small_hash_info");
        String thumbnail_url = getIntent().getStringExtra("thumbnail_url");

        String str = "thumbnail_contents/";
        int str_length = str.length();
        String prefetchImageUrl = thumbnail_url.substring(str_length);

        file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + PrefetchConfig.Local_Name + "/" + prefetchImageUrl);

        int user_no = MyConfig.myInfo.getUser_no();



        if(file.exists()){
            // 있으면 로컬에서 가져오고
            Log.d(TAG, "테스트1 : 프리패칭된 이미지에서 가지고 옵니다.");
            Glide.with(OriginalImageActivity.this).load(file).into(original_image_view);
        }else{
            InteractionManager.getInstance(this).requestContentOriginalDownload(thumbnail_url,bigHashInfo, smallHashInfo, user_no, new OnMyApiListener() {
                @Override
                public void success(Object response) {
                    ContentInfo contentInfo = (ContentInfo) response;
                    Log.d(TAG, "테스트1 :");
                    if(contentInfo != null) {
                        Log.d(TAG, "테스트1: 프리패칭이 되지 않아서 url로 부터 이미지를 가지고 옵니다." + APIConfig.baseUrl + "/" + APIConfig.baseUrl + contentInfo.getContent_url());
                        Glide.with(OriginalImageActivity.this).load(APIConfig.baseUrl + contentInfo.getContent_url()).into(original_image_view);
                    }
                }

                @Override
                public void fail() {

                }
            });
        }
//


    }
}
