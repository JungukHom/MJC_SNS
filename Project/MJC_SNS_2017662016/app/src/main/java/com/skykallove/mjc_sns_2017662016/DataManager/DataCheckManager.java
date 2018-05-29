package com.skykallove.mjc_sns_2017662016.DataManager;

import android.util.Log;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.Values.Tags;

import java.util.regex.Pattern;

/**
 * Created by sky on 2017-11-25.
 */

public class DataCheckManager {

    private static DataCheckManager _inst = null;

    public static DataCheckManager getInstance() {
        if (_inst == null) {
            _inst = new DataCheckManager();
        }
        return _inst;
    }

    // 아이디 검사
    public boolean isIdValid (String id) {
        // 정규식을 이용한 아이디 유효성 검사
        // 영문 대소문자, 숫자, _, 6 ~ 12자
        if (Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{5,11}$", id)) {
            return true;
        }
        return false;
    }

    // 비밀번호 검사
    public boolean isPasswordValid (String password) {
        // 정규식을 이용한 비밀번호 유효성 검사
        // 영문 대소문자, 숫자, _, 8 ~ 12자
        if (Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9]{7,11}$", password)) {
            return true;
        }
        return false;
    }

    // 이름 검사
    public boolean isNameValid(String name) {
        if (name.length() <= 5) {
            return true;
        }
        return false;
    }

    // 생년월일 걺사
    public boolean isBirthdayValid(String birthday) {
        if (birthday.length() == 8) {
            try {
                Integer.parseInt(birthday);
            }
            catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    // 학과 검사
    public boolean isDepartmentValid(String department) {
        if (department.length() <= 12) {
            return true;
        }
        return false;
    }

    // 학번 검사
    public boolean isConfigureValid(String configure) {
        if (configure.length() == 10) {
            try {
                Integer.parseInt(configure);
            }
            catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }

}
