package ru.falseteam.schedule.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Prog on 20.02.17.
 */

public class ServiceLoader extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, ScheduleService.class);
        context.startService(intentService);
    }
}