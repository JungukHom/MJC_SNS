package com.skykallove.mjc_sns_2017662016.Popups;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

/**
 * Created by sky on 2017-11-25.
 */

public class PersonalInformationPopup extends ViewOnClickListenerClass {

    Button not_accept, accept;
    static CheckBox checkBox = null;
    TextView personalInformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_personal_information);
        setTitle(Strings.PERSONAL_INFORMATION);

        // 텍스트뷰 값 설정
        personalInformation = (TextView) findViewById(R.id.popup_personalInformation);

        // 버튼 값 설정
        not_accept = (Button) findViewById(R.id.personalInformation_not_accept);
        accept = (Button) findViewById(R.id.personalInformation_accept);

        // 버튼 온클릭 리스너 설정
        not_accept.setOnClickListener(this);
        accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.personalInformation_not_accept:
                finish();
                Toast.makeText(this, Strings.PERSONAL_INFORMATION +
                        Strings.CANT_USE, Toast.LENGTH_SHORT).show();
                break;
            case R.id.personalInformation_accept:
                checkBox.setChecked(true);
                finish();
                break;
            default:
                break;
        }
    }

    // 체크박스 id를 넘겨받기 위한 메서드
    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
