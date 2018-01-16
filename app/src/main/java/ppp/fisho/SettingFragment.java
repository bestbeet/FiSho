package ppp.fisho;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by best on 4/4/2560.
 */

public class SettingFragment extends Fragment {

    private DatabaseReference foodset,oxygenset;
    private EditText SFL,SO,Contoller;
    private Button BFL,BO,Clear,Select;
    private TextView Tset;
    public static String idc;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_layout, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Setting");
        Tset = (TextView) view.findViewById(R.id.ID);
        SFL = (EditText) view.findViewById(R.id.TSFL);
        Contoller = (EditText) view.findViewById(R.id.SID);
        Clear = (Button) view.findViewById(R.id.CD);
        Select = (Button) view.findViewById(R.id.CID);
        BFL = (Button) view.findViewById(R.id.BSFL);
        SO =  (EditText) view.findViewById(R.id.TSO);
        BO = (Button) view.findViewById(R.id.BSO);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Tset.setText("Current ID: " + prefs.getString("IDC", ""));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String icd = prefs.getString("IDC", "");

        foodset = database.getReference(icd).child("FoodLevel");
        foodset.keepSynced(true);
        foodset.orderByValue().limitToLast(1);
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("IDC", Contoller.getText().toString());
                editor.commit();
                Tset.setText("Current ID: " + prefs.getString("IDC", ""));
            }
        });
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Sure..!!!");
                alertDialogBuilder.setMessage("Are you sure,You want to delete data");
                alertDialogBuilder.setIcon(R.drawable.question);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.commit();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });

        BFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodset.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> value1 = new HashMap<String, Object>();
                        if (SFL.getText().toString().equals("")) {
                            SFL.setError("Please enter Food Level (cm)");
                        } else {
                            value1.put("LevelSet", Float.parseFloat(SFL.getText().toString()));
                        }
                        foodset.updateChildren(value1);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        oxygenset = database.getReference(icd).child("TankSet");
        oxygenset.keepSynced(true);
        oxygenset.orderByValue().limitToLast(1);
        BO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oxygenset.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> value2 = new HashMap<String, Object>();
                        if (SO.getText().toString().equals("")) {
                            SO.setError("Please enter Oxygen time on-off (minute)");
                        } else {
                            value2.put("TimeOxygen", Float.parseFloat(SO.getText().toString()));
                        }
                        oxygenset.updateChildren(value2);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        return view;
    }
    public static String getIDC() {
        return idc;
    }
}