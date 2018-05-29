package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.Popups.ServerErrorPopup;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

/**
 * Created by sky on 2017-11-21.
 */

public class SplashActivity extends AppCompatActivity {

    // SplashActivity 를 담을 클래스 변수
    public static SplashActivity loadingActivity;

    // 프로그레스 바 선언
    // private ProgressBar progressBar;

    // 서버 구동 상태를 확인할 Url

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 상태표시줄 없애기
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 프로그레스 바 값 저장
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // 클래스 변수 초기화
        loadingActivity = SplashActivity.this;

        // ShowLoadingImageTask 실행
        ShowLoadingImageTask loadingImageTask = new ShowLoadingImageTask(Urls.SERVER_TEST, null);
        loadingImageTask.execute();

        // TODO: 2017-11-22 Make NetworkCheck Task and check network
    } // onCreate

    private class ShowLoadingImageTask extends AsyncTask<String, String, String> {

        private String url;
        private ContentValues values;

        public ShowLoadingImageTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        // 전처리
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그레스 바를 보이도록 설정
            // progressBar.setVisibility(View.VISIBLE);
        }

        // 백그라운드 처리
        @Override
        protected String doInBackground(String... params) {
            String serverState;
            // 서버 상태 확인
            HttpConnection requestConnection = HttpConnection.getInstance();
            serverState = requestConnection.request(url, values);
            try {
                // 화면을 0.5초 정지
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (serverState == null || serverState.equals("")) {
                serverState = "error";
            }
            return serverState;
        }

        // 후처리
        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            // 서버와 연결되어있으면 로그인 화면으로, 그렇지 않으면 팝업 띄우기
            // TODO: 2017-11-24 db connection check in server
            if (value.toString().equals("SERVER_OK")) {
                startIntent(LoginActivity.class);
                finish();
            }
            else {
                startIntent(ServerErrorPopup.class);
            }

        }
    }

    // 인텐트 기능
    private void startIntent(Class c) {
        // 인텐트 객체 생성
        Intent intent = new Intent(getApplicationContext(), c);
        // 인텐트 실행
        startActivity(intent);
        // = startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        // 프로그레스 바를 보이지 않도록 설정
        // progressBar.setVisibility(View.GONE);
        // 현재 액티비티 종료
//        this.finish();
    }

} // SplashActivity