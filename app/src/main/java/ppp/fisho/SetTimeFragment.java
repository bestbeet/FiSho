package ppp.fisho;

/**
 * Created by best on 8/4/2560.
 */
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetTimeFragment extends Fragment{
    private TimePicker timePicker;
    private Button morning, evening;
    private EditText minute;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_time, container, false);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        morning = (Button) view.findViewById(R.id.morning);
        evening = (Button) view.findViewById(R.id.evening);
        minute = (EditText) view.findViewById(R.id.minute);
        getActivity().setTitle("FiSho");

        morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetTimeFragment.this.getActivity(), "Morning Set: " + timePicker.getCurrentHour() + ":" +
                                timePicker.getCurrentMinute() + System.getProperty("line.separator") + " Time End: " + minute.getText() + " minute",
                        Toast.LENGTH_SHORT).show();
            }
        });

        evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetTimeFragment.this.getActivity(), "Evening Set: " + timePicker.getCurrentHour() + ":" +
                                timePicker.getCurrentMinute() + System.getProperty("line.separator") + " Time End: " + minute.getText() + " minute",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}