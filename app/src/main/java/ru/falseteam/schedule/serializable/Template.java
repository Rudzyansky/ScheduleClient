package ru.falseteam.schedule.serializable;

import java.io.Serializable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (exists != template.exists) return false;
        if (id != template.id) return false;
        if (weekEvenness != template.weekEvenness) return false;
        if (weekDay != null ? !weekDay.equals(template.weekDay) : template.weekDay != null)
            return false;
        if (lessonNumber != null ? !lessonNumber.equals(template.lessonNumber) : template.lessonNumber != null)
            return false;
        return lesson != null ? lesson.equals(template.lesson) : template.lesson == null;

    }

    @Override
    public int hashCode() {
        int result = (exists ? 1 : 0);
        result = 31 * result + id;
        result = 31 * result + (weekDay != null ? weekDay.hashCode() : 0);
        result = 31 * result + (lessonNumber != null ? lessonNumber.hashCode() : 0);
        result = 31 * result + (lesson != null ? lesson.hashCode() : 0);
        result = 31 * result + weekEvenness;
        return result;
    }

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
