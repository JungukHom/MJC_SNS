package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.DataCheckManager;
import com.skykallove.mjc_sns_2017662016.DataManager.DataFormatManager;
import com.skykallove.mjc_sns_2017662016.DataManager.UserDataManager;
import com.skykallove.mjc_sns_2017662016.DataManager.VariousDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

import java.util.regex.Pattern;

/**
 * Created by sky on 2017-11-21.
 */

// ^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$

public class LoginActivity extends ViewOnClickListenerClass {

    // 버튼의 온클릭 리스너 인터페이스 처리

    private EditText id, password;
    Button login, register;
    CheckBox rememberUserId;
    VariousDataManager data;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 쉐어드 프리퍼런스 값 설정
        preferences =  getSharedPreferences(Tags.BASE_PREFERENCE, MODE_PRIVATE);

        // 쉐어드 프리퍼런스 에디터 값 설정
        editor = preferences.edit();

        // 데이터 클래스 인스턴스 가져오기
        data = VariousDataManager.getInstance();

        // 에디트 텍스트 값 저장
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);

        // 버튼 값 저장
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        // 체크박스 값 저장
        rememberUserId = (CheckBox) findViewById(R.id.rememberUserId);

        // 버튼 온클릭 리스너 설정
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        register.setOnClickListener(this);

        // 아이디를 기억했었으면, 어플리케이션이 실행될 때 저장된 아이디 값을 가져옴
        if ( preferences.getBoolean(Tags.REMEMBER_ID, false) ) {
            rememberUserId.setChecked(true);
            String savedId = preferences.getString(Tags.USER_ID, "");
            if (savedId != null && !savedId.equals("")) {
                id.setText(savedId);
            }
        }

//        // TODO: 2017-11-22 Delete this listener when release
//        // 실험용 롱클릭리스너 설정
//        login.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // 아이디를 프리퍼런스에 저장
//                editor.putString(Tags.USER_ID, id.getText().toString());
//                // 체크 유무를 프리퍼런스에 저장 (어플리케이션 재실행 때 사용)
//                if (rememberUserId.isChecked()) {
//                    editor.putBoolean(Tags.REMEMBER_ID, true);
//                } // 체크되어있지 않으면 아이디를 초기화
//                else {
//                    editor.putBoolean(Tags.REMEMBER_ID, false);
//                }
//                editor.commit();
//                startFreeBoardActivity();
//                return false;
//            }
//        });
    } // onCreate

    public void startFreeBoardActivity() {
        // 유저 아이디 저장
        saveUserId();
        // 로그인, 회원가입 액티비티 종료
        this.finish();
        if (RegisterActivity.registerActivity != null) {
            RegisterActivity.registerActivity.finish();
        }
//        try {
//            RegisterActivity.registerActivity.finish();
//        } catch (NullPointerException e) { // try
//        } // catch
        // 액티비티 시작
        startActivity(new Intent(getApplicationContext(), FreeBoardActivity.class));
    }

    public void saveUserId() {
        // 아이디
        String userId = id.getText().toString();

        // 아이디를 프리퍼런스에 저장
        editor.putString(Tags.USER_ID, userId);
        // 체크 유무를 프리퍼런스에 저장 (어플리케이션 재실행 때 사용)
        if (rememberUserId.isChecked()) {
            editor.putBoolean(Tags.REMEMBER_ID, true);
        } // 체크되어있지 않으면 아이디를 초기화
        else {
            editor.putBoolean(Tags.REMEMBER_ID, false);
        }
        editor.commit();

        VariousDataManager variousDataManager = VariousDataManager.getInstance();
        variousDataManager.setUserId(userId);
    }

    @Override
    public void onClick(View view) {
        // 뷰에서 클릭된 객체의 int 형 id값 저장
        int id = view.getId();
        switch (id) {
            // 로그인 버튼
            case R.id.login:
                // 아이디와 비밀번호의 유효성 검사 후 둘다 통과 될 시
                String myId = this.id.getText().toString();
                String password = this.password.getText().toString();
                DataCheckManager dcm = DataCheckManager.getInstance();
                if ( dcm.isIdValid(myId) && dcm.isPasswordValid(password) ) {
                    startLoginTask();
                }
                else {
                    Toast.makeText(this, Strings.CHECK_ID_PW, Toast.LENGTH_SHORT).show();
                }
                break;
            // 회원가입 버튼
            case R.id.register:
                // 회원가입 액티비티로의 인텐트
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            default:
        }
    }

    public void startLoginTask() {
        DataFormatManager dataFormatManager = DataFormatManager.getInstance();
        String myId = id.getText().toString();
        String myPw = password.getText().toString();
        LoginTask loginTask = new LoginTask(
                Urls.SERVER_LOGIN,
                dataFormatManager.makeContentValues(myId, myPw)
        );
        loginTask.execute();
    }

    private class LoginTask extends AsyncTask<String, String, String> {

        private String url;
        private ContentValues values;

        public LoginTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection loginConnection = HttpConnection.getInstance();
            result = loginConnection.request(url, values);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.contains("LOGIN_OK")) {
                startFreeBoardActivity();
            } // if
            else if (result.contains("LOGIN_FAIL")) {
                Toast.makeText(LoginActivity.this, Strings.LOGIN_FAIL, Toast.LENGTH_SHORT).show();
            } // else if
            else { // error
                // Strings.SERVER_ERROR
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            } // else

            // 유저 아이디 저장
            UserDataManager userDataManager = UserDataManager.getInstance();
            userDataManager.setUserName(id.getText().toString());
        }
    }
} // LoginActivity