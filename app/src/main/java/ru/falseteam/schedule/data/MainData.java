package ru.falseteam.schedule.data;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.vframe.redraw.Redrawer;

/**
 * Содержит основные динамические данные
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
public class MainData {

    private static WeakReference<Context> context;

    private static String dataDir;

    private static List<Template> templates;
    private static List<JournalRecord> journal;


    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
        dataDir = context.getApplicationInfo().dataDir + "/cache/";
        templates = DataLoader.loadFromBinaryFile(dataDir + "/templates.bin");
    }

    public static void setTemplates(List<Template> templates) {
        if (templates.equals(MainData.templates)) return;
        MainData.templates = templates;
        DataLoader.saveToBinaryFile(templates, dataDir + "/templates.bin");
        Redrawer.redraw();
    }

    public static void setJournal(List<JournalRecord> journal) {
        MainData.journal = journal;
        Redrawer.redraw();
    }

    // Геттеры
    public static Context getContext() {
        return context.get();
    }

    public static List<JournalRecord> getJournal() {
        return journal;
    }
}
