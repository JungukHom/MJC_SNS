package com.skykallove.mjc_sns_2017662016.DataManager;

/**
 * Created by sky on 2017-11-27.
 */

public class UserDataManager {

    private static  UserDataManager _inst = null;

    public static UserDataManager getInstance() {
        if (_inst == null) {
            _inst = new UserDataManager();
        }
        return _inst;
    }

    private String userName = "";

    public String getUserName () {
        String _userName = this.userName;
        return _userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
