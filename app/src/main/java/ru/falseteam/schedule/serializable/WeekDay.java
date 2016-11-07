package ru.falseteam.schedule.serializable;

import java.io.Serializable;

public class WeekDay implements Serializable {
    public int id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeekDay weekDay = (WeekDay) o;

        if (id != weekDay.id) return false;
        return name != null ? name.equals(weekDay.name) : weekDay.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
