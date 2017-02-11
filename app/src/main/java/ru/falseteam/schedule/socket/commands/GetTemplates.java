package ru.falseteam.schedule.socket.commands;

import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawer;
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
        return new Container(GetTemplates.class.getSimpleName(), true);
    }
}
