package ru.falseteam.schedule.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;

public class MainData {
    
    private static Groups currentGroup;
    private static WeakReference<Context> context;

    private static SharedPreferences preferences;

    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
        preferences = context.getSharedPreferences("MainData", Context.MODE_PRIVATE);

        currentGroup = Groups.disconnected;
    }


    public static void setCurrentGroup(Groups currentGroup) {
        MainData.currentGroup = currentGroup;
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

}
