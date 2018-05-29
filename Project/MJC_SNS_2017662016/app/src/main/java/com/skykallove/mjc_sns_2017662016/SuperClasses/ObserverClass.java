package com.skykallove.mjc_sns_2017662016.SuperClasses;

import java.util.Observable;

/**
 * Created by sky on 2017-11-21.
 */

public class ObserverClass extends Observable {

    // ObserverClass 클래스 인스턴스 변수 생성
    private static ObserverClass _inst = null;

    // 인스턴스 반환 메서드
    public static ObserverClass getInstance() {
        // 인스턴스변수가 널이 아니면 반환, 그렇지 않으면 새 객체 생성
        _inst = (_inst != null ? _inst : new ObserverClass());
        return _inst;
    }
}
