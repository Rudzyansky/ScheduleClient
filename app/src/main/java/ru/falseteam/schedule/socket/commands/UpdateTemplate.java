package ru.falseteam.schedule.socket.commands;

import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.vframe.socket.Container;

public class UpdateTemplate {
    public static Container getRequest(Template template) {
        Container c = new Container(UpdateTemplate.class.getSimpleName(), true);
        c.data.put("template", template);
        return c;
    }
}
