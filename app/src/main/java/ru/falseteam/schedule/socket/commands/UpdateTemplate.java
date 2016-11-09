package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.serializable.Template;

public class UpdateTemplate {
    public static Map<String, Object> getRequest(Template template) {
        Map<String, Object> request = new HashMap<>();
        request.put("command", "update_template");
        request.put("template", template);
        return request;
    }
}
