package com.example.mk.mysmartsns.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    @Bind(R.id.logo_imageView_register)
    ImageView logo_imageView_register;

    InterestsAdapter interestsAdapter;
    ArrayList<Integer> bigHashArrayList;
    RegisterInfo registerInfo;
    String imagePath;
    boolean isImageUpload;
    ArrayAdapter<String> adapter;
    Bitmap copyBitmap;


    List<BigHashInfo> bigHashList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Butterknife bind
        ButterKnife.bind(this);
        logo_imageView_register.setImageBitmap(deleteBackgroundImage());
        bigHashArrayList = new ArrayList<>();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_in_register.setLayoutManager(linearLayoutManager);

        // registerInfo 초기화
        registerInfo = new RegisterInfo();
        ArrayList<Integer> bigList = new ArrayList<>();
        // ** Download BigHashInfo
        InteractionManager.getInstance(this).requestContentBighashDownload(new OnMyApiListener() {
            @Override
            public void success(Object response) {
                bigHashList = (List<BigHashInfo>)response;
                for(int i=0; i<bigHashList.size(); i++){
                    Log.d(TAG, "listbighash test : " + bigHashList.get(i).getBighash_no() + bigHashList.get(i).getBighash_name() + bigHashList.get(i).getBighash_count());
                }
               if(bigHashList != null) {

                   interestsAdapter = new InterestsAdapter(getApplicationContext(), bigHashList);
                   recycler_view_in_register.setAdapter(interestsAdapter);

               }
            }
            @Override
            public void fail() {

            }
        });
    }

    @OnClick({R.id.sign_up_button, R.id.cancel_button,
            R.id.profile_picture_in_register, R.id.cancel_image_in_register})
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
                if(bigHashList != null){
                    for(int i=0; i < bigHashList.size(); i++){
                        // 우선 click이 3개인걸 세야됨
                        if(bigHashList.get(i).isCheck() == true){
                            bigHashArrayList.add(bigHashList.get(i).getBighash_no());
                            count = count + 1;
                        }
                    }
                }

                String str = "";
                if(id.length()==0){
                    str = str + "이름 ";
                }
                if(pw.length()==0){
                    str = str + "비밀번호 ";
                }
                if(name.length()==0){
                    str = str + "이름 ";
                }
                if(count == 3 && id.length()>0 && pw.length()>0 && name.length()>0){

                    // ** User Register
                    Toast.makeText(this, "정상적으로 3개 들어옴", Toast.LENGTH_SHORT).show();

                    int user_interest_bighash1, user_interest_bighash2, user_interest_bighash3= 0;
//                    Gson gson = new Gson();
//                    String jsonBigHash = gson.toJson(bigHashArrayList);                             // bighash 정보를 json형태로 바꾼다.
                    user_interest_bighash1 = bigHashArrayList.get(0);
                    user_interest_bighash2 = bigHashArrayList.get(1);
                    user_interest_bighash3 = bigHashArrayList.get(2);
                    int age = 20;
                    InteractionManager.getInstance(this).requestUserRegister(id, pw, name, gender, user_interest_bighash1, user_interest_bighash2, user_interest_bighash3, user_profile_url, new OnMyApiListener() {
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
                    Toast.makeText(this, str + "를 설정해주세요", Toast.LENGTH_SHORT).show();
                    bigHashArrayList.clear();
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

    public Bitmap deleteBackgroundImage(){
        Drawable d = logo_imageView_register.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);

        // get image size
        int width = copyBitmap.getWidth();
        int height = copyBitmap.getHeight();
        // create output bitmap
        // color information
        int A, R, G, B;
        int pixel;
        copyBitmap.setHasAlpha(true);
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = copyBitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // 흰색이면 (지금 여기선 흰색이 이 값이다)
                if((R <= 256 && R >=220) && (G <= 256 && G >=220) && (B <= 256 && B >=220)){
                    copyBitmap.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        return copyBitmap;
    }
}
