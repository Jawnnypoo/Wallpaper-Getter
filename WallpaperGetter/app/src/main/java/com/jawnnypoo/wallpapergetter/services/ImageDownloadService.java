package com.jawnnypoo.wallpapergetter.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.FlickrImage;
import com.jawnnypoo.wallpapergetter.notification.WallpaperNotificationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Service we use to download an image. We use a service, since it will continue to run
 * until the job is done and will not get canceled if the app is closed
 * Created by John on 9/29/2014.
 */
public class ImageDownloadService extends IntentService{

    private static final String TAG = ImageDownloadService.class.getSimpleName();

    public static final String EXTRA_IMAGE = "extra_image";

    public static Intent newInstance(Context context, FlickrImage image) {
        Intent intent = new Intent(context, ImageDownloadService.class);
        intent.putExtra(EXTRA_IMAGE, image);
        return intent;
    }

    public ImageDownloadService() {
        super(TAG);
        Log.v(TAG, "Creating an ImageDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Starting the image download service");
        FlickrImage imageToSave = intent.getParcelableExtra(EXTRA_IMAGE);
        new SaveImageTask(getApplicationContext()).execute(imageToSave);
    }

    public class SaveImageTask extends AsyncTask<FlickrImage, Void, Boolean> {
        boolean success = false;
        Context mContext;
        FlickrImage mImage;

        public SaveImageTask(Context context) {
            mContext = context;
        }

        //Pull the bitmap from the specified URL
        @Override
        protected Boolean doInBackground(FlickrImage... params) {
            mImage = params[0];
            //We would normally do this onPreExecute, but at that point, we do not have the
            //image that we want to download
            WallpaperNotificationManager.notifyDownloadStart(getApplicationContext(), mImage);

            Bitmap imageBitmap = null;
            try {
                InputStream in = new java.net.URL(mImage.getUrl()).openStream();
                imageBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            //Still null? Failure to download
            if (imageBitmap == null) {
                return false;
            }

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + mContext.getString(R.string.app_name));
            myDir.mkdirs();

            File file = new File (myDir, mImage.getId() + ".jpg");
            if (file.exists()) { file.delete (); }
            try {
                FileOutputStream out = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            WallpaperNotificationManager.notifyDownloadComplete(getApplicationContext(), mImage, result);
        }
    }
}
