package pt.castro.mornings2;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by lourenco on 15/06/2017.
 */

public class ApplicationExtension extends Application {
    public static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
    }
}