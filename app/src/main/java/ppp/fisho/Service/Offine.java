package ppp.fisho.Service;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by best on 13/9/2560.
 */

public class Offine extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}