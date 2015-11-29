package com.westcoastcode.gear.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.westcoastcode.gear.R;
import com.westcoastcode.gear.eventbus.BusProvider;
import com.westcoastcode.gear.eventbus.RpmChangedEvent;

public class RpmFragment extends Fragment {


    private SeekBar mSeekRpm;
    private TextView mRpmText;


    public RpmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rpm, container, false);
        mRpmText = (TextView) view.findViewById(R.id.textRpm);
        mSeekRpm = (SeekBar) view.findViewById(R.id.seekRpm);
        mSeekRpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double rpm = i * 100;
                mRpmText.setText(String.format("%5.0f", rpm));
                BusProvider.getInstance().post(new RpmChangedEvent(rpm));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

}
