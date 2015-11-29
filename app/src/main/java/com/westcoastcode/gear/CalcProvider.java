package com.westcoastcode.gear;

import android.util.Log;

/**
 * Created by Jeff on 11/15/2015.
 */
public class CalcProvider {

    private static final String TAG = "CalcProvider";

    private static CalcProvider mInstance = null;

    private CalcProvider(){

    }

    public static CalcProvider getInstance(){
        if (mInstance == null){
            mInstance = new CalcProvider();
        }
        return mInstance;
    }

    private double[] mGears;
    private double mRpm;
    private double mTire;

    public void setRpm(double rpm){
        //Log.d(TAG, "setRpm: " + String.valueOf(rpm));
        mRpm = rpm;
    }

    public int getTransGearCount(){
        return mGearCount;
    }

    public void setTire(double tire){
        mTire = tire;
    }

    private int mGearCount = 0;

    public void setTrans(double... gears){
        mGearCount = 0;
        mGears = new double[gears.length];
        for(int i = 0; i < gears.length; i++){
            mGears[i] = gears[i];
            if (gears[i] > 0) mGearCount++;
        }
    }

    public double strToDouble(String s){
        try{
            return Double.parseDouble(s);
        }catch (NumberFormatException e) {
            return 0;
        }
    }

    //public double calcSpeed(double rpm, int gear, double ratio, double C){
    public double calcSpeed(int gear, double ratio, double C){

        double rpm = mRpm;

        //Log.d(TAG, String.format("calcSpeed(%.2f, %d, %.2f, %.2f)", rpm, gear, ratio, C));

        //Validate input
        if (!(rpm > 0)) return 0;
        if (!(C > 0)) return 0;
        if (!(ratio > 0)) return 0;
        if (!(gear > 0)) return 0;

        //Verify gears has been initiated
        if (mGears == null) {throw new RuntimeException("gears are null"); }//return 0;
        if (!(mGears.length >= gear)) return 0;
        double transmisionGear = mGears[gear-1];
        if (!(transmisionGear > 0)) {throw new RuntimeException("transmission gear not found"); }//return 0;

        //Divide the RPM / Transmission gear
        double trannyRpm = rpm / transmisionGear;

        //Divide by gear ration
        double diffRpm = trannyRpm / ratio;

        //Assume C is in inches
        double feetC = C  / 12;

        double feetPerMinute = feetC * diffRpm;

        double speed = (feetPerMinute / 5280) * 60;

        //Log.d(TAG, String.format("calcSpeed(%.2f, %d, %.2f, %.2f) = %.1f", rpm, gear, ratio, C, speed));

        return speed;
    }
}
