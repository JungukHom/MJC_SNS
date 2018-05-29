package com.skykallove.mjc_sns_2017662016.DataManager;

/**
 * Created by sky on 2017-11-22.
 */

public class PostDataManager {

    private static  PostDataManager _inst = null;

    public static PostDataManager getInstance() {
        if (_inst == null) {
            _inst = new PostDataManager();
        }
        return _inst;
    }

    public String postNumber;
    public String title;
    public String writeDayTime;
    public String writer;
    public String like;
    public String dislike;
    public String content;
    public String commentCount;

}
