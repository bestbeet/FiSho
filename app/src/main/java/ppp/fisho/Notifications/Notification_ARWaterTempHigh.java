package ppp.fisho.Notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.Context;
import android.os.Handler;

import com.google.firebase.database.DatabaseReference;

import ppp.fisho.MainActivity;
import ppp.fisho.R;

/**
 * Created by best on 10/9/2560.
 */

public class Notification_ARWaterTempHigh extends Service {
    private DatabaseReference myRef;
    private int notification_id;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private MediaPlayer player;
    Vibrator v;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the FiSho service");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final Handler handler = new Handler();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        notification_id = (int) System.currentTimeMillis();

        Intent notification_intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification_intent, 0);
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_small)
                .setContentTitle("FiSho Quality")
                .setContentText("Water High Temperatur")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX);

        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);
        //staring the player
        player.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                player.stop();
                v.vibrate(500);
            }
        }, 5000);

        notificationManager.notify(notification_id, builder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
    }

}