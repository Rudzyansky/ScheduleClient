package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.vframe.socket.Container;

public class UpdateTemplate {
    public static Container getRequest(Template template) {
        Map<String, Object> request = new HashMap<>();
        request.put("command", "update_template");
        request.put("template", template);
        return new Container(UpdateTemplate.class.getSimpleName(), request);
    }
}
