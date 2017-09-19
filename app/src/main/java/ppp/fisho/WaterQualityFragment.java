package ppp.fisho;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

import ppp.fisho.Notifications.Notification_ARTurbidityHigh;
import ppp.fisho.Notifications.Notification_ARTurbidityLow;
import ppp.fisho.Notifications.Notification_ARWaterTempHigh;
import ppp.fisho.Notifications.Notification_ARWaterTempLow;
import ppp.fisho.Notifications.Notification_ARpHHigh;

/**
 * Created by best on 29/3/2560.
 */

public class WaterQualityFragment extends Fragment {

    private DatabaseReference gvalue,gnoti;
    private TextView watertemp, pH,turbidity;
    public float fWt,fpH,fTur;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterquality_layout, container, false);
        getActivity().setTitle("Water Quality");
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        watertemp = (TextView) view.findViewById(R.id.WtemptextView);
        pH = (TextView) view.findViewById(R.id.pHtextView);
        turbidity = (TextView) view.findViewById(R.id.TurbiditytextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TempWater
        gvalue = database.getReference("WaterQuality");
        gvalue.keepSynced(true);
        gvalue.orderByValue().limitToLast(1);
        gvalue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String valueWt = String.valueOf(map.get("Temp"));
                String valuepH = String.valueOf(map.get("pH"));
                String valueTur = String.valueOf(map.get("Turbidity"));

                // เก็บตัวแปรไว้เปรียบเทียบ
                fWt = Float.parseFloat(valueWt);
                fpH = Float.parseFloat(valuepH);
                fTur = Float.parseFloat(valueTur);

                watertemp.setText("Temperature : " + valueWt + " °C");
                pH.setText("pH : " + valuepH);
                turbidity.setText("Turbidity : "+ valueTur + " UTF");

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //
        gnoti = database.getReference("Setting");
        gnoti.keepSynced(true);
        gnoti.orderByValue().limitToLast(1);
        gnoti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String valueTH = String.valueOf(map.get("TempH"));
                String valueTL = String.valueOf(map.get("TempL"));
                String valuepHH = String.valueOf(map.get("pHH"));
                String valuepHL = String.valueOf(map.get("pHL"));
                String valueTurH = String.valueOf(map.get("TurH"));
                String valueTurL = String.valueOf(map.get("TurL"));

                ////////// Temp High //////////
                if (fWt >= Float.parseFloat(valueTH)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARWaterTempHigh.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARWaterTempHigh.class));
                }
                ///////// Temp Low //////////
                if (fWt <= Float.parseFloat(valueTL)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARWaterTempLow.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARWaterTempLow.class));
                }
                ////////// pH High ///////////
                if (fpH >= Float.parseFloat(valuepHH)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARpHHigh.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARpHHigh.class));
                }
                ////////// pH Low ///////////
                if (fpH <= Float.parseFloat(valuepHL)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARpHHigh.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARpHHigh.class));
                }
                //////////// Turbidity High ///////////////////////
                if (fTur >= Float.parseFloat(valueTurH)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARTurbidityHigh.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARTurbidityHigh.class));
                }
                //////////// Turbidity Low ///////////////////////
                if (fTur <= Float.parseFloat(valueTurL)) {
                    getActivity().startService(new Intent(getActivity(), Notification_ARTurbidityLow.class));
                } else {
                    getActivity().stopService(new Intent(getActivity(), Notification_ARTurbidityLow.class));
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