package ppp.fisho;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.security.cert.Extension;
import java.util.Map;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by best on 29/3/2560.
 */

public class WaterQualityFragment extends Fragment {

    private DatabaseReference myRef1, myRef2;
    private TextView mFirebaseTextView1, mFirebaseTextView2;
    private int notification_id = 001;
    private NotificationCompat.Builder Builder;
    private NotificationManager mNotifyMgr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterquality_layout, container, false);
        getActivity().setTitle("Water Quality");
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFirebaseTextView1 = (TextView) view.findViewById(R.id.WtemptextView);
        mFirebaseTextView2 = (TextView) view.findViewById(R.id.pHtextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TempWater
        myRef1 = database.getReference("WaterQuality");
        myRef1.keepSynced(true);
        myRef1.orderByValue().limitToLast(1);
        /*Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, 0);
        Builder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.icon_small)
                .setContentTitle("FiSho")
                .setContentText("High Temperature")
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX);

        mNotifyMgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notification_id, Builder.build());*/


        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String valueWt = String.valueOf(map.get("Temp"));
                String valuepH = String.valueOf(map.get("pH"));
                mFirebaseTextView2.setText("pH : " + valuepH);
                mFirebaseTextView1.setText("Temperature : " + valueWt + " C°");
// รอแก้ ให้ผู้ใช้ set ค่า
                if (Float.parseFloat(valueWt) >= 32) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARWaterTemp.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARWaterTemp.class));
                }
                if (Float.parseFloat(valuepH) >= 9) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARpHHigh.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARpHHigh.class));
                }
                if (Float.parseFloat(valueWt) <= 4) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARpHLow.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARpHLow.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}
/*
class Notification_WaterQuality extends Service {

    private DatabaseReference myRef;
    private int notification_id = 001;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("WaterQuality");
        myRef.keepSynced(true);
        myRef.orderByValue().limitToLast(1);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_small)
                .setContentTitle("FiSho")
                .setContentText("High Temperature")
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX);

// Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notification_id, mBuilder.build());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("Temp"));
                String notification = dataSnapshot.child("Notification").getValue(String.class);
                if (Float.parseFloat(value) >= 32) {
                    // mNotifyMgr.notify(notification_id, mBuilder.build());
                } else ;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
    }
}*/