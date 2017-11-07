package ppp.fisho.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ppp.fisho.Notifications.Notification_AFood;
import ppp.fisho.Notifications.Notification_ARWaterTempHigh;

/**
 * Created by best on 13/9/2560.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context,Notification_AFood.class);
        context.startService(serviceIntent);
    }
}