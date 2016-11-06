package ru.falseteam.schedule.serializable;

import java.io.Serializable;
import java.sql.Time;

/**
 * Structure contains all information about template.
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class Template implements Serializable {
    public boolean exists;
    public int id;
    public WeekDay weekDay;
    public LessonNumber lessonNumber;
    public Lesson lesson;
    public int weekEvenness;

    // Empty private constructor.
    private Template() {
    }

    public static class Factory {
        public static Template getDefault() {
            Template template = new Template();
            template.exists = false;
            template.lesson = Lesson.Factory.getDefault();
            return template;
        }
    }
}
