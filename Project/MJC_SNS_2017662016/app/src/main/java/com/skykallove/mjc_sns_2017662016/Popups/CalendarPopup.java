package com.skykallove.mjc_sns_2017662016.Popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.skykallove.mjc_sns_2017662016.DataManager.VariousDataManager;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;

/**
 * Created by sky on 2017-11-21.
 */

public class CalendarPopup extends ViewOnClickListenerClass {

    Button ok, cancel;
    DatePicker datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_calendar);
        setTitle("생년월일");

        // 데이트피커 값 설정
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        // 버튼 값 설정
        ok = (Button) findViewById(R.id.calendar_ok);
        cancel = (Button) findViewById(R.id.calendar_cancel);

        // 버튼 온클릭리스너 지정
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 클릭된 뷰의 아이디를 가져옴
        switch (v.getId()) {
            // 확인 버튼일
            case R.id.calendar_ok:
                sendDateForm();
                break;
            // 취소 버튼일
            case R.id.calendar_cancel:
                CalendarPopup.this.finish();
                break;
            default:
                break;
        }
    }

    // 년, 월, 일을 받아 특정 폼으로 통합해 반환
    private String makeDateForm(String year, String month, String day) {
        // 자바에서 월을 가져오면 1을 적게 가져오는것을 해결하기 위해 +1시켜주고 합침
        month = Integer.toString(Integer.parseInt(month) + 1);
        // 월이 한자리수면
        if (month.length() == 1) {
            // 월 앞에 0을 붙혀 통일성있게 만듬
            month = "0" + month;
        }
        String _temp = year + month + day;
        return _temp;
    }

    private void sendDateForm() {
        // makeDateForm 메서드 실행
        String dateForm = makeDateForm(
                Integer.toString(datePicker.getYear()),
                Integer.toString(datePicker.getMonth()),
                Integer.toString(datePicker.getDayOfMonth())
        );
        // VariousDataManager 클래스의 dateForm 변수에 값 저장
        VariousDataManager.getInstance().setDateForm(dateForm);
        // RegisterActivity 내의 ditText 값 없데이트 메서드 실행
//        RegisterActivity registerActivity = new RegisterActivity();
//        registerActivity.setBirthdayText();
        // 팝업 액티비티 종료
        finish();
    }
}
