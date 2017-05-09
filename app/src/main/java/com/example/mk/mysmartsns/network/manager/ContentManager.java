package com.example.mk.mysmartsns.network.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.CallManagement;
import com.example.mk.mysmartsns.network.SNSService;
import com.example.mk.mysmartsns.network.ServerController;
import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.CommentInfo;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.info.PrefetchImageInfo;
import com.example.mk.mysmartsns.network.info.SmallHashInfo;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.google.gson.Gson;

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

    public void requestAddComment(int user_no, String host_no, int content_no, String uc_comment_name){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestAddComment", true);

        Call<List<CommentInfo>> callRequestAddComment = snsService.requestAddComment(user_no, host_no, content_no, uc_comment_name);
        callRequestAddComment.enqueue(new Callback<List<CommentInfo>>() {
            @Override
            public void onResponse(Call<List<CommentInfo>> call, Response<List<CommentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestAddComment::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestAddComment::isNotSuccessful() : " + response.code());
                }
                callManagement.subtractCall("requestAddComment", false);
            }

            @Override
            public void onFailure(Call<List<CommentInfo>> call, Throwable t) {
                Log.d(TAG, "requestAddComment::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestAddComment", false);
            }
        });
    }

    public void requestDownloadComment(int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestDownloadComment", true);
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
                callManagement.subtractCall("requestDownloadComment", false);
            }

            @Override
            public void onFailure(Call<List<CommentInfo>> call, Throwable t) {
                Log.d(TAG, "requestDownloadComment::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestDownloadComment", false);
            }
        });
    }

    public void requestUserThumbnailContents(int user_no, int host_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestUserThumbnailContents", true);
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
                callManagement.subtractCall("requestUserThumbnailContents", false);
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestUserThumbnailContents::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestUserThumbnailContents", false);
            }
        });
    }

    // dwonload thumbnail contents
    public void requestContentThumbnailDownload(int user_no, int current_page){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestContentThumbnailDownload", true);

        Call<List<ContentInfo>> callRequestThumbnailContents = snsService.requestThumbnailContents(user_no, current_page);
        callRequestThumbnailContents.enqueue(new Callback<List<ContentInfo>>() {
            @Override
            public void onResponse(Call<List<ContentInfo>> call, Response<List<ContentInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentThumbnailDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentThumbnailDownload::isNotSuccessful() : " + response.code());
                }
                callManagement.subtractCall("requestContentThumbnailDownload", false);
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentThumbnailDownload::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestContentThumbnailDownload", false);
            }
        });
    }

    // download the original image
    public void requestContentOriginalDownload(String thumnbail_url, String bigHashInfo, String smallHashInfo, int user_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestContentOriginalDownload", true);

        Call<ContentInfo> callRequestOriginContent = snsService.requestOriginContent(thumnbail_url, bigHashInfo, smallHashInfo, user_no);
        callRequestOriginContent.enqueue(new Callback<ContentInfo>() {
            @Override
            public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestContentOriginalDownload::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "requestContentOriginalDownload::isNotSuccessful() : " + response.code());
                }
                callManagement.subtractCall("requestContentOriginalDownload", false);
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestContentOriginalDownload::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestContentOriginalDownload", false);
            }
        });
    }


    public void requestLikeClicked(int user_no, int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestLikeClicked", true);

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
                callManagement.subtractCall("requestLikeClicked", false);
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestLikeClicked::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestLikeClicked", false);
            }
        });
    }

    public void requestUnLikeClicked(int user_no, int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestUnLikeClicked", true);

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
                callManagement.subtractCall("requestUnLikeClicked", false);
            }

            @Override
            public void onFailure(Call<ContentInfo> call, Throwable t) {
                Log.d(TAG, "requestUnLikeClicked::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestUnLikeClicked", false);
            }
        });
    }
    public void requestPeopleWhoLikeThisPost(int content_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestPeopleWhoLikeThisPost", true);

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
                callManagement.subtractCall("requestPeopleWhoLikeThisPost", false);
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d(TAG, "requestPeopleWhoLikeThisPost::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestPeopleWhoLikeThisPost", false);
            }
        });
    }
    // download bighash
    public void requestContentBighashDownload(){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestContentBighashDownload", true);

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
                callManagement.subtractCall("requestContentBighashDownload", false);

            }

            @Override
            public void onFailure(Call<List<BigHashInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentBighashDownload::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestContentBighashDownload", false);
            }
        });
    }

    /// small hash download
    public void requestContentSmallhashDownload(String smallhash){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestContentSmallhashDownload", true);

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
                callManagement.subtractCall("requestContentSmallhashDownload", false);

            }

            @Override
            public void onFailure(Call<List<SmallHashInfo>> call, Throwable t) {
                Log.d(TAG, "requestContentSmallhashDownload::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestContentSmallhashDownload", false);
            }
        });

    }

    public void requestSearchContents(String hash, int user_no){
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestSearchContents", true);

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
                callManagement.subtractCall("requestSearchContents", false);
            }

            @Override
            public void onFailure(Call<List<ContentInfo>> call, Throwable t) {
                Log.d(TAG, "requestSearchContents::onFailure() : " + t.getMessage());
                callManagement.subtractCall("requestSearchContents", false);
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

        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestContentUpload", true);

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
                callManagement.subtractCall("requestContentUpload", false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "requestContentUpload::onFailure() : " + t.getMessage());
                t.printStackTrace();
                callManagement.subtractCall("requestContentUpload", false);
            }
        });

    }

    public void requestCountLikeIncrease(int user_no, ContentInfo contentInfo){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestCountLikeIncrease", true);
        Gson gson = new Gson();
        String contentInfoJson = gson.toJson(contentInfo);
        Call<Void> call = snsService.countLikeIncrease(user_no, contentInfoJson);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "좋아요카운트테스트 isSuccessful");
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "좋아요카운트테스트 isfail");
                    listener.fail();
                }
                callManagement.subtractCall("requestCountLikeIncrease", false);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "좋아요카운트테스트 onFailure");
                callManagement.subtractCall("requestCountLikeIncrease", false);
            }
        });
    }

    public void requestCountCommentIncrease(int user_no, ContentInfo contentInfo){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestCountCommentIncrease", true);
        Gson gson = new Gson();
        String contentInfoJson = gson.toJson(contentInfo);
        Call<Void> call = snsService.countCommentIncrease(user_no, contentInfoJson);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "커멘트isSuccessful");
                }else{
                    Log.d(TAG, "커멘트isFail");
                }

                callManagement.subtractCall("requestCountCommentIncrease", false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "커멘트onFailure");
                callManagement.subtractCall("requestCountCommentIncrease", false);
            }
        });
    }

    public void requestCountSmallHashIncrease(int user_no, String bighash, int hash){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestCountSmallHashIncrease", true);

        Call<Void> call = snsService.countSmallHashIncrease(user_no, bighash, hash);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "스몰해쉬카운트테스트 isSuccessful");
                }else{
                    Log.d(TAG, "스몰해쉬카운트테스트 isFail");
                }

                callManagement.subtractCall("requestCountSmallHashIncrease", false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "커멘트onFailure");

                callManagement.subtractCall("requestCountSmallHashIncrease", false);
            }
        });
    }

    public void requestCountBigHashIncrease(int user_no, int bighash_no){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestCountBigHashIncrease", true);

        Log.d(TAG, "빅해쉬카운트테스트 : " + user_no + " , " + bighash_no);
        Call<Void> call = snsService.countBigHashIncrease(user_no, bighash_no);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "빅해쉬카운트테스트 isSuccessful");
                }else{
                    Log.d(TAG, "빅해쉬카운트테스트 isFail");
                }

                callManagement.subtractCall("requestCountBigHashIncrease", false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "커멘트onFailure");

                callManagement.subtractCall("requestCountBigHashIncrease", false);
            }
        });
    }

    public void requestCountSearchedHashIncrease(int user_no, String hash){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestCountSearchedHashIncrease", true);

        Call<Void> call = snsService.countSearchedHashIncrease(user_no, hash);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "서치해쉬카운트테스트 isSuccessful");
                }else{
                    Log.d(TAG, "서치해쉬카운트테스트 isFail");
                }

                callManagement.subtractCall("requestCountSearchedHashIncrease", false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "커멘트onFailure");

                callManagement.subtractCall("requestCountSearchedHashIncrease", false);
            }
        });
    }

    public void requestPrefetchingList(int user_no, int current_page){
        SNSService snsService = ServerController.getInstance().getSnsService();
        final CallManagement callManagement = CallManagement.getInstance();
        callManagement.addCall("requestPrefetchingList", true);

        Call<List<PrefetchImageInfo>> call = snsService.requestPrefetchingList(user_no, current_page);
        call.enqueue(new Callback<List<PrefetchImageInfo>>() {
            @Override
            public void onResponse(Call<List<PrefetchImageInfo>> call, Response<List<PrefetchImageInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "프리페칭테스트 isSuccessful");
                    listener.success(response.body());
                }else{
                    Log.d(TAG, "프리페칭테스트 isFail");
                    Log.d(TAG, response.message());
                }
                callManagement.subtractCall("requestPrefetchingList", false);
            }

            @Override
            public void onFailure(Call<List<PrefetchImageInfo>> call, Throwable t) {
                callManagement.subtractCall("requestPrefetchingList", false);
            }
        });
    }

}
