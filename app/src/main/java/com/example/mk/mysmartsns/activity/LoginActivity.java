package com.example.mk.mysmartsns.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Animation.AnimationListener{
    @Bind(R.id.input_id)
    EditText id_editText;
    @Bind(R.id.input_password)
    EditText password_editText;
    @Bind(R.id.login_button)
    Button login_button;
    @Bind(R.id.register_button)
    Button register_button;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.imageView2)
    ImageView imageView2;

    Bitmap copyBitmap;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Boolean ANIMATION_ENDED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Butterknife bind
        ButterKnife.bind(this);
        // 작동 안하는거 같다...
        id_editText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.editTextBottomLine), PorterDuff.Mode.SRC_ATOP);
        // 처음 시작때 뷰 숨기기
        hideViewsBeforeStarts();

        // TODO:: 애니메이션 setFillAfter 같은 함수들 용도 찾아보기
        Animation moveLogoAnitmation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        moveLogoAnitmation.setFillAfter(true);
        moveLogoAnitmation.setAnimationListener(this);
        imageView.startAnimation(moveLogoAnitmation);
        imageView2.setVisibility(View.GONE);

        final View activityRootView = findViewById(R.id.constraintLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(ANIMATION_ENDED){
                    Log.d(TAG, "LoginActivity layout height 차이점"+activityRootView.getRootView().getHeight()+", " + activityRootView.getHeight());
                    int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                    if(heightDiff > dpToPx(LoginActivity.this, 200)){
                        // Soft Keyboard is shown
                        Log.d(TAG, "LoginActivity layout height 차이점 ??");
                        imageView.setVisibility(View.GONE);
                        imageView2.setImageBitmap(deleteBackgroundImage());
                        imageView2.setVisibility(View.VISIBLE);
                    }else{
                        // Soft Keyboard is hidden
                        Log.d(TAG, "LoginActivity layout height 차이점 ???");
                        imageView.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.GONE);
                    }
                }
            }
        });
        imageView.setImageBitmap(deleteBackgroundImage());
    }

    //on click
    @OnClick({R.id.login_button, R.id.register_button})
    void buttonClicked(View view){
        int clicked = view.getId();
        switch (clicked){
            case R.id.login_button:
            {
                Toast.makeText(this, "login button clicked", Toast.LENGTH_SHORT).show();
                // edit text에 있는거 가져오자
                String id = id_editText.getText().toString();
                String password = password_editText.getText().toString();

                // id랑 password retrofit 써서 서버로 보내고

                // 서버에서 데이터 오는거 처리 받고
//                UserManager.checkLoginRequest(id, password);

                InteractionManager.getInstance(this).requestUserLogin(id, password, new OnMyApiListener() {
                    @Override
                    public void success(Object response) {
                        MyConfig.myInfo = (UserInfo)response;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void fail() {

                    }
                });

                break;
            }
            case R.id.register_button:
            {

                // register_activity 띄워주자
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public void hideViewsBeforeStarts(){
        id_editText.setVisibility(View.GONE);
        password_editText.setVisibility(View.GONE);
        login_button.setVisibility(View.GONE);
        register_button.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        id_editText.setAlpha(0f);
        id_editText.setVisibility(View.VISIBLE);
        password_editText.setAlpha(0f);
        password_editText.setVisibility(View.VISIBLE);
        login_button.setAlpha(0f);
        login_button.setVisibility(View.VISIBLE);
        register_button.setAlpha(0f);
        register_button.setVisibility(View.VISIBLE);

        int mediumAnimationTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        id_editText.animate()
                .alpha(1f).
                setDuration(mediumAnimationTime)
                .setListener(null);
        password_editText.animate()
                .alpha(1f).
                setDuration(mediumAnimationTime)
                .setListener(null);
        login_button.animate()
                .alpha(1f).
                setDuration(mediumAnimationTime)
                .setListener(null);
        register_button.animate()
                .alpha(1f).
                setDuration(mediumAnimationTime)
                .setListener(null);
        ANIMATION_ENDED = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public static float dpToPx(Context context, float valueInDp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public Bitmap deleteBackgroundImage(){
        Drawable d = imageView.getDrawable();
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