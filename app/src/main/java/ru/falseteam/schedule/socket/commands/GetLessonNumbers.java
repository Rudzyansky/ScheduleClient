package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.LessonNumber;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetLessonNumbers extends CommandAbstract {


    public GetLessonNumbers() {
        super("get_lesson_numbers");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        MainData.setLessonNumbers((List<LessonNumber>) map.get("lesson_numbers"));
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_lesson_numbers");
        return map;
    }
}
