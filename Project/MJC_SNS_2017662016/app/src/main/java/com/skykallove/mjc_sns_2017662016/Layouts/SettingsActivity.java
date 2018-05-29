package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.UserDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

/**
 * Created by sky on 2017-11-21.
 */

public class SettingsActivity extends ViewOnClickListenerClass {

    Button logout, memberLeave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(Strings.SETTINGS);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 버튼 값 설정
        logout = (Button) findViewById(R.id.logout);
        memberLeave = (Button) findViewById(R.id.memberLeave);

        // 버튼 온클릭 리스너 설정
        logout.setOnClickListener(this);
        memberLeave.setOnClickListener(this);


    } // onCreate

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.logout:
                // 프리보드 액티비티 종료
                showFinishAlertDialog(Strings.REALLY_LOGOUT);
                break;
            case R.id.memberLeave:
                // TODO: 2017-12-05 member leave
                ContentValues values = new ContentValues();
                UserDataManager userDataManager = UserDataManager.getInstance();
                values.put("id", userDataManager.getUserName());
                RemoveUserTask removeUserTask = new RemoveUserTask(Urls.REMOVE_USER, values);
                removeUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            default:
                break;
        } // switch

    }

    public void startIntent(Class nextClass) {
        startActivity(new Intent(getApplicationContext(), nextClass));
    }

    public void showFinishAlertDialog(String message) {
        // AlertDialog(Builder) 객체 생성
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(this);
        // AlertDialog(Builder) 메인 메세지 설정
        finishDialog.setMessage(message);
        // AlertDialog(Builder) 긍정 버튼 설정
        finishDialog.setPositiveButton(Strings.YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 모든 액티비티 종료 후 로그인액티비티로 인텐트
                FreeBoardActivity.freeBoardActivity.finish();
                finish();
                startIntent(LoginActivity.class);
            }
        });
        // AlertDialog(Builder) 부정 버튼 설정
        finishDialog.setNegativeButton(Strings.NO, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 메세지 창 취소
                dialog.cancel();
            }
        });
        // AlertDialog(Builder) 글자 색 설정
        final AlertDialog dialog = finishDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            }
        });
        // AlertDialog(Builder) 활성화
        finishDialog.create().show();
    }

    private class RemoveUserTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public RemoveUserTask (String url, ContentValues values) {
            // TODO: 2017-12-13 마무리하기
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection connection = HttpConnection.getInstance();
            result = connection.request(url, values);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(Tags.TAG_DEBUG, result);
            if (result.equals("DELETE_SUCCESS")) {
                Toast.makeText(SettingsActivity.this, Strings.USER_REMOVE_OK, Toast.LENGTH_SHORT).show();
                startIntent(LoginActivity.class);
            }
            else  { // if (result.equals("DELETE_FAIL"))+
                Toast.makeText(SettingsActivity.this, Strings.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        }




    }
} // SettingsActivity