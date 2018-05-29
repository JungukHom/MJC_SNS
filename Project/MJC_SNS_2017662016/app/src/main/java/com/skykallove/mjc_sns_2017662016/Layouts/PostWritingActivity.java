package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.DataFormatManager;
import com.skykallove.mjc_sns_2017662016.DataManager.UserDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.IntValue;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

/**
 * Created by sky on 2017-11-22.
 */

public class PostWritingActivity extends ViewOnClickListenerClass {

    Button writePost_cancel, writePost_ok;
    EditText writePost_title, writePost_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postwriting);
        setTitle(Strings.WRITE_POST);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 버튼 값 설정
        writePost_cancel = (Button) findViewById(R.id.writePost_cancel);
        writePost_ok = (Button) findViewById(R.id.writePost_ok);

        // 버튼 온클릭 리스너 설정
        writePost_cancel.setOnClickListener(this);
        writePost_ok.setOnClickListener(this);

        // 에디트텍스트 값 설정
        writePost_title = (EditText) findViewById(R.id.writePost_title);
        writePost_content = (EditText) findViewById(R.id.writePost_content);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.writePost_cancel:
                finish();
                break;
            case R.id.writePost_ok:
                String titleText = writePost_title.getText().toString().trim();
                String contentText = writePost_content.getText().toString().trim();
                if (!isAllValuesRight(titleText, contentText)) {
                    break;
                }

                DataFormatManager dataFormatManager = DataFormatManager.getInstance();
                UserDataManager userDataManager = UserDataManager.getInstance();
                PostWriteTask postWriteTask = new PostWriteTask(
                        Urls.ADD_POST,
                        dataFormatManager.makeContentValues(
                                writePost_title.getText().toString().trim(),
                                userDataManager.getUserName(),
                                writePost_content.getText().toString().trim()
                        )
                );
                postWriteTask.execute();
                break;
            default:
                break;
        }
    }

    public boolean isAllValuesRight(String titleText, String contentText) {

        // 제목이 비어있으면 토스트 메세지를 출력하고 return
        if (isCheckTextEmpty(titleText, Strings.INSERT_TITLE)) {
            return false;
        }
        Log.d(Tags.TAG_DEBUG, Boolean.toString(!isCheckTextEmpty(titleText, Strings.INSERT_TITLE)));
        // 내용이 비어있으면 토스트 메세지를 출력하고 return
        if (isCheckTextEmpty(contentText, Strings.INSERT_CONTENT)) {
            return false;
        }

        // 제목의 값을 체크하고 값이 너무 크면 토스트를 출력하고 return
        if (isTextSizeExceed(
                titleText,
                IntValue.POST_TITLE_MAXSIZE,
                Strings.POST_TITLE_SIZE_EXCEED
        )) {
            return false;
        }

        // 내용의 값을 체크하고 값이 너무 크면 토스트를 출력하고 return
        if (isTextSizeExceed(contentText,
                IntValue.POST_CONTENT_MAXSIZE,
                Strings.POST_CONTENT_SIZE_EXCEED
        )) {
            return false;
        }
        return true;
    }

    /**
     * 입력받은 값이 비어있는지 확인하고 토스트 메세지 출력
     *
     * @param checkValue String
     * @param message    String
     * @return boolean
     */
    public boolean isCheckTextEmpty(String checkValue, String message) {
        if (checkValue == null || checkValue.equals("")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * String의 길이를 구하고 값을 초과하면 토스트 메세지 출력
     *
     * @param text       String
     * @param size       int
     * @param toastValue String
     * @return boolean
     */
    public boolean isTextSizeExceed(String text, int size, String toastValue) {
        if (text.length() >= size) {
            Toast.makeText(this, toastValue, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private class PostWriteTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public PostWriteTask(String url, ContentValues values) {
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
            if (result.contains("COMMIT_OK")) {
                Toast.makeText(
                        PostWritingActivity.this,
                        Strings.COMMIT_SUCCESS,
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else if (result.contains("COMMIT_FAIL")) {
                Toast.makeText(
                        PostWritingActivity.this,
                        Strings.SERVER_ERROR,
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
