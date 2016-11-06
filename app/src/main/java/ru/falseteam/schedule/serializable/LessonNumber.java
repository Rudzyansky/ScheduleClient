package ru.falseteam.schedule.serializable;

import java.io.Serializable;
import java.sql.Time;

public class LessonNumber implements Serializable {
    public int id;
    public Time begin;
    public Time end;

    @Override
    public String toString() {
        return id + " " + begin + " " + end;
    }
}
