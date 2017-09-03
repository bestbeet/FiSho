package ppp.fisho;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.HashMap;

/**
 * Created by best on 29/3/2560.
 */

public class WaterQualityFragment extends Fragment {

        public DatabaseReference myRef;
        private TextView mFirebaseTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.waterquality_layout, container, false);

        mFirebaseTextView = (TextView) view.findViewById(R.id.WtemptextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.keepSynced(true);
        myRef.orderByValue().limitToLast(1);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map map = (Map)dataSnapshot.getValue();
                String value = String.valueOf(map.get("WaterTemp"));
                mFirebaseTextView.setText("Temperature : " + value + " C°");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  view;
        //return inflater.inflate(R.layout.waterquality_layout,null);
    }
}