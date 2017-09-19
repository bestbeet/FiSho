package ppp.fisho.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ppp.fisho.Notifications.Notification_ARWaterTempHigh;

/**
 * Created by best on 13/9/2560.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context,Notification_ARWaterTempHigh.class);
        serviceIntent.putExtra("extra", state);
        if(state.equals("yes"))
            context.startService(serviceIntent);
        else
            context.stopService(serviceIntent);
    }
}