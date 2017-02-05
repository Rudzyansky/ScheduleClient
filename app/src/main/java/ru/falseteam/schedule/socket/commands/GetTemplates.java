package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;
import ru.falseteam.vframe.socket.Container;

public class GetTemplates extends ClientProtocolAbstract {

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, ClientSocketWorker worker) {
        MainData.setTemplates((List<Template>) map.get("templates"));
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_templates");
        return new Container(GetTemplates.class.getSimpleName(), map);
    }
}
