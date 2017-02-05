package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;
import ru.falseteam.vframe.socket.Container;

public class GetWeekDays extends ClientProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, ClientSocketWorker worker) {
        MainData.setWeekDays((List<WeekDay>) map.get("week_days"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_week_days");
        return new Container(GetWeekDays.class.getSimpleName(), map);
    }
}
