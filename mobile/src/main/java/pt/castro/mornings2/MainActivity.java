package pt.castro.mornings2;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ConnectionHelper mConnectionHelper;
    private ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        mListFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, mListFragment).commit();
        mConnectionHelper = new ConnectionHelper();
        mConnectionHelper.setConnectionListener(new ConnectionHelper.ConnectionObserver() {
            @Override
            public void onConnectionChange(boolean connected) {
                if (connected) {
                    mListFragment.setMessage(getString(R.string.starting));
                } else {
                    mListFragment.setMessage(getString(R.string.no_connection));
                }
            }
        });

        setTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnectionHelper.unsetConnectionListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        boolean autoScroll = settings.getBoolean("auto_scroll", false);
        MenuItem item = menu.findItem(R.id.auto_scroll);
        item.setChecked(autoScroll);
        mListFragment.setAutoScroll(autoScroll);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.auto_scroll) {
            item.setChecked(!item.isChecked());
            SharedPreferences settings = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("auto_scroll", item.isChecked());
            editor.apply();
            mListFragment.setAutoScroll(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitle() {
        String[] titles = getResources().getStringArray(R.array.title);
        int random = new Random().nextInt(titles.length);
        setTitle(titles[random]);
    }

    public void setBitmap(Bitmap bitmap) {
//        ImageView b = (ImageView) findViewById(R.id.bitmap);
//        b.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    //    private void addAd() {
//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                NativeExpressAdView ad = new NativeExpressAdView(MainActivity.this);
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout
//                        .LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                lp.gravity = Gravity.BOTTOM;
//                ad.setLayoutParams(lp);
//                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//                ad.setAdSize(new AdSize((int) dpWidth, 80));
//                ad.setAdUnitId("ca-app-pub-8849549484897415/5873983884");
//                AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest
//                        .DEVICE_ID_EMULATOR).build();
//                ad.loadAd(adRequest);
//                FrameLayout parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
//                parentLayout.addView(ad);
//            }
//        });
//    }
}