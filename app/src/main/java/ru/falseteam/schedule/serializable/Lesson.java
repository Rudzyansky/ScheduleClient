package ru.falseteam.schedule.serializable;

import java.io.Serializable;

/**
 * Created by Prog on 01.11.16.
 */
public class Lesson implements Serializable {
    public boolean exists;
    public int id;
    public String name;
    public String audience;
    public String teacher;
    public String lastTask;

    @Override
    public String toString() {
        return name;
    }

    private Lesson() {
    }

    public static class Factory {
        public static Lesson getDefault() {
            Lesson lesson = new Lesson();
            lesson.exists = false;
            return lesson;
        }
    }
}
