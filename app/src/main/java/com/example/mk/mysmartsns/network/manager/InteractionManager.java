package com.example.mk.mysmartsns.network.manager;

import android.content.Context;
import android.util.Log;

import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class InteractionManager {
    private Context context;
    private static InteractionManager instance = null;

    public static InteractionManager getInstance(Context context){      // singleton
        if(instance == null)
            instance = new InteractionManager();
        instance.setContext(context);                   // context 정보 set
        return instance;
    }
    public void setContext(Context context){this.context = context;}



    //** To UserManager
    public void requestUserLogin(String id, String pw, OnMyApiListener listener){
        new UserManager(context,listener).requestUserLogin(id, pw);
    }
//    public void requestUserRegister(String id, String pw, String name, int age, String gender, int user_interest_bighash1,
//                                    int user_interest_bighash2, int user_interest_bighash3, String user_profile_url, OnMyApiListener listener){
//        new UserManager(context, listener).requestUserRegister(id, pw, name, age, gender,user_interest_bighash1, user_interest_bighash2, user_interest_bighash3, user_profile_url);
//    }

    public void requestUserRegister(String id, String pw, String name, String gender,
                                    int user_interest_first, int user_interest_second, int user_interest_third, String user_profile_url, OnMyApiListener listener){
        new UserManager(context, listener).requestUserRegister(id, pw, name, gender, user_interest_first, user_interest_second, user_interest_third, user_profile_url);
    }

    //** To ContentManager
    public void requestContentThumbnailDownload(int user_no, int current_page, OnMyApiListener listener){
        new ContentManager(context, listener).requestContentThumbnailDownload(user_no, current_page);
    }
    public void requestContentOriginalDownload(String thumbnail_url, String bigHashInfo, String smallHashInfo, int user_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestContentOriginalDownload(thumbnail_url, bigHashInfo, smallHashInfo, user_no);
    }

    public void requestHitInformation(int user_no, boolean hit, OnMyApiListener listener){
        new ContentManager(context, listener).requestHitInformation(user_no, hit);
    }

    public void requestContentBighashDownload(OnMyApiListener listener){
        new ContentManager(context, listener).requestContentBighashDownload();
    }
    public void requestContentSmallhashDownload(String smallhash, OnMyApiListener listener){
        new ContentManager(context, listener).requestContentSmallhashDownload(smallhash);
    }

    public void requestContentUpload(String path, String description, String host, String bighash, String smallhash, OnMyApiListener listener){
        new ContentManager(context, listener).requestContentUpload(path, description, host, bighash, smallhash);
    }

    public void requestLikeClicked(int user_no, int content_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestLikeClicked(user_no, content_no);
    }

    public void requestUnLikeClicked(int user_no, int content_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestUnLikeClicked(user_no, content_no);
    }

    public void requestPeopleWhoLikeThisPost(int content_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestPeopleWhoLikeThisPost(content_no);
    }

    public void requestUserThumbnailContents(int user_no, int host_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestUserThumbnailContents(user_no, host_no);
    }

    public void requestSearchContents(String hash, int user_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestSearchContents(hash, user_no);
    }

    public void requestDownloadComment(int content_no, OnMyApiListener listener){
        new ContentManager(context, listener).requestDownloadComment(content_no);
    }

    public void requestAddComment(int user_no, String host_no, int content_no, String uc_comment_name, OnMyApiListener listener){
        new ContentManager(context, listener).requestAddComment(user_no, host_no, content_no, uc_comment_name);
    }
//    public void requestAddComment(int User_no, int content_no, )

    //** To FriendManager

    public void requestFriendsList(OnMyApiListener listener){
        new FriendManager(context, listener).requestFriendsList();
    }

    public void requestSearchUser(int user_no, String search_name ,OnMyApiListener listener){
        new FriendManager(context, listener).requestSearchUser(user_no,search_name);
    }

    public void requestCountLikeIncrease(int user_no, ContentInfo contentInfo, OnMyApiListener listener){
        new ContentManager(context, listener).requestCountLikeIncrease(user_no, contentInfo);
    }

    public void requestCountCommentIncrease(int user_no, ContentInfo contentInfo, OnMyApiListener listener){
        new ContentManager(context, listener).requestCountCommentIncrease(user_no, contentInfo);
    }

    public void requestCountSmallHashIncrease(int user_no, String bighash, int hash, OnMyApiListener listener){
        new ContentManager(context, listener).requestCountSmallHashIncrease(user_no, bighash, hash);
    }

    public void requestCountBigHashIncrease(int user_no, int bighash_no, OnMyApiListener listener){
        Log.d(TAG, "빅해쉬카운트테스트 in interaction Manager " + bighash_no);
        new ContentManager(context, listener).requestCountBigHashIncrease(user_no, bighash_no);
    }

    public void requestCountSearchedHashIncrease(int user_no, String hash, OnMyApiListener listener){
        new ContentManager(context, listener).requestCountSearchedHashIncrease(user_no, hash);
    }

    public void requestPrefetchingList(int user_no, int current_page, int totalContentCount, OnMyApiListener listener){
        new ContentManager(context, listener).requestPrefetchingList(user_no, current_page, totalContentCount);
    }

    public void requestTotalCount(OnMyApiListener listener){
        new ContentManager(context, listener).requestTotalCount();
    }
}
