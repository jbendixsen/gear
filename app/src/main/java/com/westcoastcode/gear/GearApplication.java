package com.westcoastcode.gear;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jeff on 11/28/2015.
 */
public class GearApplication extends Application {

    private static final String TAG = "GearApplication";

    private static GearApplication sInstance;

    public GearApplication(){
        super();
        sInstance = this;
    }

    public static GearApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

}
