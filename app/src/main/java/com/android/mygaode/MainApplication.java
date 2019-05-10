package com.android.mygaode;

import android.app.Application;
import android.os.Handler;

public class MainApplication extends Application {

    private static Handler mainThreadHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        mainThreadHandler = new Handler();
    }

    public static Handler getMainThreadHandler() {
        return mainThreadHandler;
    }
}
