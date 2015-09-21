package com.jawnnypoo.wallpapergetter.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.FlickrImage;

/**
 * Manager class to allow for better control of how notifications are posted
 * Created by John on 9/29/2014.
 */
public class WallpaperNotificationManager {

    private static final String TAG = WallpaperNotificationManager.class.getSimpleName();

    private static final int DOWNLOAD_NOTIFICATION_ID = 123;

    public static void notifyDownloadStart(Context context, FlickrImage image) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        if (image.getTitle() != null && !image.getTitle().isEmpty()) {
            notificationBuilder.setContentTitle(image.getTitle());
        } else {
            notificationBuilder.setContentTitle(image.getId());
        }
        notificationBuilder.setContentText(context.getString(R.string.downloading))
                .setProgress(0, 0, true)
                .setSmallIcon(R.drawable.ic_launcher);
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Notify the user that the image download is complete. Since we use the same
     * notification id, it will remove the notifcation showing that the image is downloading
     * @param context
     * @param image
     */
    public static void notifyDownloadComplete(Context context, FlickrImage image, boolean success) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(image.getTitle())
                .setSmallIcon(R.drawable.ic_launcher);
        if (success) {
            notificationBuilder.setContentText(context.getString(R.string.download_complete));
            notificationBuilder.setTicker(context.getString(R.string.download_complete));
        } else {
            notificationBuilder.setContentText(context.getString(R.string.download_failed));
            notificationBuilder.setTicker(context.getString(R.string.download_failed));
        }
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
    }
}
