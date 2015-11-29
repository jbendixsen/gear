package com.westcoastcode.gear.eventbus;

/**
 * Created by Jeff on 5/18/2015.
 */
public class RpmChangedEvent {

    private double mRpm;

    public RpmChangedEvent(double rpm){
        mRpm = rpm;
    }

    public double getRpm(){
        return mRpm;
    }
}
