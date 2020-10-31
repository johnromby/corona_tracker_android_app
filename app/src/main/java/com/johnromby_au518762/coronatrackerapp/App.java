// Code taken from (E20) ITSMAP "L7 - Fragments and Advanced UI" Tracker demo app.

package com.johnromby_au518762.coronatrackerapp;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App instance; //the one application object for the application's life time

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;    //Android specifies that this is the first code to run in app, so should not be null from here on
    }

    //NB: Get Application context - ONLY for use outside UI (e.g. for repository)
    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}