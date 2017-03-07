package com.example.mk.mysmartsns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.UserInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @Bind(R.id.input_id)
    EditText id_editText;
    @Bind(R.id.input_password)
    EditText password_editText;
    @Bind(R.id.login_button)
    Button login_button;
    @Bind(R.id.register_button)
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Butterknife bind
        ButterKnife.bind(this);
    }

    //on click
    @OnClick({R.id.login_button, R.id.register_button})
    void buttonClicked(View view){
        int clicked = view.getId();
        switch (clicked){
            case R.id.login_button:
            {
//                Toast.makeText(this, "login button clicked", Toast.LENGTH_SHORT).show();
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
}
