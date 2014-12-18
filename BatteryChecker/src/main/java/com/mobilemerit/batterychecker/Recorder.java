package com.mobilemerit.batterychecker;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobilemerit.batterymonitor.reader.PowerReader;

/**
 * Record battery information.
 * 
 *
 */
public class Recorder {

    private static final String PREF_NAME = "BatteryMonitor";

    private final String mName;

    private final long mStartTime;

    private final long mStartPower;

    public Recorder(String name) {
        mName = name;
        mStartTime = new Date().getTime();
        Long power = PowerReader.read();
        mStartPower = power == null ? -1 : power;
    }

    public Recorder(String name, String record) {
        mName = name;
        String[] tokens = record.split(",");
        mStartTime = Long.valueOf(tokens[1]);
        mStartPower = Long.valueOf(tokens[2]);
    }

    public String getName() {
        return mName;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getStartPower() {
        return mStartPower;
    }

    public long getDuration() {
        long now = new Date().getTime();
        return (now - mStartTime) / 1000;
    }

    public long getConsumption() {
        Long power = PowerReader.read();
        return power == null ? -1 : power - mStartPower;
    }

    public float getAverageConsumption() {
        return (float) getConsumption() / getDuration();
    }

    public void save() {
    	 try{
    		 Context context = App.getContext();
    	        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    	        prefs.edit().putString(mName, toString()).commit();
    	      
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }
        
    }

    public void remove() {
        Context context = App.getContext();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(mName).commit();
    }

    @Override
    public String toString() {
        return mName + "," + mStartTime + "," + mStartPower;
    }

    public static Recorder get(String name) {
        Context context = App.getContext();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String record = prefs.getString(name, null);
        if (record == null) {
            return null;
        } else {
            return new Recorder(name, record);
        }
    }

}
