package com.jawnnypoo.wallpapergetter.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.FlickrImage;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A loader that works asynchronously to get search results from Flickr's search
 * API. We then take the response JSON and turn it into an ArrayList of FlickrImages
 *
 * See more about Loaders here: http://developer.android.com/guide/components/loaders.html
 * Created by John on 9/20/2014.
 */
public class FlickrSearchLoader extends AsyncTaskLoader<ArrayList<FlickrImage>> {

    private static final String TAG = FlickrSearchLoader.class.getSimpleName();

    private static final String BASE_SEARCH_URL = "https://api.flickr.com/services/rest/";
    private static final String ARG_FORMAT = "format=json";
    private static final String ARG_SORT = "&sort=random";
    private static final String ARG_METHOD = "&method=flickr.photos.search";
    private static final String ARG_TAGS = "&tags=";
    private static final String ARG_TAG_MODE = "&tag_mod=all";
    private static final String ARG_API_KEY = "&api_key=";
    private static final String ARG_NO_JSON_CALLBACK = "&nojsoncallback=1";

    private static final String FIELD_PHOTOS = "photos";
    private static final String FIELD_PHOTO = "photo";

    private String mSearchTerm;

    public FlickrSearchLoader(Context context, String searchTerm) {
        super(context);
        mSearchTerm = searchTerm;
    }

    @Override
    public ArrayList<FlickrImage> loadInBackground() {
        ArrayList<FlickrImage> images = new ArrayList<FlickrImage>();

        mSearchTerm = normalizeSearchTerm(mSearchTerm);
        OkHttpClient client = new OkHttpClient();
        String searchUrl = buildSearchUrl();
        Log.v(TAG, "Flickr search URL: " + searchUrl);
        Request request = new Request.Builder()
                .url(buildSearchUrl())
                .build();
        try {
            //With this call, we make a call and wait here until we get
            //a response from the server. So, never do this on the main thread
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject photosRoot = root.getJSONObject(FIELD_PHOTOS);
            JSONArray photoArray = photosRoot.getJSONArray(FIELD_PHOTO);
            for (int i=0; i<photoArray.length(); i++) {
                images.add(FlickrImage.fromJSON(photoArray.getJSONObject(i)));
            }
            Log.v(TAG, jsonResponse);
        } catch (IOException e) {
            Log.e(TAG, "IO Error while getting search response");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Error with JSON response");
            e.printStackTrace();
        }

        return images;
    }

    /**
     * Make the search term ready for being a URL
     * @param searchTerm
     * @return
     */
    private String normalizeSearchTerm(String searchTerm) {
        return searchTerm.replaceAll(" ", "+");
    }

    private String buildSearchUrl() {
        return  BASE_SEARCH_URL
                + "?"
                + ARG_FORMAT
                + ARG_SORT
                + ARG_METHOD
                + ARG_TAGS
                + mSearchTerm
                + ARG_TAG_MODE
                + ARG_API_KEY
                + getContext().getString(R.string.api_key_flickr)
                + ARG_NO_JSON_CALLBACK;
    }

}
