package com.westcoastcode.gear;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

import com.squareup.otto.Subscribe;
import com.westcoastcode.gear.eventbus.BusProvider;
import com.westcoastcode.gear.eventbus.RpmChangedEvent;
import com.westcoastcode.gear.fragments.MainFragment;
import com.westcoastcode.gear.models.Transmissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.resultListener{

    private static final String TAG = "MainActivity";

    protected static final float DEFAULT_RPM = 2000.00f;
    protected static final float DEFAULT_TIRE = 103.6728f;

    private DrawerLayout mDrawer;
    //private FloatingActionButton mFab;
    //private ImageButton mRpmSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeSharedPref();
//        mFab = (FloatingActionButton) findViewById(R.id.fab);
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showHideRpmSlider();
//            }
//        });

//        mRpmSlider = (ImageButton) findViewById(R.id.rpmSlider);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CalcProvider.getInstance().setTrans(Transmissions.transT176);
    }

//    private void showSnackbar(String msg){
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        // post rpm refresh?
        //startThinkingAboutHidingTheFab();
        BusProvider.getInstance().post(new RpmChangedEvent(getRpm()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_transmission) {
            Snackbar.make(mDrawer, "Transmission", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_tire) {
            Snackbar.make(mDrawer, "Tires", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_transfer_case) {
            Snackbar.make(mDrawer, "Transfer Case", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_gears) {
            Snackbar.make(mDrawer, "Final Gear", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_settings) {
            Snackbar.make(mDrawer, "Settings", Snackbar.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public double calcSpeed(double ratio, int gear) {
        double C = 103.6728d; //33 x 10.5
        double s = CalcProvider.getInstance().calcSpeed(gear, ratio, C);
        //Log.d(TAG, "calcSpeed: " + String.valueOf(s));
        return s;//CalcProvider.getInstance().calcSpeed(gear, ratio, C);
    }

    @Subscribe
    public void onRpmChangeEvent(RpmChangedEvent event){
        //Log.d(TAG, "onRpmChangeEvent: " + String.valueOf(event.getRpm()));
        if (event.getRpm() > 0){
            setRpm(event.getRpm());
            //Trigger a recalc
        }
    }

    protected void setRpm(double rpm){
        SharedPreferences prefs = GearApplication.getAppContext().getSharedPreferences(getString(R.string.gear_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putFloat(getString(R.string.rpm_pref_key), (float)rpm);
        e.apply();
        CalcProvider.getInstance().setRpm(rpm);
    }

    public double getRpm(){
        SharedPreferences prefs = GearApplication.getAppContext().getSharedPreferences(getString(R.string.gear_preferences), Context.MODE_PRIVATE);
        return prefs.getFloat(getString(R.string.rpm_pref_key), DEFAULT_RPM);
    }

    protected void initializeSharedPref(){
        SharedPreferences prefs = GearApplication.getAppContext().getSharedPreferences(getString(R.string.gear_preferences), Context.MODE_PRIVATE);

        if (!prefs.contains(getString(R.string.rpm_pref_key))){
            SharedPreferences.Editor e = prefs.edit();
            e.putFloat(getString(R.string.rpm_pref_key), 2000);
            e.apply();
        }

        if (!prefs.contains(getString(R.string.tire_pref_key))){
            SharedPreferences.Editor e = prefs.edit();
            e.putFloat(getString(R.string.tire_pref_key), 103.6728f);
            e.apply();
        }

        if (!prefs.contains("trans")){
            SharedPreferences.Editor e = prefs.edit();
            e.putInt("trans", 0);
            e.apply();
        }

    }



    @Override
    public void setTouched() {
//        if (mFabHiding ){
//            showHideFab(true);
//            mHandler.removeCallbacks(mRunnable);
//            startThinkingAboutHidingTheFab();
//        } else {
//            showHideFab(false);
//        }
    }

//    private static final int FAB_HIDE_DELAY = 4000;
//    private Handler mHandler = new Handler();
//    private Runnable mRunnable;

//    private void startThinkingAboutHidingTheFab(){
//        if (mRunnable == null) {
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    hideTheFab();
//                }
//            };
//        }
//        mHandler.postDelayed(mRunnable, FAB_HIDE_DELAY);
//    }

//    private void hideTheFab(){
//        showHideFab(false);
//    }

//    private boolean mFabHiding = false;
//    private void showHideFab(boolean show){
//        mFab.animate().alpha(show ? 1 : 0).setInterpolator(new AccelerateDecelerateInterpolator()).start();
//        mFabHiding = !show;
//    }

//    //private boolean mRpmSliderHiding = false;
//    private void showHideRpmSlider(){
//        boolean showing = mRpmSlider.getAlpha() > 0.1;
//        Log.d(TAG, "showHideRpmSlider-did yo get this? " + String.valueOf(showing));
//        mRpmSlider.animate().alpha(showing ? 0 : 1).setInterpolator(new AccelerateDecelerateInterpolator()).start();
//        //mRpmSliderHiding = !show;
//    }


}
