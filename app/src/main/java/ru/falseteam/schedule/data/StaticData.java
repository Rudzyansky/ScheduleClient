package ru.falseteam.schedule.data;

/**
 * Created by Vlad on 12.11.2016.
 */

public class StaticData {
    static String clientVersion;
    static String hostname;
    static String publicPass;
    static int portSchedule;
    static int portUpdate;


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
