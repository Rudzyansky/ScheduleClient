package ru.falseteam.schedule.socket.commands;

import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetLessons extends ProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setLessons((List<Lesson>) map.get("lessons"));
    }

    public static Container getRequest() {
        return new Container(GetLessons.class.getSimpleName(), true);
    }
}
