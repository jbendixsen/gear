package com.westcoastcode.gear.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Jeff on 5/17/2015.
 */
public class BusProvider {

    private static final String TAG = "BusProvider";

    private static Bus mBus;

    public static Bus getInstance(){
        if (mBus == null){
            mBus = new Bus();
        }
        return mBus;
    }

    private BusProvider(){}
}
