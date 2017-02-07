package ru.falseteam.schedule.socket.commands;

import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.vframe.redraw.Redrawer;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetJournal extends ProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setJournal((List<JournalRecord>) map.get("journal"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        return new Container(GetJournal.class.getSimpleName(), true);
    }
}
