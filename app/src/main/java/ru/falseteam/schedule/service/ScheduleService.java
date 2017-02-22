package ru.falseteam.schedule.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ScheduleService extends Service {

    public static final String INTENT_NAME = "ru.falseteam.schedule.service.ServiceLoader";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent();
        intent.setAction(INTENT_NAME);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis()
                + (2000), pendingIntent);
        super.onTaskRemoved(rootIntent);
    }

}
