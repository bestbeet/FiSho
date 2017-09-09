package ppp.fisho;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

    public DatabaseReference myRef1, myRef2;
    private TextView mFirebaseTextView1, mFirebaseTextView2;
        /*private int notification_id = 001;
        private NotificationCompat.Builder Builder;
        private NotificationManager mNotifyMgr;*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterquality_layout, container, false);

        mFirebaseTextView1 = (TextView) view.findViewById(R.id.WtemptextView);
        mFirebaseTextView2 = (TextView) view.findViewById(R.id.pHtextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TempWater
        myRef1 = database.getReference();
        myRef1.keepSynced(true);
        myRef1.orderByValue().limitToLast(1);
        // pH
        myRef2 = database.getReference();
        myRef2.keepSynced(true);
        myRef2.orderByValue().limitToLast(1);

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("WaterTemp"));
                mFirebaseTextView1.setText("Temperature : " + value + " C°");

                if (Float.parseFloat(value) <= 32) {
                    getActivity().startService(new Intent(getActivity(), Notification_Fertilization.class));
                } else {

                    getActivity().stopService(new Intent(getActivity(), Notification_Fertilization.class));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("pH"));
                mFirebaseTextView2.setText("pH : " + value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
        //return inflater.inflate(R.layout.waterquality_layout,null);
    }
}

class Notification_Fertilization extends Service {

    public DatabaseReference myRef1, myRef2;
    private TextView mFirebaseTextView1, mFirebaseTextView2;
    private int notification_id = 001;
    private NotificationCompat.Builder Builder;
    private NotificationManager mNotifyMgr;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference();
        myRef1.keepSynced(true);
        myRef1.orderByValue().limitToLast(1);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        Builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_small)
                .setContentTitle("FiSho")
                .setContentText("High Temperature")
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX);

// Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value = String.valueOf(map.get("WaterTemp"));
                //String notification = dataSnapshot.child("Notification").getValue(String.class);
                if (Float.parseFloat(value) >= 32)
                    mNotifyMgr.notify(notification_id, Builder.build());
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
}