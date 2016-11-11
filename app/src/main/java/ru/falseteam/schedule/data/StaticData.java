package ru.falseteam.schedule.data;

import android.content.Context;
import android.content.pm.PackageManager;

import ru.falseteam.schedule.R;

public class StaticData {
    private static String clientVersion;
    private static String hostname;
    private static String publicPass;
    private static int portSchedule;
    private static int portUpdate;

    static void init(Context context){
        try {
            StaticData.clientVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StaticData.hostname = context.getString(R.string.hostname);
        StaticData.publicPass = context.getString(R.string.public_pass);
        StaticData.portSchedule = context.getResources().getInteger(R.integer.port_schedule);
        StaticData.portUpdate = context.getResources().getInteger(R.integer.port_update);
    }


    public static int getPortSchedule() {
        return portSchedule;
    }

    public static int getPortUpdate() {
        return portUpdate;
    }

    public static String getPublicPass() {
        return publicPass;
    }

    public static String getHostname() {
        return hostname;
    }

    public static String getClientVersion() {
        return clientVersion;
    }
}
