package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetLessons extends CommandAbstract {
    public GetLessons() {
        super("get_lessons");
    }


    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        MainData.setLessons((List<Lesson>) map.get("lessons"));
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_lessons");
        return map;
    }
}
