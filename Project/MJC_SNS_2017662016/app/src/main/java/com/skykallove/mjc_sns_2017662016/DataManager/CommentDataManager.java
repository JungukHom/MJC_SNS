package com.skykallove.mjc_sns_2017662016.DataManager;

/**
 * Created by sky on 2017-12-01.
 */

public class CommentDataManager {

    private static  CommentDataManager _inst = null;

    public static CommentDataManager getInstance() {
        if (_inst == null) {
            _inst = new CommentDataManager();
        }
        return _inst;
    }

    public String writer;
    public String writedaytime;
    public String content;
}
