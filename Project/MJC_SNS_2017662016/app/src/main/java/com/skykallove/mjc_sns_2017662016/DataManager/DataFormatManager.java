package com.skykallove.mjc_sns_2017662016.DataManager;

import android.content.ContentValues;

/**
 * Created by sky on 2017-11-27.
 */

public class DataFormatManager {

    private static DataFormatManager _inst = null;

    public static DataFormatManager getInstance() {
        if (_inst == null) {
            _inst = new DataFormatManager();
        }
        return _inst;
    }

    public ContentValues makeContentValues(String id) {
        ContentValues checkIdValue = new ContentValues();
        checkIdValue.put("id", id);
        return checkIdValue;
    }

    public ContentValues makeContentValues(String id, String pw) {
        ContentValues checkIdValue = new ContentValues();
        checkIdValue.put("id", id);
        checkIdValue.put("pw", pw);
        return checkIdValue;
    }

    public ContentValues makeContentValues(String id, String pw, String name, String birthday,
                                           String department, String configure) {
        ContentValues registerValues = new ContentValues();
        registerValues.put("id", id);
        registerValues.put("pw", pw);
        registerValues.put("name", name);
        registerValues.put("birthday", birthday);
        registerValues.put("department", department);
        registerValues.put("configure", configure);
        return registerValues;
    }

    public ContentValues makeContentValues(String title, String writer, String content) {
        ContentValues writePostValues = new ContentValues();
        writePostValues.put("title", title);
        writePostValues.put("writer", writer);
        writePostValues.put("content", content);
        return writePostValues;
    }

}
