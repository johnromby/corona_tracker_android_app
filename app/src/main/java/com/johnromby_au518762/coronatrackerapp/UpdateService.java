// Most code taken from: (E20) ITSMAP "L5 - Services and Asynch Processing" DemoService project.

package com.johnromby_au518762.coronatrackerapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateService extends Service {
    // Debugging Tag
    private static final String TAG = "UpdateService";

    public static final String SERVICE_CHANNEL = "serviceChannel";
    public static final int NOTIFICATION_ID = 42;
    public static final int SLEEP_TIMER = 60000; // milliseconds

    private ExecutorService execService;
    private boolean started = false;

    private CountryRepository repository;

    public UpdateService() {
        repository = CountryRepository.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Service started");

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Live Corona Data Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // This should start the foreground service.
        startForeground(NOTIFICATION_ID, getNewNotification());

        doBackgroundStuff();
        return START_STICKY;
    }

    private Notification getNewNotification() {
        Country c = repository.getSingleRandomCountry();
        return new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle("Live Corona Data for " + c.getCountryName())
                .setContentText("Cases: " + c.getNumInfectedAsString() + " / Deaths: " + c.getNumDeathAsString())
                .setSmallIcon(R.drawable.ic_new_corona_numbers)
                .setTicker("Brought to you by: https://covid19api.com")
                .build();
    }

    private void doBackgroundStuff() {
        Log.d(TAG, "doBackgroundStuff: Doing background stuff...");
        if (!started) {
            started = true;
            doRecursiveWork();
        }
    }

    private void doRecursiveWork() {
        if (execService == null) {
            execService = Executors.newSingleThreadExecutor();
        }

        execService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIMER);
                    Notification notification = getNewNotification();
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(NOTIFICATION_ID, notification);
                    Log.d(TAG, "Notification updated.");
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: ERROR", e);
                }

                if (started) {
                    doRecursiveWork();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Service bound");
        // TO-DO: Return the communication channel to the service.
        return null; // Not gonna use this binding for now.
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }
}