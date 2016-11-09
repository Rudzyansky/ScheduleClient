package ru.falseteam.schedule.serializable;

import java.io.Serializable;
import java.sql.Time;

public class LessonNumber implements Serializable {
    public int id;
    public Time begin;
    public Time end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonNumber that = (LessonNumber) o;

        if (id != that.id) return false;
        if (begin != null ? !begin.equals(that.begin) : that.begin != null) return false;
        return end != null ? end.equals(that.end) : that.end == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (begin != null ? begin.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return id + ". " + begin.toString().substring(0, 5) + " - " + end.toString().substring(0, 5);
    }
}
