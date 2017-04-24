package com.example.mk.mysmartsns.network;

import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.CommentInfo;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.info.LoginInfo;
import com.example.mk.mysmartsns.network.info.PrefetchImageInfo;
import com.example.mk.mysmartsns.network.info.RegisterInfo;
import com.example.mk.mysmartsns.network.info.SmallHashInfo;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mk on 2017-02-02.
 */

public interface SNSService {
//    @GET("{path}/2.5/weather/")
//    Call<WeatherInfo> getWeatherInfo(@Path("path") String data, @Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String appId);

    // 맨처음 register 들어가면 서버가 bighash정보 넘겨줄꺼임
    @GET("/download/bighash")
    Call<List<BigHashInfo>> requestBighash();

    // download the small hash
    @GET("/download/smallhash/{smallhash}")
    Call<List<SmallHashInfo>> requestSmallhash(@Path("smallhash") String smallhash);


    // register 할때 post 형식으로 서버에게 register 정보들 넘겨줌
    @POST("/intro/regist")
    Call<Void> registerUser(@Body RegisterInfo registerInfo);

    // login 할때 아이디 비밀번호 서버에게 넘겨줌
    @POST("/intro/login")
    Call<UserInfo> loginUser(@Body LoginInfo loginInfo);

    // login 해서 들어가면 get방식으로 이미지랑, 정보들 받아와야됨
    @GET("/download/thumbnail")
    Call<List<ContentInfo>> requestThumbnailContents(@Query("user_no") int user_no);

    // original image !!
    // user_no, bighash, smallhash 넘겨줘야됨... 즉 그냥 컨텐츠 정보를 넘겨주자
    @GET("/download/original/{thumb_content_url}")
    Call<ContentInfo> requestOriginContent(@Path("thumb_content_url") String thumb_content_url,@Query("big_hash_info") String bigHashInfo, @Query("small_hash_info") String smallHashInfo, @Query("user_no") int user_no);         // original image 받아오기

//
//    // original image !!
//    // user_no, bighash, smallhash 넘겨줘야됨... 즉 그냥 컨텐츠 정보를 넘겨주자
//    @GET("/download/original/{thumb_content_url}")
//    Call<ContentInfo> requestOriginContent(@Path("thumb_content_url") String thumb_content_url);         // original image 받아오기


    // contents upload
    @Multipart
    @POST("/upload/single")
    Call<ResponseBody> uploadContents(@Part("description") RequestBody description, @Part("host") RequestBody host
            , @Part("bighash") RequestBody bighash, @Part("smallhash") RequestBody smallhash, @Part MultipartBody.Part file);

    // 추가
    // 좋아요 눌렀을때
    @GET("/others/like")
    Call<ContentInfo> requestLikeClicked(@Query("user_no") int user_no, @Query("content_no") int content_no);

    @GET("/others/unlike")
    Call<ContentInfo> requestUnLikeClicked(@Query("user_no") int user_no, @Query("content_no") int content_no);

    // 좋아요 갯수 눌렀을때 서버로 content_no 보내주면 userInfo들 받아오는거
    @GET("/download/like_user/{content_no}")
    Call<List<UserInfo>> requestPeopleWhoLikeThisPost(@Path("content_no") int content_no);

    @GET("/friends")
    Call<List<UserInfo>> requestFriendsList();

    @GET("/download/user_thumbnail")
    Call<List<ContentInfo>> requestUserThumbnailContents(@Query("user_no") int user_no, @Query("host_no") int host_no);

    // search user
    @GET("/download/search_user")
    Call<List<UserInfo>> requestSearchUser(@Query("user_no") int user_no, @Query("search_name") String search_name);

    // hash에 해당하는 thumbnail image 받아오기
    @GET("/download/thumbnail/search/{hash_name}")
    Call<List<ContentInfo>> requestSearchContents(@Path("hash_name") String hash_name, @Query("user_no") int user_no);

    // comment activity 들어가면 comment들 받아오기
    @GET("/others/download_comment")
    Call<List<CommentInfo>> requestDownloadComment(@Query("content_no") int content_no);

    @GET("/others/add_comment")
    Call<List<CommentInfo>> requestAddComment(@Query("user_no") int user_no,@Query("host_no") String host_no, @Query("content_no") int content_no, @Query("uc_comment_name") String uc_comment_name);

    // count increase
    @GET("/count/like")
    Call<Void> countLikeIncrease(@Query("user_no") int user_no, @Query("content_info") String contentInfo);

    @GET("/count/comment")
    Call<Void> countCommentIncrease(@Query("user_no") int user_no, @Query("content_info") String contentInfo);

    @GET("/count/smallhash_surf")
    Call<Void> countSmallHashIncrease(@Query("user_no") int user_no, @Query("bighash_list") String bighash_list, @Query("smallhash") int smallhash);

    @GET("/count/bighash_surf")
    Call<Void> countBigHashIncrease(@Query("user_no") int user_no, @Query("bighash") int bighash);

    @GET("/count/search")
    Call<Void> countSearchedHashIncrease(@Query("user_no") int user_no, @Query("searched_hash") String hash);

    @GET("/prefetch/list/{user_no}")
    Call<List<PrefetchImageInfo>> requestPrefetchingList(@Path("user_no") int user_no);
}
