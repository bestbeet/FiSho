package ppp.fisho;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ppp.fisho.Notifications.Notification_AFood;
import ppp.fisho.Service.AlarmReceiver;

/**
 * Created by best on 5/4/2560.
 */

public class FeedTimeFragment extends Fragment {
    private TimePicker timefood;
    private ToggleButton tb;
    private Button mor,af,eve;
    private EditText fm;
    private DatabaseReference time;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_feed_time,container,false);
        getActivity().setTitle("Feed Time");
        timefood = (TimePicker)view.findViewById(R.id.TimeFood);
        tb = (ToggleButton) view.findViewById(R.id.set1);
        //mor = (Button) view.findViewById(R.id.morning);
        af = (Button) view.findViewById(R.id.afternoon);
        eve = (Button) view.findViewById(R.id.evening);
        fm = (EditText) view.findViewById(R.id.minute);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        time = database.getReference("FeedSet");
        time.keepSynced(true);
        time.orderByValue().limitToLast(1);

        final Intent myIntent = new Intent(getActivity(), Notification_AFood.class);
        alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        timefood.setIs24HourView(true);


        /*time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value, value1;
                value = String.valueOf(map.get("Alert"));
                value1 = String.valueOf(map.get("Notification"));
                if (value.equals("Enable"))
                    tv.setText("Set time: "  + String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute()));
                if (value.equals("Disable"))
                    tv.setText("System : Disable");
                if (value.equals("Disable") && value1.equals("Enable")) {
                    alarmManager.cancel(pending_intent);
                    getActivity().stopService(new Intent(getActivity(), Notification_AFertilization.class));
                    tv.setText("System : Disable");
                } else ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> value = new HashMap<String, Object>();

                        if (tb.isChecked()) {

                            calendar.add(Calendar.SECOND, 3);
                            final int hour = timefood.getCurrentHour();
                            final int minute = timefood.getCurrentMinute();
                            Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                            calendar.set(Calendar.HOUR_OF_DAY, timefood.getCurrentHour());
                            calendar.set(Calendar.MINUTE, timefood.getCurrentMinute());
                            myIntent.putExtra("extra", "yes");
                            pending_intent = PendingIntent.getService(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                            if (fm.getText().toString().equals("")) {
                                fm.setError("Please enter minute");
                                value.put("Set1", "Disable");
                            } else {
                                value.put("Set1", hour);
                                value.put("Secret", minute);
                                value.put("DelaySet1", Integer.parseInt(fm.getText().toString()));
                                time.updateChildren(value);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                            }
                        }
                        else{
                            alarmManager.cancel(pending_intent);
                            value.put("DelaySet1", "0");
                            value.put("Set1", "Disable");
                            value.put("Secret", "0");
                            time.updateChildren(value);
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        return view;
    }
}