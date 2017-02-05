package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;
import ru.falseteam.vframe.socket.Container;

public class GetLessons extends ClientProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, ClientSocketWorker worker) {
        MainData.setLessons((List<Lesson>) map.get("lessons"));
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_lessons");
        return new Container(GetLessons.class.getSimpleName(), map);
    }
}
