package com.example.mk.mysmartsns.network.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.SNSService;
import com.example.mk.mysmartsns.network.ServerController;
import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.CommentInfo;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.info.SmallHashInfo;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class ContentManager {

    private static String TAG = ContentManager.class.getSimpleName();
    private OnMyApiListener listener;
    private Context context;

    public ContentManager(Context context, OnMyApiListener listener){
        this.context = context;
        this.listener = listener;
    }

    public void requestAddComment(int user_no, int content_no, String uc_comment_name){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<CommentInfo>> callRequestAddComment = snsService.requestAddComment(user_no, content_no, uc_comment_name);
        callRequestAddComment.enqueue(new Callback<List<CommentInfo>>() {
            @Override
            public void onResponse(Call<List<CommentInfo>> call, Response<List<CommentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestAddComment::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestAddComment::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CommentInfo>> call, Throwable t) {
                Log.d(TAG, "requestAddComment::onFailure() : " + t.getMessage());

            }
        });
    }

    public void requestDownloadComment(int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<CommentInfo>> callRequestDownloadComment = snsService.requestDownloadComment(content_no);
        callRequestDownloadComment.enqueue(new Callback<List<CommentInfo>>() {
            @Override
            public void onResponse(Call<List<CommentInfo>> call, Response<List<CommentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestDownloadComment::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestDownloadComment::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CommentInfo>> call, Throwable t) {
                Log.d(TAG, "requestDownloadComment::onFailure() : " + t.getMessage());
            }
        });
    }

    public void requestUserThumbnailContents(int user_no, int host_no){
        // 레트로핏 추가..!
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<ContentInfo>> callRequestThumbnailContents = snsService.requestUserThumbnailContents(user_no, host_no);
        callRequestThumbnailContents.enqueue(new Callback<List<ContentInfo>>() {
            @Override
            public void onResponse(Call<List<ContentInfo>> call, Response<List<ContentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestUserThumbnailContents::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestUserThumbnailContents::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestUserThumbnailContents::onFailure() : " + t.getMessage());
            }
        });
    }

    // dwonload thumbnail contents
    public void requestContentThumbnailDownload(int user_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<ContentInfo>> callRequestThumbnailContents = snsService.requestThumbnailContents(user_no);
        callRequestThumbnailContents.enqueue(new Callback<List<ContentInfo>>() {
            @Override
            public void onResponse(Call<List<ContentInfo>> call, Response<List<ContentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentThumbnailDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentThumbnailDownload::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentThumbnailDownload::onFailure() : " + t.getMessage());
            }
        });
    }

    // download the original image
    public void requestContentOriginalDownload(String thumnbail_url){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<ContentInfo> callRequestOriginContent = snsService.requestOriginContent(thumnbail_url);
        callRequestOriginContent.enqueue(new Callback<ContentInfo>() {
            @Override
            public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentOriginalDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentOriginalDownload::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestContentOriginalDownload::onFailure() : " + t.getMessage());
            }
        });
    }


    public void requestLikeClicked(int user_no, int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        Call<ContentInfo> likeClicked = snsService.requestLikeClicked(user_no, content_no);
        likeClicked.enqueue(new Callback<ContentInfo>() {
            @Override
            public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestLikeClicked::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestLikeClicked::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestLikeClicked::onFailure() : " + t.getMessage());
            }
        });
    }

    public void requestUnLikeClicked(int user_no, int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        Call<ContentInfo> likeClicked = snsService.requestUnLikeClicked(user_no, content_no);
        likeClicked.enqueue(new Callback<ContentInfo>() {
            @Override
            public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestUnLikeClicked::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestUnLikeClicked::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestUnLikeClicked::onFailure() : " + t.getMessage());
            }
        });
    }
    public void requestPeopleWhoLikeThisPost(int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        Call<List<UserInfo>> likeClicked = snsService.requestPeopleWhoLikeThisPost(content_no);
        likeClicked.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestPeopleWhoLikeThisPost::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestPeopleWhoLikeThisPost::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d(TAG, "requestPeopleWhoLikeThisPost::onFailure() : " + t.getMessage());
            }
        });
    }
    // download bighash
    public void requestContentBighashDownload(){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<BigHashInfo>> callRequestBighash = snsService.requestBighash();
        callRequestBighash.enqueue(new Callback<List<BigHashInfo>>() {
            @Override
            public void onResponse(Call<List<BigHashInfo>> call, Response<List<BigHashInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentBighashDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentBighashDownload::isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BigHashInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentBighashDownload::onFailure() : " + t.getMessage());
            }
        });
    }

    /// small hash download
    public void requestContentSmallhashDownload(String smallhash){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<SmallHashInfo>> callRequestSmallhash = snsService.requestSmallhash(smallhash);
        callRequestSmallhash.enqueue(new Callback<List<SmallHashInfo>>() {
            @Override
            public void onResponse(Call<List<SmallHashInfo>> call, Response<List<SmallHashInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentSmallhashDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentSmallhashDownload::isNotSuccessful() : " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<SmallHashInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentSmallhashDownload::onFailure() : " + t.getMessage());
            }
        });

    }

    public void requestSearchContents(String hash, int user_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        Call<List<ContentInfo>> callRequestSearchContents = snsService.requestSearchContents(hash, user_no);
        callRequestSearchContents.enqueue(new Callback<List<ContentInfo>>() {
            @Override
            public void onResponse(Call<List<ContentInfo>> call, Response<List<ContentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestSearchContents::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestSearchContents::isNotSuccessful() : " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestSearchContents::onFailure() : " + t.getMessage());
            }
        });
    }

    // upload contents
    public void requestContentUpload(String path, String description, String host, String bighash, String smallhash){
        SNSService snsService = ServerController.getInstance().getSnsService();
        File file = new File(path);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RequestBody descriptionBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), description);
        RequestBody hostBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), host);
        RequestBody bigHashBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), bighash);
        RequestBody smallHashBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), smallhash);

        Call<ResponseBody> call = snsService.uploadContents(descriptionBody, hostBody, bigHashBody, smallHashBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentUpload::isSuccessful() : " + response.code());
                    listener.success(response.body());

                }else{
                    Log.d(TAG, "requestContentUpload::isNotSuccessful() : " + response.code());
                    Toast.makeText(context, "upload failure", Toast.LENGTH_SHORT).show();
                    listener.fail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "requestContentUpload::onFailure() : " + t.getMessage());
                t.printStackTrace();
            }
        });

    }
}
