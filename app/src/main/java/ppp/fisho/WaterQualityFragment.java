package ppp.fisho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
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
    public float wt,p,t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterquality_layout, container, false);
        getActivity().setTitle("Water Quality");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        watertemp = (TextView) view.findViewById(R.id.WtemptextView);
        pH = (TextView) view.findViewById(R.id.pHtextView);
        turbidity = (TextView) view.findViewById(R.id.TurbiditytextView);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String icd = prefs.getString("IDC", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TempWater
        gvalue = database.getReference(icd).child("WaterQuality");
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
                wt = fWt;
                p = fpH;
                t = fTur;


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
