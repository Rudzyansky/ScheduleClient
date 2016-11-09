package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.serializable.Template;

public class DeleteTemplate {
    public static Map<String, Object> getRequest(Template template) {
        Map<String, Object> request = new HashMap<>();
        request.put("command", "delete_template");
        request.put("template", template);
        return request;
    }
}
