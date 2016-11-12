package ru.falseteam.schedule.data;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Template;

public class MainData {

    private static Groups currentGroup;
    private static WeakReference<Context> context;

    private static List<Template> templates;


    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
        currentGroup = Groups.disconnected;

        loadTemplates();
    }


    public static void setCurrentGroup(Groups currentGroup) {
        MainData.currentGroup = currentGroup;
        OnChangeGroup.change();
        Redrawer.redraw();
    }

    //Сетеры. TODO перенсти в дата лодер.
    public static void setTemplates(List<Template> templates) {
        MainData.templates = templates;
        saveTemplate();
        Redrawer.redraw();
    }

    // Созранянлки в кэш. TODO перенести в дата лодер.
    private static void saveTemplate() {
        try {
            File file = new File(context.get().getApplicationInfo().dataDir + "/templates.bin");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(templates);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Загружалки

    @SuppressWarnings("unchecked")
    private static void loadTemplates() {
        try {
            File file = new File(context.get().getApplicationInfo().dataDir + "/templates.bin");
            if (!file.exists()) return;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            templates = (List<Template>) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignore) {
        }
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
