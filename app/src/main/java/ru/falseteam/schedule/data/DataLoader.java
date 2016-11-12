package ru.falseteam.schedule.data;

import android.content.Context;

import ru.falseteam.schedule.listeners.Redrawer;

public class DataLoader {
    public static void load(Context context) {
        StaticData.init(context);
        MainData.init(context);
        VkData.init(context);

        Redrawer.redraw();
    }


}
