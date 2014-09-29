package com.jawnnypoo.wallpapergetter.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data that will represent an image from Flickr that we get from the API
 * Created by Jawn on 9/14/2014.
 */
public class FlickrImage implements Parcelable {

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

    private String mRawJSON;
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
            flickrImage.mRawJSON = jsonObject.toString();
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

    public static FlickrImage fromCursor(Cursor cursor) {
        String json = cursor.getString(cursor.getColumnIndex(WallpaperGetterContentProvider.KEY_COLUMN_JSON));
        try {
            return fromJSON(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getRawJSON() {
        return mRawJSON;
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

    protected FlickrImage(Parcel in) {
        mRawJSON = in.readString();
        mFarm = in.readInt();
        mId = in.readString();
        mIsPublic = in.readByte() != 0x00;
        mIsFamily = in.readByte() != 0x00;
        mIsFriend = in.readByte() != 0x00;
        mOwner = in.readString();
        mSecret = in.readString();
        mServer = in.readString();
        mTitle = in.readString();
        mUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRawJSON);
        dest.writeInt(mFarm);
        dest.writeString(mId);
        dest.writeByte((byte) (mIsPublic ? 0x01 : 0x00));
        dest.writeByte((byte) (mIsFamily ? 0x01 : 0x00));
        dest.writeByte((byte) (mIsFriend ? 0x01 : 0x00));
        dest.writeString(mOwner);
        dest.writeString(mSecret);
        dest.writeString(mServer);
        dest.writeString(mTitle);
        dest.writeString(mUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlickrImage> CREATOR = new Parcelable.Creator<FlickrImage>() {
        @Override
        public FlickrImage createFromParcel(Parcel in) {
            return new FlickrImage(in);
        }

        @Override
        public FlickrImage[] newArray(int size) {
            return new FlickrImage[size];
        }
    };
}