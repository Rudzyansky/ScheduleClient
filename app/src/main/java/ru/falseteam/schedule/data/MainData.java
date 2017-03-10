package ru.falseteam.schedule.data;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Содержит основные динамические данные
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
public class MainData {
    private static WeakReference<Context> context;

    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
//        dataDir = context.getApplicationInfo().dataDir + "/cache/";
//        templates = DataLoader.loadFromBinaryFile(dataDir + "/templates.bin");
    }

    // Геттеры
    public static Context getContext() {
        return context.get();
    }
}
