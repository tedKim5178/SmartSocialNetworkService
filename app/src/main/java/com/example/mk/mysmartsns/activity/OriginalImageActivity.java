package com.example.mk.mysmartsns.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.CallManagement;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;
import com.example.mk.mysmartsns.prefetch.OriginalDownload;
import com.example.mk.mysmartsns.prefetch.ResumeDownloadListener;

import java.io.File;
import java.util.Iterator;

/**
 * Created by mk on 2017-02-15.
 */

public class OriginalImageActivity extends AppCompatActivity implements ResumeDownloadListener{
    private static final String TAG = OriginalImageActivity.class.getSimpleName();

    //ToDo. gilsoo_변수명 바꾸기 영어로 뭐라해야될지 모르겟따 ㅠ
    final String DOWNLOAD_ORIGINAL_SEMI_IMAGE = "download the original image semi prefetched";
    final String DOWNLOAD_ORIGINAL_IMAGE = "download the original image";
    boolean isFollowing = false;
    File file;
    String prefetchImageUrl;
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
       prefetchImageUrl = thumbnail_url.substring(str_length);

        file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + PrefetchConfig.Local_Name + "/" + prefetchImageUrl);

        int user_no = MyConfig.myInfo.getUser_no();

        // 서버와 통신하여 original image url 과 original image size를 받아온다.
        CallManagement.getInstance().addCall(DOWNLOAD_ORIGINAL_IMAGE, true);
        InteractionManager.getInstance(this).requestContentOriginalDownload(thumbnail_url,bigHashInfo, smallHashInfo, user_no, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                ContentInfo contentInfo = (ContentInfo) response;

                if(contentInfo != null) {
                    if(file.exists() && file.length() >= contentInfo.getContent_size()){              // 로컬에 다 받아져 있다면
                        Log.d(TAG, "InLocal :: file.length() : " + file.length() + ", content_size : " + contentInfo.getContent_size());
                        Toast.makeText(getBaseContext(), "로컬에서 이미지 로드", Toast.LENGTH_SHORT).show();
                        Glide.with(OriginalImageActivity.this).load(file).into(original_image_view);
                    }else if(file.exists() && file.length() < contentInfo.getContent_size()){                     // 로컬에 일부분만 받아져 있다면
                        isFollowing = true;
                        Log.d(TAG, "InLocal&Server :: file.length() : " + file.length() + ", content_size : " + contentInfo.getContent_size());
                        Toast.makeText(getBaseContext(), "로컬+서버 이미지 로드", Toast.LENGTH_SHORT).show();

                        CallManagement.getInstance().addCall(DOWNLOAD_ORIGINAL_SEMI_IMAGE, true);
                        Log.d(TAG, "prefetchImageUrl : " + prefetchImageUrl);
                        OriginalDownload.newInstance().setResumeDownloader(OriginalImageActivity.this).initUrl(APIConfig.prefetchUrl + prefetchImageUrl).startPrefetching();

                    }else{                                                                           // 로컬에 안 받아져 있다면
                        // ToDo. 근데 이것이 만일 프리페칭 리스트에 있었다면   _done
                        if(PrefetchConfig.prefetching_queue.contains(prefetchImageUrl)){
                            Log.d(TAG, "Prefetching queue에 들어있는 contents 라면 -  " + prefetchImageUrl);
                            PrefetchConfig.prefetching_queue.remove(prefetchImageUrl);
                        }
                        Toast.makeText(getBaseContext(), "서버에서 이미지 로드", Toast.LENGTH_SHORT).show();
                        Glide.with(OriginalImageActivity.this).load(APIConfig.baseUrl + contentInfo.getContent_url()).into(original_image_view);
                    }

                }
                CallManagement.getInstance().subtractCall(DOWNLOAD_ORIGINAL_IMAGE, false);
            }

            @Override
            public void fail() {
                CallManagement.getInstance().subtractCall(DOWNLOAD_ORIGINAL_IMAGE, false);
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManagement.getInstance().subtractCall(DOWNLOAD_ORIGINAL_IMAGE, false);
        if(isFollowing) {
            CallManagement.getInstance().subtractCall(DOWNLOAD_ORIGINAL_SEMI_IMAGE, false);
            OriginalDownload.newInstance().stopPrefetching();
            isFollowing = false;
        }
    }

    @Override
    public void progressUpdate() {

    }

    @Override
    public void onComplete() {
        Log.d(TAG, "OriginalImageActivity ::: onComplete()");
        //ToDo. gilsoo_그리고 이렇게 받은 이미지 프리페칭 목록에서 제거해줘야함 - done
        PrefetchConfig.prefetching_queue.remove(prefetchImageUrl);      // 이렇게 하면되나?
        Iterator<String> iter = PrefetchConfig.prefetching_queue.iterator();
        while(iter.hasNext()){
            Log.d(TAG, "OriginalImageActivity ::: " + iter.next());
        }
        CallManagement.getInstance().subtractCall(DOWNLOAD_ORIGINAL_SEMI_IMAGE, false);          // Call 관리에서 빼주고 다른 프리페칭은 시작
        OriginalDownload.newInstance().stopPrefetching();                      // 다음 프리페칭 안되게 멈춰주고
        Glide.with(OriginalImageActivity.this).load(file).into(original_image_view);        // 이미지 로드
    }
}
