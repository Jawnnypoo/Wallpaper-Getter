package com.jawnnypoo.wallpapergetter.data;

/**
 * Data that will represent a wallpaper that we get from the API
 * Created by Jawn on 9/14/2014.
 */
public class Wallpaper {
    private int mId;
    private String mUrl;

    public Wallpaper(int id, String url) {
        mId = id;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
