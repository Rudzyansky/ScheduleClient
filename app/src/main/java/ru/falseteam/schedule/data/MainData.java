package ru.falseteam.schedule.data;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.serializable.LessonNumber;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.serializable.WeekDay;

/**
 * Содержит основные динамические данные
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
public class MainData {

    private static Groups currentGroup;
    private static WeakReference<Context> context;

    private static String cacheDir;

    private static List<Template> templates;
    private static List<LessonNumber> lessonNumbers;
    private static List<Lesson> lessons;
    private static ArrayList<User> users;
    private static List<WeekDay> weekDays;


    static void init(Context context) {
        MainData.context = new WeakReference<>(context);
        cacheDir = context.getApplicationInfo().dataDir + "/cache";

        currentGroup = Groups.disconnected;

        templates = DataLoader.loadFromBinaryFile(cacheDir + "/templates.bin");
    }

    public static void setCurrentGroup(Groups currentGroup) {
        MainData.currentGroup = currentGroup;
        OnChangeGroup.change();
        Redrawer.redraw();
    }


    public static void setTemplates(List<Template> templates) {
        MainData.templates = templates;
        DataLoader.saveToBinaryFile(templates, cacheDir + "/templates.bin");
        Redrawer.redraw();
    }

    public static void setLessonNumbers(List<LessonNumber> lessonNumbers) {
        MainData.lessonNumbers = lessonNumbers;
        Redrawer.redraw();
    }

    public static void setLessons(List<Lesson> lessons) {
        MainData.lessons = lessons;
        Redrawer.redraw();
    }

    public static void setUsers(ArrayList<User> users) {
        MainData.users = users;
        Redrawer.redraw();
    }

    public static void setWeekDays(List<WeekDay> weekDays) {
        MainData.weekDays = weekDays;
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

    public static List<LessonNumber> getLessonNumbers() {
        return lessonNumbers;
    }

    public static List<Lesson> getLessons() {
        return lessons;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static List<WeekDay> getWeekDays() {
        return weekDays;
    }
}
