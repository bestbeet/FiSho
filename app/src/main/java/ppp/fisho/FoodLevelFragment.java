package ppp.fisho;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by best on 5/4/2560.
 */

public class FoodLevelFragment extends Fragment {

    private DatabaseReference gfood;
    private TextView FL;
    private EditText SFL;
    private Button BFL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.foodintake_layout,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Food Level");

        FL = (TextView) view.findViewById(R.id.TFL);
        SFL = (EditText) view.findViewById(R.id.TSFL);
        BFL = (Button) view.findViewById(R.id.BSFL);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        gfood = database.getReference("FoodLevel");
        gfood.keepSynced(true);
        gfood.orderByValue().limitToLast(1);
        gfood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String FoodLevel = String.valueOf(map.get("Level"));

                FL.setText("Food Level : " + FoodLevel + "%");
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        BFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gfood.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> value = new HashMap<String, Object>();
                        if (SFL.getText().toString().equals("")) {
                            SFL.setError("Please enter Food Level (cm)");
                        } else {
                            value.put("LevelSet", Float.parseFloat(SFL.getText().toString()));
                        }
                        gfood.updateChildren(value);

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