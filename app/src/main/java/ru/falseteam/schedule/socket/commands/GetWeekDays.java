package ru.falseteam.schedule.socket.commands;

import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.vframe.redraw.Redrawer;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetWeekDays extends ProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setWeekDays((List<WeekDay>) map.get("week_days"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        return new Container(GetWeekDays.class.getSimpleName(), true);
    }
}
