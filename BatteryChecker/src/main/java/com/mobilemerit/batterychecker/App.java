package com.mobilemerit.batterychecker;

import android.app.Application;
import android.app.WallpaperManager;
import android.content.Context;

/**
 * Application.
 * 
 *
 */
public class App extends Application {

    private static Context mInstance;

    public App(Context c) {
        mInstance = c;
    }

    public static Context getContext() {
    	Class<WallpaperManager> classVariable=WallpaperManager.class;
        return mInstance;
    }
   
}
