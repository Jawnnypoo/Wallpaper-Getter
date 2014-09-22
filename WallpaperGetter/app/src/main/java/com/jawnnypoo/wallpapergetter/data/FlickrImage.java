package com.jawnnypoo.wallpapergetter.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data that will represent an image from Flickr that we get from the API
 * Created by Jawn on 9/14/2014.
 */
public class FlickrImage {

    private static final String TAG = FlickrImage.class.getSimpleName();

    private static final String FIELD_FARM = 	"farm";
    private static final String FIELD_ID = 		"id";
    private static final String FIELD_PUBLIC = 	"ispublic";
    private static final String FIELD_FAMILY = 	"isfamily";
    private static final String FIELD_FRIEND = 	"isfriend";
    private static final String FIELD_OWNER = 	"owner";
    private static final String FIELD_SECRET = 	"secret";
    private static final String FIELD_SERVER = 	"server";
    private static final String FIELD_TITLE = 	"title";

    private int mFarm;
    private String mId;
    private boolean mIsPublic;
    private boolean mIsFamily;
    private boolean mIsFriend;
    private String mOwner;
    private String mSecret;
    private String mServer;
    private String mTitle;
    private String mUrl;

    public FlickrImage() {
        //Empty constructor, don't use
    }

    public static FlickrImage fromJSON(JSONObject jsonObject) {
        try {
            FlickrImage flickrImage = new FlickrImage();
            if (jsonObject.has(FIELD_FARM)) {
                flickrImage.mFarm = jsonObject.getInt(FIELD_FARM);
            }
            if (jsonObject.has(FIELD_ID)) {
                flickrImage.mId = jsonObject.getString(FIELD_ID);
            }
            if (jsonObject.has(FIELD_PUBLIC)) {
                flickrImage.mIsPublic = jsonObject.getInt(FIELD_PUBLIC) == 1;
            }
            if (jsonObject.has(FIELD_FAMILY)) {
                flickrImage.mIsFamily = jsonObject.getInt(FIELD_FAMILY) == 1;
            }
            if (jsonObject.has(FIELD_FRIEND)) {
                flickrImage.mIsFriend = jsonObject.getInt(FIELD_FRIEND) == 1;
            }
            if (jsonObject.has(FIELD_OWNER)) {
                flickrImage.mOwner = jsonObject.getString(FIELD_OWNER);
            }
            if (jsonObject.has(FIELD_SECRET)) {
                flickrImage.mSecret = jsonObject.getString(FIELD_SECRET);
            }
            if (jsonObject.has(FIELD_SERVER)) {
                flickrImage.mServer = jsonObject.getString(FIELD_SERVER);
            }
            if (jsonObject.has(FIELD_TITLE)) {
                flickrImage.mTitle = jsonObject.getString(FIELD_TITLE);
            }
            flickrImage.mUrl = generateImageUrl(flickrImage);
            return flickrImage;
        } catch (JSONException e) {
            Log.e(TAG, "Problem with JSON creating FlickrImage");
            e.printStackTrace();
        }
        return null;
    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * Create the full image url based on the information we have on the image
     */
    public static String generateImageUrl(FlickrImage flickrImage) {
        return "http://farm"
                + flickrImage.mFarm
                + ".static.flickr.com/"
                + flickrImage.mServer
                + "/"
                + flickrImage.mId
                + "_"
                + flickrImage.mSecret
                + ".jpg";
    }
}
