package ppp.fisho;

import android.app.NotificationManager;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by best on 29/3/2560.
 */

public class TankFragment extends Fragment {

    private DatabaseReference gpond;
    private TextView WLevel,OS,PS;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tank_layout,container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Pond");


        WLevel = (TextView) view.findViewById(R.id.WLevel);
        OS = (TextView) view.findViewById(R.id.OxygenS);
        PS = (TextView) view.findViewById(R.id.PumpS);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TempWater
        gpond = database.getReference("Tank");
        gpond.keepSynced(true);
        gpond.orderByValue().limitToLast(1);
        gpond.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String valueWLevel = String.valueOf(map.get("WaterLevel"));
                String valueOxygen = String.valueOf(map.get("Oxygen"));
                String valuePump = String.valueOf(map.get("Pump"));

                WLevel.setText("Water Level : " + valueWLevel);
                OS.setText("Oxygen : " + valuePump);
                PS.setText("Pump : " + valueOxygen);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return  view;
    }


}
