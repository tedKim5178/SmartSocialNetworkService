package com.example.mk.mysmartsns.network.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.CallManagement;
import com.example.mk.mysmartsns.network.SNSService;
import com.example.mk.mysmartsns.network.ServerController;
import com.example.mk.mysmartsns.network.info.LoginInfo;
import com.example.mk.mysmartsns.network.info.UserInfo;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mk on 2017-02-05.
 */

public class UserManager {

    private static String TAG = UserManager.class.getSimpleName();
    private OnMyApiListener listener;
    private Context context;

    public UserManager(Context context, OnMyApiListener listener){
        this.context = context;
        this.listener = listener;
    }

    //    // register를 담당하는 함수
//    public static int registerRequest(RegisterInfo registerInfo){
//        // User라는 객체를 넘겨줄까 아니면 String 형식으로 넘겨주는게 깨끗할까? 고민해보자..!
//        int registeCheck = 0;
//        registeCheck = DatabaseManager.saveUserToDB(registerInfo);
//
//        return registeCheck;
//    }
//
//    // login 을 담당하는 함수
//    public static void checkLoginRequest(String name, String password){
//        DatabaseManager.checkLoginToDB(name, password);
//    }

    // Login
    public void requestUserLogin(String id, String pw) {
        SNSService snsService = ServerController.getInstance().getSnsService();

        final CallManagement callManagement = CallManagement.getInstance(context);
        callManagement.addCall("requestUserLogin", true);

        Call<UserInfo> callLoginUser = snsService.loginUser(new LoginInfo(id, pw));
        callLoginUser.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestUserLogin::isSuccessful() : " + response.code());
                    //ToDo. UserInfo 저장하는 코드 작성
//                    response.body();
                    listener.success(response.body());                 //** 설정했던 콜백함수 실행
                }else{
                    Log.d(TAG, "requestUserLogin::isNotSuccessful() : " + response.code());
                    if(response.code() == 401)
                        Toast.makeText(context, "아이디 혹은 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                }
                PrefetchConfig.isPrefetching = true;
                callManagement.subtractCall("requestUserLogin", false);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d(TAG, "requestUserLogin::onFailure() : " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(context, "네트워크 상황을 확인하여 주세요", Toast.LENGTH_SHORT).show();
                callManagement.subtractCall("requestUserLogin", false);
            }
        });
    }

    public void requestUserRegister(String id, String pw, String name, String gender, int user_interest_first, int user_interest_second,
                                    int user_interest_third, String user_profile_url) {
        SNSService snsService = ServerController.getInstance().getSnsService();
        File file = new File(user_profile_url);
        Log.d(TAG, "requestUserRegister");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);

        RequestBody idBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), id);
        RequestBody pwBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), pw);
        RequestBody nameBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);
        RequestBody genderBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), gender);

        RequestBody user_interest_firstBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(user_interest_first));
        RequestBody user_interest_secondBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(user_interest_second));

        RequestBody user_interest_thirdBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), String.valueOf(user_interest_third));

        Call<ResponseBody> call = snsService.registerUser(idBody, pwBody, nameBody, genderBody,
                user_interest_firstBody, user_interest_secondBody, user_interest_thirdBody, body);

        Log.d(TAG, "requestUserRegister " + user_profile_url + " , " + user_interest_first + " , " + name + " , " + gender);
        Log.d(TAG, "requestUserRegister" + call.request().toString() + " , " +call.toString());
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "requestUserRegister::isSuccessful() : " + response.code());
                    listener.success(response.body());
                }else {
                    Log.d(TAG, "requestUserRegister::isNotSuccessful() : " + response.code());
                    Toast.makeText(context, "upload failure", Toast.LENGTH_SHORT).show();
                    listener.fail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "requestUserRegister::isFailure() " + t.toString());
            }
        });
    }



    // register
//    public void requestUserRegister(String id, String pw, String name, int age, String gender, int user_interest_bighash1,int user_interest_bighash2,int user_interest_bighash3,String user_profile_url) {
//        SNSService snsService = ServerController.getInstance().getSnsService();
//
//        final CallManagement callManagement = CallManagement.getInstance();
//        Call<Void> callRegisterUser = snsService.registerUser(new RegisterInfo(id, pw, name, age, gender, user_interest_bighash1, user_interest_bighash2, user_interest_bighash3, user_profile_url));
//        callRegisterUser.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()){
//                    Log.d(TAG, "requestUserRegister::isSuccessful() : " + response.code());
//                    Toast.makeText(context, "회원가입이 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                    listener.success(response.body());
//                }else{
//                    Log.d(TAG, "requestUserRegister::isNotSuccessful() : " + response.code());
//                    Toast.makeText(context, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();       // 아이디 중복 확인 해야됨
//                }
//            }
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d(TAG, "requestUserRegister::onFailure() : " + t.getMessage());
//                Toast.makeText(context, "네트워크 상황을 확인하여 주세요", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}

