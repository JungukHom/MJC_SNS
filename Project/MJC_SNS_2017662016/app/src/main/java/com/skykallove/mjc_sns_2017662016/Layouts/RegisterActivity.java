package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.DataCheckManager;
import com.skykallove.mjc_sns_2017662016.DataManager.DataFormatManager;
import com.skykallove.mjc_sns_2017662016.DataManager.UserDataManager;
import com.skykallove.mjc_sns_2017662016.DataManager.VariousDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.Popups.CalendarPopup;
import com.skykallove.mjc_sns_2017662016.Popups.PersonalInformationPopup;
import com.skykallove.mjc_sns_2017662016.Popups.UseTermsPopup;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ObserverClass;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

/**
 * Created by sky on 2017-11-21.
 */

public class RegisterActivity extends ViewOnClickListenerClass {

    Button complete, selectDate;
    Button checkIdRight;
    EditText id, pw, name, birthday, department, configure;
    CheckBox useTerms_check, personalInfo_check;
    ObserverClass watcher;
    TextView useTerm, personalInfo;
    DataCheckManager dataCheckManager;
    DataFormatManager formatManager;

    public static RegisterActivity registerActivity;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(Strings.REGISTER);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // RegisterActivity 값 설정
        registerActivity = RegisterActivity.this;

        // DataCheckManager 클래스 인스턴스 가져오기
        dataCheckManager = DataCheckManager.getInstance();

        // DataFormatManager 클래스 인스턴스 가져오기
        formatManager = DataFormatManager.getInstance();

        // ObserverClass 클래스 인스턴스 가져오기
        watcher = ObserverClass.getInstance();

        // 버튼 값 설정
        complete = (Button) findViewById(R.id.complete);
        selectDate = (Button) findViewById(R.id.selectDate);
        checkIdRight = (Button) findViewById(R.id.checkIdRight);

        // 버튼 온클릭 리스너 설정
        complete.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        checkIdRight.setOnClickListener(this);

        // 에디트텍스트 값 설정
        id = (EditText) findViewById(R.id.register_id);
        pw = (EditText) findViewById(R.id.register_password);
        name = (EditText) findViewById(R.id.register_username);
        birthday = (EditText) findViewById(R.id.register_birthday);
        department = (EditText) findViewById(R.id.register_department);
        configure = (EditText) findViewById(R.id.register_configureNumber);

        // 체크박스 값 설정
        useTerms_check = (CheckBox) findViewById(R.id.useTerms);
        personalInfo_check = (CheckBox) findViewById(R.id.personalInformation);

        // 텍스트뷰 값 설정
        useTerm = (TextView) findViewById(R.id.useTermsTextView);
        personalInfo = (TextView) findViewById(R.id.personalInformationTextView);

        // 텍스트뷰 온클릭 설정
        useTerm.setOnClickListener(this);
        personalInfo.setOnClickListener(this);


//        R;

    } // onCreate

    // 액티비티가 Resume 될 때, 생년월일을 가져옴
    @Override
    protected void onResume() {
        super.onResume();
        String dateForm = VariousDataManager.getInstance().getDateForm();
        birthday.setText(dateForm);
    }

    // 온클릭 리스너 메서드
    @Override
    public void onClick(View v) {
        // 클릭한 뷰의 아이디를 가져옴
        switch (v.getId()) {
            // 가입 완료 버튼
            case R.id.complete:
                // 값이 완벽한지 확인
                if (! checkValues()) {
                    Toast.makeText(this, "입력하신 값을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 약관을 모두 동의하지 않았을 때
                if (! isAgreementPassed()) {
                    Toast.makeText(this, "약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 태스크 실행
                RegisterTask registerTask = new RegisterTask(
                        Urls.REGISTER,
                        formatManager.makeContentValues(
                                id.getText().toString(),
                                pw.getText().toString(),
                                name.getText().toString(),
                                birthday.getText().toString(),
                                department.getText().toString(),
                                configure.getText().toString()
                        )
                );
                registerTask.execute();
                break;
            // 아이디 중복 확인
            case R.id.checkIdRight:
                String myId = id.getText().toString();
                CheckIdTask checkIdTask = new CheckIdTask(
                        Urls.ID_CHECK,
                        formatManager.makeContentValues(myId)
                );
                checkIdTask.execute();
                break;
            // 날짜 설정
            case R.id.selectDate:
                // 액티비티 시작
                startIntent(CalendarPopup.class);
                break;
            // 이용약관 글자
            case R.id.useTermsTextView:
                // 체크박스 아이디를 팝업으로 넘기기
                new UseTermsPopup().setCheckBox((CheckBox) findViewById(R.id.useTerms));
                // 액티비티 시작
                startIntent(UseTermsPopup.class);
                break;
            // 개인정보처리방침 글자
            case R.id.personalInformationTextView:
                // 체크박스 아이디를 팝업으로 넘기기
                new PersonalInformationPopup().setCheckBox(
                        (CheckBox) findViewById(R.id.personalInformation));
                // 액티비티 시작
                startIntent(PersonalInformationPopup.class);
                break;
            default:
                break;
        }
    }

    // startActivity()
    public void startIntent(Class c) {
        startActivity(new Intent(getApplicationContext(), c));
    }

    // 약관을 모두 동의했는지 체크
    public boolean isAgreementPassed() {
        if (useTerms_check.isChecked() && personalInfo_check.isChecked()) {
            return true;
        }
        return false;
    }

    public boolean checkValues() {
        // 값 받아오기
        String id = this.id.getText().toString();
        String pw = this.pw.getText().toString();
        String name = this.name.getText().toString();
        String birthday = this.birthday.getText().toString();
        String department = this.department.getText().toString();
        String configure = this.configure.getText().toString();

        // 모든 리턴값이 true 이면 true 반환
        if (
             dataCheckManager.isIdValid(id) &&
             dataCheckManager.isPasswordValid(pw) &&
             dataCheckManager.isNameValid(name) &&
             dataCheckManager.isBirthdayValid(birthday) &&
             dataCheckManager.isDepartmentValid(department) &&
             dataCheckManager.isConfigureValid(configure)
                ) {
            return true;
        }
        return false;
    }

    private class CheckIdTask extends AsyncTask<String, String, String> {

        private String url;
        private ContentValues value;

        public CheckIdTask(String url, ContentValues values) {
            this.url = url;
            this.value = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection loginConnection = HttpConnection.getInstance();
            result = loginConnection.request(url, value);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("ID_EXIST")) {
                // TODO: 2017-11-27  팝업 띄우기
                Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show();
            }
            else if (result.equals("ID_NOT_EXIST")) {
                Toast.makeText(RegisterActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, String, String> {

        private String url;
        private ContentValues value;

        public RegisterTask(String url, ContentValues values) {
            this.url = url;
            this.value = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection loginConnection = new HttpConnection();
            result = loginConnection.request(url, value);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.contains("REGISTER_OK")) {
                Toast.makeText(
                        RegisterActivity.this,
                        Strings.REGISTER_SUCCESS,
                        Toast.LENGTH_SHORT
                ).show();
                // 유저 아이디 저장
                UserDataManager userDataManager = UserDataManager.getInstance();
                userDataManager.setUserName(id.getText().toString());
                finish();
            }
            else if (result.contains("REGISTER_FAIL")) {
                Toast.makeText(
                        RegisterActivity.this,
                        Strings.REGISTER_FAIL, 
                        Toast.LENGTH_SHORT
                ).show();
            }
            else {
                Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }


} // RegisterActivity