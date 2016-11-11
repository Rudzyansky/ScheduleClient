package ru.falseteam.schedule.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;

public class Data {


    static Groups currentGroup;
    static String name;
    static Bitmap userIcon;
    static String userIconUrl;
    static String userIconPath;

    static SharedPreferences preferences;
    static WeakReference<Context> context;


    public static void setCurrentGroup(Groups currentGroup) {
        Data.currentGroup = currentGroup;
        OnChangeGroup.change();
        Redrawer.redraw();
    }


    // Гетеры
    public static Context getContext() {
        return context.get();
    }

    public static Groups getCurrentGroup() {
        return currentGroup;
    }

    public static String getName() {
        return name;
    }

    public static Bitmap getUserIcon() {
        return userIcon;
    }

}
