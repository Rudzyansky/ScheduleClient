package ru.falseteam.schedule.socket.commands;

import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.LessonNumber;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetLessonNumbers extends ProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setLessonNumbers((List<LessonNumber>) map.get("lesson_numbers"));
    }

    public static Container getRequest() {
        return new Container(GetLessonNumbers.class.getSimpleName(), true);
    }
}
