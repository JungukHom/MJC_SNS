package com.skykallove.mjc_sns_2017662016.Popups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.skykallove.mjc_sns_2017662016.Layouts.SplashActivity;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.Strings;

/**
 * Created by sky on 2017-11-24.
 */

public class ServerErrorPopup extends ViewOnClickListenerClass {

    Button serverError_reTry, serverError_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_servererror);
        setTitle(Strings.SORRY_MESSAGE);

        // 버튼 값 설정
        serverError_reTry = (Button) findViewById(R.id.serverError_reTry);
        serverError_ok = (Button) findViewById(R.id.serverError_ok);

        // 버튼 온클릭 리스너 설정
        serverError_reTry.setOnClickListener(this);
        serverError_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.serverError_reTry:
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                SplashActivity.loadingActivity.finish();
                finish();
                break;
            case R.id.serverError_ok:
                // 어플리케이션 강제종료
                android.os.Process.killProcess(android.os.Process.myPid());
                System.gc();
                break;
            default:
                break;
        }
    }
}
