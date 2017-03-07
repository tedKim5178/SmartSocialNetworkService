package com.example.mk.mysmartsns.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.InterestsAdapter;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.model.Interest_item;
import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.RegisterInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mk on 2017-02-02.
 */

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = LoginActivity.class.getSimpleName();
    @Bind(R.id.register_id)
    EditText register_id_editText;
    @Bind(R.id.register_password)
    EditText register_password_editText;
    @Bind(R.id.register_name)
    EditText register_name_editText;
    @Bind(R.id.sign_up_button)
    Button sign_up_button;
    @Bind(R.id.cancel_button)
    Button cancel_button;
    @Bind(R.id.recycler_view_in_register)
    RecyclerView recycler_view_in_register;
    @Bind(R.id.profile_picture_in_register)
    ImageView profile_picture_in_register;
    @Bind(R.id.checkbox_for_man)
    CheckBox checkbox_for_man;
    @Bind(R.id.checkbox_for_woman)
    CheckBox checkbox_for_woman;

    InterestsAdapter interestsAdapter;

    ArrayList<Interest_item> itemlist;
    ArrayList<String> bigHashList;

    RegisterInfo registerInfo;

    String imagePath;
    boolean isImageUpload;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Butterknife bind
        ButterKnife.bind(this);
        itemlist = new ArrayList<Interest_item>();
        bigHashList = new ArrayList<>();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_in_register.setLayoutManager(linearLayoutManager);

        // registerInfo 초기화
        registerInfo = new RegisterInfo();

        // onCreate를 하면서 bighash 정들을 받아와야한다. 그리고 그렇게 받아온 bighas 정보들은
        // view들에 업데이트 시켜줄 수 있다..!
//        Retrofit client = new Retrofit.Builder().baseUrl("http://172.16.21.148:3001").addConverterFactory(GsonConverterFactory.create()).build();
//        SNSService snsService = client.create(SNSService.class);
//        Call<List<BigHashInfo>> call = snsService.requestBighash();
//        call.enqueue(new Callback<List<BigHashInfo>>() {
//            @Override
//            public void onResponse(Call<List<BigHashInfo>> cll, Response<List<BigHashInfo>> response) {
//                if(response.isSuccessful()){
//                    Log.d(TAG, "[레트로핏테스트1] success");
//                    // response 부분 어떻게 받아와야 되는지 잘 모르겠다.
//                    // 이렇게 받는게 맞나,..?
//                    List<BigHashInfo> hashInfo = response.body();
//
//                    for(int i=0; i< hashInfo.size(); i++){
//                        arraylist.add(i, hashInfo.get(i).getBighash_name());
//                    }
//
//                    // 이런식으로 HashInfo에서 데이터를 받아와서 넣어준다.
////                    arraylist.add("data0");
////                    arraylist.add("data1");
////                    arraylist.add("data2");
////                    arraylist.add("data3");
//
//                }else{
//                    Log.d(TAG, "[레트로핏테스트1] Fail");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<BigHashInfo>> call, Throwable t) {
//                Log.d(TAG, "[레트로핏테스트1] onFail" + t.getMessage());
//            }
//        });

        // ** Download BigHashInfo
        InteractionManager.getInstance(this).requestContentBighashDownload(new OnMyApiListener() {
            @Override
            public void success(Object response) {
                List<BigHashInfo> bigHashInfo = (List<BigHashInfo>)response;
               if(bigHashInfo != null) {
                   for (int i = 0; i < bigHashInfo.size(); i++) {
                       Interest_item interest_item= new Interest_item(bigHashInfo.get(i).getBighash_name(), false);
                       itemlist.add(i, interest_item);
                   }
                   interestsAdapter = new InterestsAdapter(getApplicationContext(), itemlist);
                   recycler_view_in_register.setAdapter(interestsAdapter);

               }
            }
            @Override
            public void fail() {

            }
        });
    }

    @OnClick({R.id.sign_up_button, R.id.cancel_button, R.id.profile_picture_in_register, R.id.cancel_image_in_register,
        R.id.checkbox_for_man, R.id.checkbox_for_woman})
    void buttonClicked(View view) {
        int clicked = view.getId();
        switch (clicked){
            case R.id.sign_up_button:
            {
                recycler_view_in_register.setVisibility(View.VISIBLE);
                // 여기서 서버로 정보들 보낸다. (데이터베이스에 그 정보들 저장하자)
                String id = register_id_editText.getText().toString();
                String pw = register_password_editText.getText().toString();
                String name = register_name_editText.getText().toString();
                String gender = "1";
                String user_profile_url = imagePath;
                // 여기서 이제 hash들 가져와야한다..!
                // hash들은 지금 어디에있냐면.... 바로 adapter에 있다...!
                // 여기서 arraylist 접근해볼까..?
                int count = 0;
                for(int i=0; i < itemlist.size(); i++){
                    // 우선 click이 3개인걸 세야됨
                    if(itemlist.get(i).isClicked() == true){
                        bigHashList.add(itemlist.get(i).getInterest());
                        count = count + 1;
                    }
                }
                if(count == 3){
                    // ** User Register
                    Toast.makeText(this, "정상적으로 3개 들어옴", Toast.LENGTH_SHORT).show();
                    String user_interest_bighash1 = bigHashList.get(0).toString();
                    String user_interest_bighash2 = bigHashList.get(1).toString();
                    String user_interest_bighash3 = bigHashList.get(2).toString();
                    InteractionManager.getInstance(this).requestUserRegister(id, pw, name, gender,user_profile_url, user_interest_bighash1,
                            user_interest_bighash2, user_interest_bighash3, new OnMyApiListener() {
                        @Override
                        public void success(Object response) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void fail() {

                        }
                    });
                }else{
                    Toast.makeText(this, "3개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                    bigHashList.clear();
                }

                break;
            }
            case R.id.cancel_button:
            {
                Toast.makeText(this, "정상작동2", Toast.LENGTH_SHORT).show();
                // main activity로 가자...!
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Log.d(TAG, "여기나오나..?");
                finish();
                break;
            }
            case R.id.profile_picture_in_register:
            {
                if (!isImageUpload) {         // 이미지가 안올라가 있을 때
                    Toast.makeText(this, "이미지뷰 클릭", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 998);
                    break;
                }
            }
            case R.id.cancel_image_in_register:
            {
                isImageUpload = false;
                profile_picture_in_register.setImageResource(android.R.drawable.ic_menu_camera);
                break;
            }
            case R.id.checkbox_for_man:
            {

                if(checkbox_for_man.isChecked() == true){
                    checkbox_for_woman.setChecked(false);
                }else{
                    checkbox_for_man.setChecked(true);
                }

                break;
            }
            case R.id.checkbox_for_woman:
            {

                if(checkbox_for_woman.isChecked() == true){
                    checkbox_for_man.setChecked(false);
                }else{
                    checkbox_for_woman.setChecked(true);
                }

                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "이거퍼즈들어오나");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (requestCode == 998) {
                Uri imageUri = data.getData();
                Log.d(TAG, "image url from album " + imageUri);

                // uri로 filepath가져오기
                Cursor cursor = this.getBaseContext().getContentResolver().query(imageUri, null, null, null, null);
                cursor.moveToNext();
                String path = cursor.getString(cursor.getColumnIndex("_data"));
                cursor.close();
                Log.d(TAG, "image path from album " + path);
                imagePath = path;
                Glide.with(this.getBaseContext()).load(imagePath).fitCenter().into(profile_picture_in_register);
                isImageUpload = true;
            }
        }
    }

}
