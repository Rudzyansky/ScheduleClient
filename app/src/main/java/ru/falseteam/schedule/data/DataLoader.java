package ru.falseteam.schedule.data;

import android.content.Context;

import ru.falseteam.schedule.listeners.Redrawer;

public class DataLoader {
    public static void init(Context context) {
        StaticData.init(context);
        MainData.init(context);
        VkData.init(context);

        Redrawer.redraw();
    }

    static Object loadFromBinaryFile(String path) {
        //TODO дописать это немного позже когда будет протестировано что дата работает корректно.
        return null;
    }

    static void saveToBinaryFile(Object data, String path) {

    }

}
