package com.example.mk.mysmartsns.database;

import android.content.Context;
import android.util.Log;

import com.example.mk.mysmartsns.activity.LoginActivity;
import com.example.mk.mysmartsns.network.SNSService;
import com.example.mk.mysmartsns.network.info.LoginInfo;
import com.example.mk.mysmartsns.network.info.RegisterInfo;
import com.example.mk.mysmartsns.network.info.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mk on 2017-02-05.
 */

public class DatabaseManager {
    private static final String TAG = LoginActivity.class.getSimpleName();
    static int regi;
    // 각종 데이터베이스에 관련된 함수들...!
    static Context contextt;
    public static void setContext(Context context){
        contextt = context;
    }
    // 유저가 로그인 했다고 신호 보내주기..!
    // 결국 그러면 이 안에서 retrofit을 사용하려나..?
    public static void checkLoginToDB(String name, String password){
        // name과 password를 이용해서 서버와 데이터 전송..! 그리고 checkLogin 을 사용..!

        // 만약에 정상적으로 로그인이 됬으면...!
        // 이제 user_no랑 id랑 name이랑 profile_url을 받았다고 치자..!
//        String user_id = "rlaansrl123";
//        int user_no = 1;
//        String profile_url = "http://rlaansrl";
//        String user_name = "rlaansrl";
//
//        UserInfo.id = user_id;
//        UserInfo.user_no = user_no;
//        UserInfo.profileURL = profile_url;
//        UserInfo.name = user_name;

        LoginInfo loginInfo = new LoginInfo(name, password);
        Log.d(TAG, "[레프토핏테스트] : " + loginInfo.getUser_id());
        Log.d(TAG, "[레프토핏테스트] : " + loginInfo.getUser_password());

        // 정상적으로 안되는 경우도 있으니까  if문을 사용해서 구분해줘야 할 것이다..!
        Retrofit client = new Retrofit.Builder().baseUrl("http://172.16.21.148:3001").addConverterFactory(GsonConverterFactory.create()).build();
        SNSService snsService = client.create(SNSService.class);
        Call<UserInfo> call = snsService.loginUser(loginInfo);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> cll, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "[레트로핏테스트(login)] SUCCESS");
                }else{
                    Log.d(TAG, "[레트로핏테스트(login)] FAIL");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d(TAG, "[레트로핏테스트] onFail(login)");
            }
        });

    }
    //
    public static int saveUserToDB(RegisterInfo registerInfo){

        // retrofit2 post 방식 이용해서 정보들 이제 서버에 보내주고 받는 부분도 여기에 존재한다.
        Retrofit client = new Retrofit.Builder().baseUrl("http://172.16.21.148:3001").addConverterFactory(GsonConverterFactory.create()).build();
        SNSService snsService = client.create(SNSService.class);
//        Call<ResponseBody> call = snsService.registerUser(registerInfo);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> cll, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//                    int registeCheck = response.code();
//                    Log.d(TAG, "[레트로핏테스트] registeCheck : " + registeCheck);
//                    if(registeCheck == 200){
//                        // 성공
//                        Toast.makeText(contextt, "회원가입이 정상적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "[레트로핏테스트] onFail");
//            }
//        });

        Log.d(TAG, "레트로핏 register변수 테스트 ; " + regi);
        return regi;
    }
    public static String[] getContentsFromDB(){
        // 아마 URL들이 넘어와서 URL 배열이 리턴값으로 바뀌어야 할 것이다.
        return new String[0];
    }

    // 친구목록 가지고 오는 함수
    public static String[] getAllFriends(){
        // 안에서 서버로부터 데이터를 가져와서 친구 목록을 가져와야됨.
        return new String[0];
    }
}
