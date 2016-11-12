package ru.falseteam.schedule.data;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Template;

/**
 * Содержит основные динамические данные
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
public class MainData {

    private static Groups currentGroup;
    private static WeakReference<Context> context;

    private static String dataDir;

    private static List<Template> templates;

    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
        dataDir = context.getApplicationInfo().dataDir;

        currentGroup = Groups.disconnected;

        templates = DataLoader.loadFromBinaryFile(dataDir + "/templates.bin");
    }

    public static void setCurrentGroup(Groups currentGroup) {
        MainData.currentGroup = currentGroup;
        OnChangeGroup.change();
        Redrawer.redraw();
    }

    //Сетеры. TODO перенсти в дата лодер.
    public static void setTemplates(List<Template> templates) {
        MainData.templates = templates;
        DataLoader.saveToBinaryFile(templates, dataDir + "/templates.bin");
        Redrawer.redraw();
    }

    // Гетеры
    public static Context getContext() {
        return context.get();
    }

    public static Groups getCurrentGroup() {
        return currentGroup;
    }

    public static List<Template> getTemplates() {
        return templates;
    }
}
