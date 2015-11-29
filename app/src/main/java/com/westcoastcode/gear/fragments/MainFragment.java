package com.westcoastcode.gear.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.westcoastcode.gear.CalcProvider;
import com.westcoastcode.gear.R;
import com.westcoastcode.gear.adapters.SpeedsAdapter;
import com.westcoastcode.gear.eventbus.BusProvider;
import com.westcoastcode.gear.eventbus.RpmChangedEvent;


public class MainFragment extends Fragment implements SpeedsAdapter.SpeedAdapterListener {

    private static final String TAG = "MainFragment";

    private RecyclerView  mRecyclerView;
    private SpeedsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private resultListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public void setListener(resultListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.resultsList);

        mAdapter = new SpeedsAdapter();
        mAdapter.setListener(this);
        mLayoutManager = new LinearLayoutManager(this.getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN){
                    mListener.setTouched();
                }
                //Log.d(TAG, String.format("onInterceptTouchEvent %d", e.getAction()));
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                //Log.d(TAG, "onTouchEvent");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                //Log.d(TAG, "onRequestDisallowInterceptTouchEvent");
            }
        });
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Show the fab", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (resultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement resultListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public double calcSpeed(double gearRation, int transGear) {
        //Called by SpeedsAdapter after notify dataset changed
        //Fragment calls activity to compute speed
        //Log.d(TAG, String.format("calcSpeed: %.0f, %d", gearRation, transGear));
        return mListener.calcSpeed(gearRation, transGear);
    }

    @Override
    public int getTransGearCount() {
        return CalcProvider.getInstance().getTransGearCount();
    }

    public interface resultListener{
        double calcSpeed(double ratio, int gear);
        void setTouched();
    }

    @Subscribe
    public void onRpmChangeEvent(RpmChangedEvent event){
        if (event.getRpm() > 0){
            //Log.d(TAG, "Rpm: " + Double.valueOf(event.getRpm()));
            mAdapter.notifyDataSetChanged();
        }
    }
}
