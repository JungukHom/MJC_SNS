package com.skykallove.mjc_sns_2017662016.DataManager;

/**
 * Created by sky on 2017-11-21.
 */

public class VariousDataManager {

    // VariousDataManager 인스턴스 초기화
    private static VariousDataManager _inst = null;

    // VariousDataManager 인스턴스 게터 선언
    public static VariousDataManager getInstance() {
        // 인스턴스변수가 널이 아니면 반환, 그렇지 않으면 개 객체 생성
        _inst = (_inst != null ? _inst : new VariousDataManager());
        return _inst;
    }

    public String postNumber = "";


    // 데이트폼 변수
    protected String dateForm = "";

    // 데이트폼 세터
    public void setDateForm(String dateForm) {
        // 데이트폼 값 업데이트
        this.dateForm = dateForm;
    }

    // 데이트폼 게터
    public String getDateForm() {
        // dateForm 값 복사
        String _copy = dateForm;
        // 복사한 값 리턴
        return _copy;
    }


    // No Usage
    // 유저 아이디 변수
    private String userId = "id error 01";

    // 유저 아이디 세터
    public void setUserId(String userId) {
        // 유저 아이디 값 업데이트
        this.userId = userId;
    }

    // 유저 아이디 게터
    public String getUserId() {
        // 유저 아이디 값 복사
        String _copy = userId;
        // 복사한 값 리턴
        return _copy;
    }
    // No Usage
}
