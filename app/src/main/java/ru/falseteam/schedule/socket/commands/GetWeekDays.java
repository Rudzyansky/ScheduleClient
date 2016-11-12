package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetWeekDays extends CommandAbstract {

    public GetWeekDays() {
        super("get_week_days");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        MainData.setWeekDays((List<WeekDay>) map.get("week_days"));
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_week_days");
        return map;
    }
}
