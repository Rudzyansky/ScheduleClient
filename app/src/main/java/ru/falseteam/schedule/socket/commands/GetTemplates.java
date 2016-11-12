package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetTemplates extends CommandAbstract {

    public GetTemplates() {
        super("get_templates");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        MainData.setTemplates((List<Template>) map.get("templates"));
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_templates");
        return map;
    }
}
