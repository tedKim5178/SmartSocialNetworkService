package com.example.mk.mysmartsns.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

/**
 * Created by mk on 2017-02-15.
 */

public class OriginalImageActivity extends AppCompatActivity{

    ImageView original_image_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_image);

        original_image_view = (ImageView)findViewById(R.id.original_image_view);
        InteractionManager.getInstance(this).requestContentOriginalDownload(getIntent().getStringExtra("thumbnail_url"), new OnMyApiListener() {
            @Override
            public void success(Object response) {
                ContentInfo contentInfo = (ContentInfo) response;
                if(contentInfo != null)
                    Glide.with(OriginalImageActivity.this).load(APIConfig.baseUrl+"/"+contentInfo.getContent_url()).into(original_image_view);
            }

            @Override
            public void fail() {

            }
        });

    }
}
