package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetTemplates extends CommandAbstract {
    public static List<Template> templates;


    public GetTemplates() {
        super("get_templates");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        templates = (List<Template>) map.get("templates");
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_templates");
        return map;
    }
}
