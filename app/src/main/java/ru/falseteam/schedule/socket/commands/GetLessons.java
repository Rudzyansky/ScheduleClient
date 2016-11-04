package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetLessons extends CommandAbstract {
    public GetLessons() {
        super("get_pairs");
    }

    public static List<Lesson> lessons;

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        lessons = (List<Lesson>) map.get("lessons");
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_pairs");
        return map;
    }
}
