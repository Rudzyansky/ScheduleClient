package ru.falseteam.schedule.serializable;

import java.io.Serializable;

public class Lesson implements Serializable {
    public boolean exists;
    public int id;
    public String type;
    public String name;
    public String audience;
    public String teacher;
    public String lastTask;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (exists != lesson.exists) return false;
        if (id != lesson.id) return false;
        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        if (audience != null ? !audience.equals(lesson.audience) : lesson.audience != null)
            return false;
        if (teacher != null ? !teacher.equals(lesson.teacher) : lesson.teacher != null)
            return false;
        return lastTask != null ? lastTask.equals(lesson.lastTask) : lesson.lastTask == null;

    }

    @Override
    public int hashCode() {
        int result = (exists ? 1 : 0);
        result = 31 * result + id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (audience != null ? audience.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (lastTask != null ? lastTask.hashCode() : 0);
        return result;
    }

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
