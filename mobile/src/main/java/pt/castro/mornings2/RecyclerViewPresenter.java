package pt.castro.mornings2;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.castro.mornings2.models.RadioModel;
import pt.castro.mornings2.models.ShowModel;
import pt.castro.mornings2.utils.ImageUtils;

/**
 * Created by lourenco on 16/06/2017.
 */

public class RecyclerViewPresenter implements GenericPresenter {

    private RecyclerView mRecyclerView;
    private ShowAdapter mAdapter;

    private ValueEventListener mShowsEventListener;
    private Handler mHandler;
    private boolean mAutoScroll;

    public RecyclerViewPresenter(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mHandler = new Handler();
    }

    public void updateView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.mAutoScroll = autoScroll;
    }

    @Override
    public void onResume() {
        updateTime();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        unsetShowsListener();
    }

    public interface LoadShowsObserver {
        void onShowsLoaded(List<ShowModel> shows);

        void onItemClick(ShowModel showModel, View view);
    }

    public void loadShows(LoadShowsObserver observer) {
        mAdapter = new ShowAdapter(getShows(observer), mRecyclerView.getContext(), observer);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<ShowModel> getShows(final LoadShowsObserver observer) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final List<ShowModel> models = new ArrayList<>();
        final DatabaseReference reference = database.getReference(FirebaseKeys.RADIOS);

        mShowsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, RadioModel> radios = new HashMap<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    RadioModel radioModel = dataSnapshot1.getValue(RadioModel.class);
                    radios.put(dataSnapshot1.getKey(), radioModel);
                }

                final DatabaseReference showsDatabase = database.getReference(FirebaseKeys.SHOWS);
                final Calendar calendar = Calendar.getInstance();

                showsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        models.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            ShowModel showModel = dataSnapshot1.getValue(ShowModel.class);
                            if (radios.get(showModel.getRadioName()) != null) {
                                showModel.setRadioModel(radios.get(showModel.getRadioName()));
                            }
                            Long time;
                            if (ApplicationExtension.DEBUG) {
                                time = (Long) dataSnapshot1.child(FirebaseKeys.TIMETABLE).child
                                        (FirebaseKeys.calendarIntToKey(5)).getValue();
                            } else {
                                time = (Long) dataSnapshot1.child(FirebaseKeys.TIMETABLE).child
                                        (FirebaseKeys.calendarIntToKey(calendar.get(Calendar
                                                .DAY_OF_WEEK))).getValue();
                            }
                            if (time == null) {
                                continue;
                            }
                            showModel.setShowTime(time.intValue());
                            models.add(showModel);
                        }
                        Collections.sort(models, new Comparator<ShowModel>() {
                            @Override
                            public int compare(ShowModel o1, ShowModel o2) {
                                return o1.getShowTime() - o2.getShowTime();
                            }
                        });
                        observer.onShowsLoaded(models);
                        mRecyclerView.getAdapter().notifyItemRangeInserted(1, models.size());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(mShowsEventListener);
        return models;
    }

    private List<ShowModel> getDummyShows(LoadShowsObserver observer) {
        List<ShowModel> list = new ArrayList<>();
        for (int x = 0; x < 100; x++) {
            ShowModel showModel = new ShowModel();
            showModel.setRadioModel(new RadioModel("Radio " + x, ImageUtils.encodeImage
                    (mRecyclerView.getContext().getResources(), R.mipmap.ic_launcher_round)));

            showModel.setShowTime(x * 10);
            showModel.setShowName("Show " + x);

            HashMap<String, Integer> timetable = new HashMap<>();
            for (int y = 2; y < 7; y++) {
                timetable.put(FirebaseKeys.calendarIntToKey(y), x * 10);
            }
            showModel.setTimetable(timetable);
            list.add(showModel);
        }
        observer.onShowsLoaded(list);
        return list;
    }

    private Map<String, RadioModel> loadRadios() {
        final Map<String, RadioModel> radios = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(FirebaseKeys.RADIOS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    RadioModel radioModel = dataSnapshot1.getValue(RadioModel.class);
                    radios.put(radioModel.getName(), radioModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return radios;
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);
        ((ShowAdapter) mRecyclerView.getAdapter()).update();
        if (mAutoScroll) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    int index = ((ShowAdapter) mRecyclerView.getAdapter()).getFirstofInterest();
                    if (index >= 0) {
                        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller
                                (mRecyclerView.getContext()) {


                            @Override
                            protected int getVerticalSnapPreference() {
                                return LinearSmoothScroller.SNAP_TO_START;
                            }

                            @Override
                            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                return 100f / displayMetrics.densityDpi;
                            }
                        };
                        smoothScroller.setTargetPosition(Math.max(0, index - 1));
                        mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                    }
                }
            });
        }
//        final long time = ApplicationExtension.DEBUG ? 1000 : 60000 - (seconds * 1000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateTime();
//            }
//        }, time);
    }

    private void unsetShowsListener() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(FirebaseKeys.RADIOS);
        reference.removeEventListener(mShowsEventListener);
    }
}
