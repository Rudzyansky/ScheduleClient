package ru.falseteam.schedule.data;

import android.content.Context;

import java.lang.ref.WeakReference;

import ru.falseteam.schedule.R;

public class DataLoader {
    public static void load(Context context) {
        Data.context = new WeakReference<Context>(context);

        try {
            StaticData.clientVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception ignore) {
        }
        StaticData.hostname = context.getString(R.string.hostname);
        StaticData.publicPass = context.getString(R.string.public_pass);
        StaticData.portSchedule = context.getResources().getInteger(R.integer.port_schedule);
        StaticData.portUpdate = context.getResources().getInteger(R.integer.port_update);
    }
}
