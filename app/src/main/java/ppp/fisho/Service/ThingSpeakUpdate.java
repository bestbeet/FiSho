package ppp.fisho.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by best on 15/9/2560.
 */

public class ThingSpeakUpdate extends Service {
    private RequestQueue requestQueue;
    private DatabaseReference thinkUp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestQueue = Volley.newRequestQueue(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        thinkUp = database.getReference("WaterQuality");
        thinkUp.keepSynced(true);
        thinkUp.orderByValue().limitToLast(1);

        thinkUp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String valueTemp = String.valueOf(map.get("Temp"));
                String valuepH = String.valueOf(map.get("pH"));
                String valueTur = String.valueOf(map.get("Turbidity"));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://api.thingspeak.com/update?api_key=W986B2XBZA8GEZFQ&field1=" + valueTemp +"&field2=" + valuepH +"&field3=" +valueTur ,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }

}