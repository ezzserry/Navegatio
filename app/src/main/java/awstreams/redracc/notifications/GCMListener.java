package awstreams.redracc.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import awstreams.redracc.R;
import awstreams.redracc.activities.DetailedNews_Activity;
import awstreams.redracc.helpers.Constants;

/**
 * Created by LENOVO on 12/10/2016.
 */
public class GCMListener extends GcmListenerService {
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isLogged_in;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isLogged_in = sharedPrefs.getBoolean(Constants.isLoggedin, false);

        if (!sharedPrefs.getBoolean(Constants.ifNotifications, false) || !sharedPrefs.getBoolean(Constants.isLoggedin, false)) {

            super.onMessageReceived(from, data);
            String message = data.getString("title");
            String id = data.getString("id");
            sendNotification(message, id);
        }
    }


    private void sendNotification(String message, String id) {
        editor = sharedPrefs.edit();
        Intent intent = new Intent(this, DetailedNews_Activity.class);
        intent.putExtra(getResources().getString(R.string.post_by_id_intent_key), id);
        intent.putExtra(getResources().getString(R.string.post_by_slug_intent_key), "");
        editor.putBoolean(Constants.isNotification, true);
        editor.apply();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_ic)
                .setContentTitle("Redracc")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
