package pt.castro.mornings2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by lourenco on 18/06/2017.
 */

public class ConnectionHelper {

    private ValueEventListener mConnectionEventListener;

    public interface ConnectionObserver {
        void onConnectionChange(boolean connected);
    }

    public void setConnectionListener(final ConnectionObserver observer) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference("" + "" + ""
                + ".info/connected");
        mConnectionEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                observer.onConnectionChange(connected);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        connectedRef.addValueEventListener(mConnectionEventListener);
    }

    public void unsetConnectionListener() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference("" + "" + ""
                + ".info/connected");
        connectedRef.removeEventListener(mConnectionEventListener);
    }
}
