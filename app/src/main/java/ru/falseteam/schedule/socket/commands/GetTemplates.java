package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetTemplates extends ProtocolAbstract {

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setTemplates((List<Template>) map.get("templates"));
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_templates");
        return new Container(GetTemplates.class.getSimpleName(), map);
    }
}
